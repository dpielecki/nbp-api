package io.github.dpielecki.nbpapi.exchangeRates;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exchangerates")
public class ExchangeRatesController {

    @Autowired
    private ExchangeRatesService exchangeRatesService;

    public ExchangeRatesController(ExchangeRatesService exchangeRatesService) {
        this.exchangeRatesService = exchangeRatesService;
    }
    
    @GetMapping("/average/{currency}/{date}")
    public String getAverage(@PathVariable("currency") String currency, @PathVariable("date") String date) throws IOException {
        return exchangeRatesService.getAverage(currency, date).toString();
    }

    @GetMapping(value = "/extremes/{currency}/{quotations}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExtremes(@PathVariable("currency") String currency, @PathVariable("quotations") int quotations) throws IOException {
        return exchangeRatesService.getExtremes(currency, quotations);
    }

    @GetMapping("/majordifference/{currency}/{quotations}")
    public String getMajorDifference(@PathVariable("currency") String currency, @PathVariable("quotations") int quotations) throws IOException {
        return exchangeRatesService.getMajorDifference(currency, quotations).toString();
    }
}
