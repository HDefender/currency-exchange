package dto.response;

import dto.BaseDto;
import entity.CurrencyEntity;

import java.math.BigDecimal;

public class ExchangeResponseDto extends BaseDto {
    private CurrencyResponseDto baseCurrency;
    private CurrencyResponseDto targetCurrency;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal convertedAmount;

    public ExchangeResponseDto(CurrencyResponseDto baseCurrency, CurrencyResponseDto targetCurrency, BigDecimal rate, BigDecimal amount, BigDecimal convertedAmount) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

}
