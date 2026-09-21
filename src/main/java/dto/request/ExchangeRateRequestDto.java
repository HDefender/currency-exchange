package dto.request;

import java.math.BigDecimal;

public record ExchangeRateRequestDto(String baseCurrency, String targetCurrency, BigDecimal rate){

}
