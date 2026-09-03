package dao;

import entity.ExchangeRatesEntity;

import java.util.Optional;

public interface ExchangeRatesDao extends Dao<ExchangeRatesEntity> {
    Optional<ExchangeRatesEntity> findByCode(String baseCode, String targetCode);
    Optional<ExchangeRatesEntity> update (ExchangeRatesEntity exchangeRatesEntity);
}
