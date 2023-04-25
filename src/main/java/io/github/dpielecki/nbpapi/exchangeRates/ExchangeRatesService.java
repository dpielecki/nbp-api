package io.github.dpielecki.nbpapi.exchangeRates;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRatesService {

    public Double getAverage(JSONObject jsonData) throws IOException {
        return jsonData
            .getJSONArray("rates")
            .getJSONObject(0)
            .getDouble("mid");
    }

    public String getExtremes(JSONObject jsonData) throws IOException {
        JSONArray rates = jsonData.getJSONArray("rates");
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

    public Double getMajorDifference(JSONObject jsonData) throws IOException {
        JSONArray rates = jsonData.getJSONArray("rates");
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
