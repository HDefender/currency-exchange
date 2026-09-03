package dto.response;

import dto.BaseDto;
import entity.CurrencyEntity;

import java.math.BigDecimal;

public class ExchangeRatesResponseDto extends BaseDto {
    private final int id;
    private CurrencyEntity baseCurrency;
    private CurrencyEntity targetCurrency;
    private BigDecimal rate;

    public ExchangeRatesResponseDto(int id, CurrencyEntity baseCurrency, CurrencyEntity targetCurrency, BigDecimal rate) {
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
