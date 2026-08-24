package main.dto.request;

import main.dto.BaseDto;

import java.math.BigDecimal;

public class ExchangeRatesRequestDto extends BaseDto {
    private final String baseCurrency;
    private final String targetCurrency;
    private final BigDecimal rate;

    public ExchangeRatesRequestDto(String baseCurrency, String targetCurrency, BigDecimal rate) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }
}
