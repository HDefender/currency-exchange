package util;

import dto.request.CurrencyRequestDto;
import dto.request.ExchangeRateRequestDto;
import dto.request.ExchangeRequestDto;
import exception.IncorrectInputException;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class ValidationUtil {
    private static final Pattern CODE_PATTERN = Pattern.compile("^[a-zA-Z]+$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z ]+$");
    private static final Pattern RATE_PATTERN = Pattern.compile("\\d+");
    private static final BigDecimal MIN_VALUE = BigDecimal.valueOf(0.000001);
    private static final int URL_LENGTH = 6;
    private static final int CODE_LENGTH = 3;
    private static final int SIGN_LENGTH = 3;

    private ValidationUtil() {
    }

    public static void validateURL (String URL, int URLLength){

        if(URL == null){
            throw new IncorrectInputException("URL is null");
        }
        if(URL.isBlank()){
            throw new IncorrectInputException("Invalid code format. Code should not be blank");
        }
        if(URL.length() != URLLength){
            throw new IncorrectInputException("Invalid code length. Length should contain"+ URL_LENGTH +" characters");
        }
        if (!CODE_PATTERN.matcher(URL).matches()){
            throw new IncorrectInputException("Invalid code format. Code should contain only letters");
        }
    }

    public static void validateLengthCode(String code) {
        if (code.length() != CODE_LENGTH){
            throw new IncorrectInputException("Invalid code length. Length should be 3 characters");
        }
    }

    public static void validateCodeFormat(String code) {
        if (!CODE_PATTERN.matcher(code).matches()){
            throw new IncorrectInputException("Invalid code format. Code should contain only letters");
        }
    }

    public static void validateNameFormat(String name) {
        if (!NAME_PATTERN.matcher(name).matches()){
            throw new IncorrectInputException("Invalid name format. Name should be without digits");
        }
    }

    public static void validateRate(BigDecimal rate) {
        if (rate == null || rate.compareTo(MIN_VALUE) <= 0){
            throw new IncorrectInputException("Invalid rate. Rate should be greater than zero");
        }
    }

    public static void validateRateFormat(String parameter){
        if (NAME_PATTERN.matcher(parameter).matches()){
            throw new IncorrectInputException("Invalid rate. Rate should only digits");
        }
    }

    public static void validateInput (String input){
        if (input == null){
            throw new IncorrectInputException("Some parameter or parameters are null");
        }
        if (input.isBlank()){
            throw new IncorrectInputException("Blank input is not allowed");
        }
        if (input.isEmpty()){
            throw new IncorrectInputException("Empty input is not allowed");
        }
    }

    public static void validateSign (String sign){
        if(sign.length() > SIGN_LENGTH){
            throw new IncorrectInputException("Invalid sign. Sign should be less than or equal to 3");
        }
    }

    public static void validateCodePair (String first, String second){
        if(first.equals(second)){
            throw new IncorrectInputException("Invalid code pair. Code pair should not be equal");
        }
    }

    public static void validateCurrencyDto(CurrencyRequestDto currency){
        validateCodeFormat(currency.code());
        validateLengthCode(currency.code());
        validateNameFormat(currency.name());
        validateSign(currency.sign());
    }

    public static void validateExchangeRatesDto(ExchangeRateRequestDto exchangeRateRequestDto){
        validateInput(exchangeRateRequestDto.baseCurrency());
        validateInput(exchangeRateRequestDto.targetCurrency());
        validateLengthCode(exchangeRateRequestDto.baseCurrency());
        validateLengthCode(exchangeRateRequestDto.targetCurrency());
        validateCodeFormat(exchangeRateRequestDto.baseCurrency());
        validateCodeFormat(exchangeRateRequestDto.targetCurrency());
        validateCodePair(exchangeRateRequestDto.baseCurrency(), exchangeRateRequestDto.targetCurrency());
        validateRate(exchangeRateRequestDto.rate());
    }

    public static void validateExchangeDto (ExchangeRequestDto exchangeRequestDto){
        validateInput(exchangeRequestDto.baseCurrency());
        validateInput(exchangeRequestDto.targetCurrency());
        validateLengthCode(exchangeRequestDto.baseCurrency());
        validateLengthCode(exchangeRequestDto.targetCurrency());
        validateCodeFormat(exchangeRequestDto.baseCurrency());
        validateCodeFormat(exchangeRequestDto.targetCurrency());
        validateCodePair(exchangeRequestDto.baseCurrency(), exchangeRequestDto.targetCurrency());
        validateRate(exchangeRequestDto.amount());
    }
}
