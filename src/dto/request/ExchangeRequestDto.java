package dto.request;

import dto.BaseDto;

import java.math.BigDecimal;

public class ExchangeRequestDto extends BaseDto {
    private String baseCurrency;
    private String targetCurrency;
    private BigDecimal amount;

    public ExchangeRequestDto(String baseCurrency, String targetCurrency, BigDecimal amount) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.amount = amount;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
