package io.github.dpielecki.nbpapi.exchangeRates;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class ExchangeRatesService {

    private final OkHttpClient httpClient = new OkHttpClient();
    private final String baseUrl = "http://api.nbp.pl/api/exchangerates/rates/";

    private Request buildGetRequest(String resourcePath) {
        Request request = new Request.Builder()
            .url(baseUrl + resourcePath)
            .build();
        return request;
    }

    private JSONObject fetchData(Request request) throws IOException {
        Response response = httpClient.newCall(request).execute();
        if (response.code() == 400) {
            throw new IllegalArgumentException("Invalid request.");
        }
        else if (response.code() == 404) {
            throw new NoDataException("No data for given parameters.");
        }
        return new JSONObject(response.body().string());
    }

    public Double getAverage(String currency, String date) throws IOException {
        Request request = buildGetRequest(String.format("A/%s/%s", currency, date));
        JSONObject jsonData = fetchData(request);
        return jsonData
            .getJSONArray("rates")
            .getJSONObject(0)
            .getDouble("mid");
    }

    public String getExtremes(String currency, int quotations) throws IOException {
        if (quotations < 1 || quotations > 255) {
            throw new IllegalArgumentException("Number of last quotations must be in range 1-255");
        }
    
        Request request = buildGetRequest(String.format("A/%s/last/%s", currency, quotations));
        JSONArray rates = fetchData(request).getJSONArray("rates");

        double minRate = rates.getJSONObject(0).getDouble("mid");
        double maxRate = rates.getJSONObject(0).getDouble("mid");

        for (Object element : rates) {
            double dailyAverageRate = ((JSONObject) element).getDouble("mid");
            if (dailyAverageRate < minRate) {
                minRate = dailyAverageRate;
            }
            else if (dailyAverageRate > maxRate) {
                maxRate = dailyAverageRate;
            }
        }

        return String.format("{\"minAverageValue\": %s, \"maxAverageValue\": %s}", minRate, maxRate);
    }

    public Double getMajorDifference(String currency, int quotations) throws IOException {
        if (quotations < 1 || quotations > 255) {
            throw new IllegalArgumentException("Number of last quotations must be in range 1-255");
        }

        Request request = buildGetRequest(String.format("C/%s/last/%s", currency, quotations));
        JSONArray rates = fetchData(request).getJSONArray("rates");

        double majorDifference = rates.getJSONObject(0).getDouble("ask") - rates.getJSONObject(0).getDouble("bid");

        for (Object element : rates) {
            double dailyDifference = ((JSONObject) element).getDouble("ask") - ((JSONObject) element).getDouble("bid");
            if (dailyDifference > majorDifference) {
                majorDifference = dailyDifference;
            }
        }

        return majorDifference;
    }
}
