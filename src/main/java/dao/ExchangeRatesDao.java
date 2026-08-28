package dao;

import entity.CurrencyEntity;
import entity.ExchangeRatesEntity;
import exception.DatabaseException;
import exception.SQLExceptionHandler;
import util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRatesDao implements Dao<ExchangeRatesEntity> {
    private static final ExchangeRatesDao INSTANCE = new ExchangeRatesDao();

    private ExchangeRatesDao() {

    }
    public static ExchangeRatesDao getInstance() {
        return INSTANCE;
    }
    private static final String CREATE_SQL = """
            INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate)
            VALUES (?,?,?)
            """;

    private static final String FIND_ALL_SQL = """
            SELECT
                ExchangeRates.ID,
                base.ID AS BaseCurrencyId,
                base.Code AS BaseCurrencyCode,
                base.FullName AS BaseCurrencyName,
                base.Sign AS BaseCurrencySign,
                target.ID AS TargetCurrencyId,
                target.Code AS TargetCurrencyCode,
                target.FullName AS TargetCurrencyName,
                target.Sign AS TargetCurrencySign,
                ExchangeRates.Rate
            FROM ExchangeRates
                     INNER JOIN Currencies AS base ON ExchangeRates.BaseCurrencyId = base.ID
                     INNER JOIN Currencies AS target ON ExchangeRates.TargetCurrencyId = target.ID;
            """;

    private static final String FIND_PAIR_SQL =  """
           SELECT
           ExchangeRates.ID,
                base.ID AS BaseCurrencyId,
                base.Code AS BaseCurrencyCode,
                base.FullName AS BaseCurrencyName,
                base.Sign AS BaseCurrencySign,
                target.ID AS TargetCurrencyId,
                target.Code AS TargetCurrencyCode,
                target.FullName AS TargetCurrencyName,
                target.Sign AS TargetCurrencySign,
                ExchangeRates.Rate
            FROM ExchangeRates
                     INNER JOIN Currencies AS base ON ExchangeRates.BaseCurrencyId = base.ID
                     INNER JOIN Currencies AS target ON ExchangeRates.TargetCurrencyId = target.ID
            WHERE base.Code = ? AND target.Code = ?;
           """;

    private static final String UPDATE_RATE_SQL = """
            UPDATE ExchangeRates SET Rate = ?
            WHERE BaseCurrencyId = (SELECT Currencies.ID FROM Currencies WHERE Code = ?)
            AND TargetCurrencyId = (SELECT Currencies.ID FROM Currencies WHERE Code = ?)
            RETURNING ID
    """;

    @Override
    public Optional<ExchangeRatesEntity> create(ExchangeRatesEntity exchangeRatesEntity) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_SQL)){
        preparedStatement.setInt(1, exchangeRatesEntity.getBaseCurrency().getId());
        preparedStatement.setInt(2, exchangeRatesEntity.getTargetCurrency().getId());
        preparedStatement.setBigDecimal(3, exchangeRatesEntity.getRate());

        preparedStatement.executeUpdate();

        try(ResultSet resultSet = preparedStatement.getGeneratedKeys()){
            if (resultSet.next()) {
                exchangeRatesEntity.setId(resultSet.getInt(1));
            }
            return Optional.of(exchangeRatesEntity);
        }

        } catch (SQLException e) {
            SQLExceptionHandler.exceptionHandler(e);
        }
        return Optional.empty();
    }

    @Override
    public List<ExchangeRatesEntity> findAll() {
        List<ExchangeRatesEntity> exchangeRatesList = new ArrayList<>();
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)){

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()) {
                    exchangeRatesList.add(
                            createExchangeRateEntity(resultSet));
                }
                return exchangeRatesList;
            }

        } catch (SQLException e) {
            SQLExceptionHandler.exceptionHandler(e);
        }
        return exchangeRatesList;
    }

    public Optional<ExchangeRatesEntity> findByCode(String baseCode, String targetCode) {
        try(Connection connection = ConnectionManager.get();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_PAIR_SQL)){

            preparedStatement.setString(1, baseCode);
            preparedStatement.setString(2, targetCode);

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()) {
                    return Optional.of(createExchangeRateEntity(resultSet));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            SQLExceptionHandler.exceptionHandler(e);
        }
        return Optional.empty();
    }

    public Optional<ExchangeRatesEntity> update(ExchangeRatesEntity exchangeRatesEntity) {
        try(Connection connection = ConnectionManager.get();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_RATE_SQL)){

            preparedStatement.setBigDecimal(1, exchangeRatesEntity.getRate());
            preparedStatement.setString(2, exchangeRatesEntity.getBaseCurrency().getCode());
            preparedStatement.setString(3, exchangeRatesEntity.getTargetCurrency().getCode());

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()) {
                    return Optional.of(
                            new ExchangeRatesEntity(
                                    resultSet.getInt("ID"),
                            exchangeRatesEntity.getBaseCurrency(),
                            exchangeRatesEntity.getTargetCurrency(),
                            exchangeRatesEntity.getRate())
                    );
                }
                throw new DatabaseException("Update operation did not execute");
            }
        } catch (SQLException e) {
           SQLExceptionHandler.exceptionHandler(e);
        }
        return Optional.empty();
    }

    private ExchangeRatesEntity createExchangeRateEntity (ResultSet resultSet) throws SQLException {
        return new ExchangeRatesEntity(
                resultSet.getInt("ID"),
                new CurrencyEntity(resultSet.getInt("BaseCurrencyId"),
                        resultSet.getString("BaseCurrencyCode"),
                        resultSet.getString("BaseCurrencyName"),
                        resultSet.getString("BaseCurrencySign")),
                new CurrencyEntity(resultSet.getInt("TargetCurrencyId"),
                        resultSet.getString("TargetCurrencyCode"),
                        resultSet.getString("TargetCurrencyName"),
                        resultSet.getString("TargetCurrencySign")),
                resultSet.getBigDecimal("Rate")
        );
    }
}