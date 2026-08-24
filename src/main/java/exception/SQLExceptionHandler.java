package exception;

import java.sql.SQLException;

public class SQLExceptionHandler {

    public static void exceptionHandler (SQLException e){
        String message = e.getMessage();
        if (message.contains("[SQLITE_CONSTRAINT_UNIQUE] A UNIQUE constraint failed")) {
            throw new AlreadyExistException("This object is already exist");
        }
        if(message.contains("NULL constraint")) {
            throw new DataNotFoundException("Some parameter is null or parameters are null");
        }

        throw new DatabaseException("Database error");

    }
}
