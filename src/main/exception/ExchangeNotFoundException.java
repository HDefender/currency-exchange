package main.exception;

public class ExchangeNotFoundException extends RuntimeException{
    public ExchangeNotFoundException(String message) {
        super(message);
    }
}