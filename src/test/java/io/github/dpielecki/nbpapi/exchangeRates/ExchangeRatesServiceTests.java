package io.github.dpielecki.nbpapi.exchangeRates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ExchangeRatesServiceTests {

    @Autowired
    ExchangeRatesService exchangeRatesService;

    @Test
    public void getAverageShouldReturnCorrectValue() throws IOException {
        JSONObject jsonData = new JSONObject("{\"rates\": [{\"mid\": 4.57}]}");
        
        Double result = exchangeRatesService.getAverage(jsonData);

        assertEquals(4.57d, result);
    }

    @Test
    public void getExtremesShouldReturnCorrectValues() throws IOException {
        JSONObject jsonData = new JSONObject("{\"rates\": [{\"mid\": 4.57}, {\"mid\": 4.39}, {\"mid\": 4.40}]}");

        String result = exchangeRatesService.getExtremes(jsonData);

        assertEquals("{\"minAverageValue\": 4.39, \"maxAverageValue\": 4.57}", result);
    }

    @Test
    public void getMajorDifferenceShouldReturnCorrectValue() throws IOException {
        JSONObject jsonData = new JSONObject("{\"rates\": [{\"ask\": 4.57, \"bid\": 4.53}, {\"ask\": 4.58, \"bid\": 4.54}, {\"ask\": 4.56, \"bid\": 4.52}]}");

        Double result = exchangeRatesService.getMajorDifference(jsonData);

        assertTrue(Math.abs(result - 0.04d) < 0.001d);
    }
}
