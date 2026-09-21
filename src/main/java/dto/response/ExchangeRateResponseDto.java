package dto.response;

import dto.BaseDto;

import java.math.BigDecimal;

public class ExchangeRateResponseDto extends BaseDto {
    private final int id;
    private CurrencyResponseDto baseCurrency;
    private CurrencyResponseDto targetCurrency;
    private BigDecimal rate;

    public ExchangeRateResponseDto(int id, CurrencyResponseDto baseCurrency, CurrencyResponseDto targetCurrency, BigDecimal rate) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public int getId() {
        return id;
    }
}
