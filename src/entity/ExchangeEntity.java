package entity;

public class ExchangeEntity {
    private CurrencyEntity baseCurrency;
    private CurrencyEntity targetCurrency;
    private double rate;
    private double amount;
    private double convertedAmount;

    public ExchangeEntity(CurrencyEntity baseCurrency, CurrencyEntity targetCurrency, double rate, double amount, double convertedAmount) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }
}
