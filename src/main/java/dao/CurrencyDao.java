package dao;

import entity.CurrencyEntity;
import exception.DatabaseException;
import exception.SQLExceptionHandler;
import util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDao implements Dao<CurrencyEntity> {
    public static final CurrencyDao INSTANCE = new CurrencyDao();

    private CurrencyDao() {
    }

    public static CurrencyDao getInstance() {
        return INSTANCE;
    }

    private static final String CREATE_SQL = """
            INSERT INTO Currencies (Code, FullName, Sign)
            VALUES (?,?,?)
            """;

    public static final String FIND_ALL = """
            SELECT ID, Code, FullName, Sign
            FROM Currencies
            """;

    public static final String FIND_BY_CODE = FIND_ALL + """
            WHERE Code = ?
            """;


    @Override
    public Optional<CurrencyEntity> create(CurrencyEntity currencyEntity) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, currencyEntity.getCode());
            preparedStatement.setString(2, currencyEntity.getFullName());
            preparedStatement.setString(3, currencyEntity.getSign());

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    currencyEntity.setId(generatedKeys.getInt(1));
                    return Optional.of(currencyEntity);
                }
                throw new DatabaseException("Failed to create currency");
            }

        } catch (SQLException e) {
            SQLExceptionHandler.exceptionHandler(e);
        }
        return Optional.empty();
    }

    public Optional<CurrencyEntity> findByCode(String code) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CODE)) {
            preparedStatement.setString(1, code);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                CurrencyEntity currencyEntity = null;

                if (rs.next()) {
                    currencyEntity = buildCurrencyEntity(rs);
                }
                return Optional.ofNullable(currencyEntity);
            }

        } catch (SQLException e) {
            SQLExceptionHandler.exceptionHandler(e);
        }
        return Optional.empty();
    }

    @Override
    public List<CurrencyEntity> findAll() {
        List<CurrencyEntity> currencyEntities = new ArrayList<>();
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                currencyEntities.add(buildCurrencyEntity(resultSet));
            }
            return currencyEntities;
        } catch (SQLException e) {
            SQLExceptionHandler.exceptionHandler(e);
        }
        return currencyEntities;
    }

    private CurrencyEntity buildCurrencyEntity(ResultSet resultSet) throws SQLException {
        return new CurrencyEntity(
                resultSet.getInt("id"),
                resultSet.getString("code"),
                resultSet.getString("fullName"),
                resultSet.getString("sign"));
    }
}