package dao;

import entity.CurrencyEntity;
import entity.ExchangeRateEntity;
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

public class ExchangeRatesDaoImpl implements ExchangeRatesDao {
    private static final ExchangeRatesDaoImpl INSTANCE = new ExchangeRatesDaoImpl();

    private ExchangeRatesDaoImpl() {
    }

    public static ExchangeRatesDaoImpl getInstance() {
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

    private static final String FIND_PAIR_SQL = """
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
    public Optional<ExchangeRateEntity> create(ExchangeRateEntity exchangeRateEntity) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_SQL)) {
            preparedStatement.setInt(1, exchangeRateEntity.getBaseCurrency().getId());
            preparedStatement.setInt(2, exchangeRateEntity.getTargetCurrency().getId());
            preparedStatement.setBigDecimal(3, exchangeRateEntity.getRate());

            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    return Optional.of(new ExchangeRateEntity(
                            resultSet.getInt(1),
                            exchangeRateEntity.getBaseCurrency(),
                            exchangeRateEntity.getTargetCurrency(),
                            exchangeRateEntity.getRate()
                    ));
                }
                return Optional.of(exchangeRateEntity);
            }

        } catch (SQLException e) {
            SQLExceptionHandler.exceptionHandler(e);
        }
        return Optional.empty();
    }

    @Override
    public List<ExchangeRateEntity> findAll() {
        List<ExchangeRateEntity> exchangeRatesList = new ArrayList<>();
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
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

    @Override
    public Optional<ExchangeRateEntity> findByCode(String baseCode, String targetCode) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_PAIR_SQL)) {

            preparedStatement.setString(1, baseCode);
            preparedStatement.setString(2, targetCode);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
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

    @Override
    public Optional<ExchangeRateEntity> update(ExchangeRateEntity exchangeRateEntity) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_RATE_SQL)) {

            preparedStatement.setBigDecimal(1, exchangeRateEntity.getRate());
            preparedStatement.setString(2, exchangeRateEntity.getBaseCurrency().getCode());
            preparedStatement.setString(3, exchangeRateEntity.getTargetCurrency().getCode());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(
                            new ExchangeRateEntity(
                                    resultSet.getInt("ID"),
                                    exchangeRateEntity.getBaseCurrency(),
                                    exchangeRateEntity.getTargetCurrency(),
                                    exchangeRateEntity.getRate())
                    );
                }
                throw new DatabaseException("Update operation did not execute");
            }
        } catch (SQLException e) {
            SQLExceptionHandler.exceptionHandler(e);
        }
        return Optional.empty();
    }

    private ExchangeRateEntity createExchangeRateEntity(ResultSet resultSet) throws SQLException {
        return new ExchangeRateEntity(
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