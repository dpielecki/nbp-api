package io.github.dpielecki.nbpapi.exchangeRates;

public class NoDataException extends RuntimeException {
    public NoDataException(String message) {
        super(message);
    }
}
