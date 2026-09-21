package dao;

import entity.ExchangeRateEntity;

import java.util.Optional;

public interface ExchangeRatesDao extends Dao<ExchangeRateEntity> {
    Optional<ExchangeRateEntity> findByCode(String baseCode, String targetCode);
    Optional<ExchangeRateEntity> update (ExchangeRateEntity exchangeRateEntity);
}
