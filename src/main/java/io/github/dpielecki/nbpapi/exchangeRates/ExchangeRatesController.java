package io.github.dpielecki.nbpapi.exchangeRates;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RestController
@RequestMapping("/exchangerates")
public class ExchangeRatesController {

    @Autowired
    private ExchangeRatesService exchangeRatesService;

    private final String baseUrl = "http://api.nbp.pl/api/exchangerates/rates/";
    private final OkHttpClient httpClient = new OkHttpClient.Builder()
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build();

    public ExchangeRatesController(ExchangeRatesService exchangeRatesService) {
        this.exchangeRatesService = exchangeRatesService;
    }

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

    @GetMapping("/average/{currency}/{date}")
    public String getAverage(
        @Parameter(name = "currency") @PathVariable("currency") String currency, 
        @Parameter(name = "date") @PathVariable("date") String date) throws IOException {

        Request request = buildGetRequest(String.format("A/%s/%s", currency, date));

        return String.format(Locale.US, "%.2f", exchangeRatesService.getAverage(fetchData(request)));
    }

    @GetMapping(value = "/extremes/{currency}/{quotations}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExtremes(
        @Parameter(name = "currency") @PathVariable("currency") String currency, 
        @Parameter(name = "quotations") @PathVariable("quotations") int quotations) throws IOException {
            
        if (quotations < 1 || quotations > 255) {
            throw new IllegalArgumentException("Number of last quotations must be in range 1-255");
        }
        
        Request request = buildGetRequest(String.format("A/%s/last/%s", currency, quotations));
        
        return exchangeRatesService.getExtremes(fetchData(request));
    }

    @GetMapping("/majordifference/{currency}/{quotations}")
    public String getMajorDifference(
        @Parameter(name = "currency") @PathVariable("currency") String currency, 
        @Parameter(name = "quotations") @PathVariable("quotations") int quotations) throws IOException {

        if (quotations < 1 || quotations > 255) {
            throw new IllegalArgumentException("Number of last quotations must be in range 1-255");
        }
    
        Request request = buildGetRequest(String.format("C/%s/last/%s", currency, quotations));

        return String.format(Locale.US, "%.2f", exchangeRatesService.getMajorDifference(fetchData(request)));
    }
}
