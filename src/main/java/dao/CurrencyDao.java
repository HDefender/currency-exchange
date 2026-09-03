package dao;

import entity.CurrencyEntity;

import java.util.Optional;

public interface CurrencyDao extends Dao<CurrencyEntity> {
    Optional<CurrencyEntity> findByCode(String code);
}
