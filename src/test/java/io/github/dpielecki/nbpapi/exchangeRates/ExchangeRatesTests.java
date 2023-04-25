package io.github.dpielecki.nbpapi.exchangeRates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ExchangeRatesTests {

    @Autowired
    ExchangeRatesService exchangeRatesService;

    @Test
    public void validGetAverageRequestShouldReturnCorrectValue() throws IOException {
        Double result = exchangeRatesService.getAverage("EUR", "2010-06-01");
        assertEquals(4.1208d, result);
    }

    @Test
    public void validGetExtremesRequestShouldReturnCorrectValues() throws IOException {
        String result = exchangeRatesService.getExtremes("EUR", 10);
        assertEquals("{\"minAverageValue\": 4.598, \"maxAverageValue\": 4.666}", result);
    }

    @Test
    public void validGetMajorDifferenceRequestShouldReturnCorrectValue() throws IOException {
        Double result = exchangeRatesService.getMajorDifference("EUR", 10);
        assertEquals(0.09339999999999993, result);
    }

    @Test
    public void whenDateInvalidGetAverageShouldThrowException() throws IOException {
        assertThrows(NoDataException.class, 
            () -> exchangeRatesService.getAverage("EUR", "202020-10-10"));
    }

    @Test
    public void whenDateIsWeekendGetAverageShouldThrowException() throws IOException {
        assertThrows(NoDataException.class, 
            () -> exchangeRatesService.getAverage("EUR", "2023-04-23"));
    }

    @Test
    public void whenCurrencyNotExistGetAverageShouldThrowException() throws IOException {
        assertThrows(NoDataException.class, 
            () -> exchangeRatesService.getAverage("RUE", "2023-04-24"));
    }

    @Test
    public void whenCurrencyNotExistGetExtremesShouldThrowException() throws IOException {
        assertThrows(NoDataException.class, 
            () -> exchangeRatesService.getExtremes("RUE", 10));
    }

    @Test
    public void whenCurrencyNotExistGetMajorDifferenceShouldThrowException() throws IOException {
        assertThrows(NoDataException.class, 
            () -> exchangeRatesService.getMajorDifference("RUE", 10));
    }

    @Test
    public void whenInvalidQuotationNumbertGetExtremesShouldThrowException() throws IOException {
        assertThrows(IllegalArgumentException.class, 
            () -> exchangeRatesService.getExtremes("EUR", 256));
    }

    @Test
    public void whenInvalidQuotationNumbertGetMajorDifferenceShouldThrowException() throws IOException {
        assertThrows(IllegalArgumentException.class, 
            () -> exchangeRatesService.getMajorDifference("EUR", 256));
    }


}
