package util;

import dto.request.CurrencyRequestDto;
import dto.request.ExchangeRateRequestDto;
import dto.request.ExchangeRequestDto;
import exception.IncorrectInputException;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public final class ValidationUtil {
    private static final Pattern CODE_PATTERN = Pattern.compile("^[a-zA-Z]+$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z ]+$");
    private static final int CODE_LENGTH = 3;
    private static final int SIGN_LENGTH = 3;

    private ValidationUtil() {
    }

    public static void validateUrl(String url, int urlLength) {
        if (url == null) {
            throw new IncorrectInputException("URL is null");
        }
        if (url.isBlank()) {
            throw new IncorrectInputException("Invalid code format. Code should not be blank");
        }
        if (url.length() != urlLength) {
            throw new IncorrectInputException("Invalid code length. Length should contain " + urlLength + " characters");
        }
        if (!CODE_PATTERN.matcher(url).matches()) {
            throw new IncorrectInputException("Invalid code format. Code should contain only letters");
        }
    }

    public static void validateLengthCode(String code) {
        if (code.length() != CODE_LENGTH) {
            throw new IncorrectInputException("Invalid code length. Length should be " + CODE_LENGTH + " characters");
        }
    }

    public static void validateCodeFormat(String code) {
        if (!CODE_PATTERN.matcher(code).matches()) {
            throw new IncorrectInputException("Invalid code format. Code should contain only letters");
        }
    }

    public static void validateNameFormat(String name) {
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new IncorrectInputException("Invalid name format. Name should be without digits");
        }
    }

    public static void validateRate(BigDecimal rate) {
        if (rate == null || rate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IncorrectInputException("Invalid rate. Rate should be greater than zero");
        }
    }

    public static BigDecimal parseRate(String parameter) {
        try {
            return new BigDecimal(parameter);
        } catch (NumberFormatException e) {
            throw new IncorrectInputException("Invalid rate. Rate should be a number");
        }
    }

    public static void validateInput(String input) {
        if (input == null) {
            throw new IncorrectInputException("Parameter should not be null");
        }
        if (input.isBlank()) {
            throw new IncorrectInputException("Blank input is not allowed");
        }
    }

    public static void validateSign(String sign) {
        if (sign == null || sign.isBlank()) {
            throw new IncorrectInputException("Invalid sign. Sign should not be blank");
        }
        if (sign.length() > SIGN_LENGTH) {
            throw new IncorrectInputException("Invalid sign. Sign should be less than or equal to " + SIGN_LENGTH + " characters");
        }
    }

    public static void validateCodePair(String first, String second) {
        if (first == null || second == null) {
            throw new IncorrectInputException("Invalid code pair. Codes should not be null");
        }
        if (first.equals(second)) {
            throw new IncorrectInputException("Invalid code pair. Code pair should not be equal");
        }
    }

    public static void validateCurrencyDto(CurrencyRequestDto currency) {
        validateInput(currency.code());
        validateLengthCode(currency.code());
        validateCodeFormat(currency.code());
        validateInput(currency.name());
        validateNameFormat(currency.name());
        validateInput(currency.sign());
        validateSign(currency.sign());
    }

    public static void validateExchangeRatesDto(ExchangeRateRequestDto exchangeRateRequestDto) {
        validateInput(exchangeRateRequestDto.baseCurrency());
        validateInput(exchangeRateRequestDto.targetCurrency());
        validateLengthCode(exchangeRateRequestDto.baseCurrency());
        validateLengthCode(exchangeRateRequestDto.targetCurrency());
        validateCodeFormat(exchangeRateRequestDto.baseCurrency());
        validateCodeFormat(exchangeRateRequestDto.targetCurrency());
        validateCodePair(exchangeRateRequestDto.baseCurrency(), exchangeRateRequestDto.targetCurrency());
        validateRate(exchangeRateRequestDto.rate());
    }

    public static void validateExchangeDto(ExchangeRequestDto exchangeRequestDto) {
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
