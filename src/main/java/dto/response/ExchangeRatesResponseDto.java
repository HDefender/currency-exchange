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

    public CurrencyEntity getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(CurrencyEntity baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public CurrencyEntity getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(CurrencyEntity targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public int getId() {
        return id;
    }
}
