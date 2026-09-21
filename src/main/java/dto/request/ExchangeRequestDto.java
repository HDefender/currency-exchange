package dto.request;

import java.math.BigDecimal;

public record ExchangeRequestDto (String baseCurrency, String targetCurrency, BigDecimal amount) {

}
