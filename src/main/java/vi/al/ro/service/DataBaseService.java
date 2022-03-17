package vi.al.ro.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vi.al.ro.model.KeyStoreEntity;

import java.sql.*;

public class DataBaseService {

    private static final Logger log = LogManager.getLogger(DataBaseService.class);

//    private static final String DB_URL = "jdbc:h2:mem:crypto";
    private static final String DB_URL = "jdbc:h2:~/crypto";

    private static final String PASSWORD = "password";

    private static final String USER = "user";

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS key_store(id BIGINT PRIMARY KEY, path_to_key_store VARCHAR(255));";

    private static final String INSERT_INTO = "INSERT INTO key_store(id, path_to_key_store) VALUES (?, ?)";

    public static void init() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement statement = connection.createStatement();) {
            statement.execute(CREATE_TABLE);
        }
    }

    public int save(KeyStoreEntity entity) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(INSERT_INTO);) {
            statement.setLong(1, entity.getId());
            statement.setString(2, entity.getPathToKeyStore());
            return statement.executeUpdate();
        } catch (SQLException e) {
            log.error("", e);
            throw e;
        }
    }
}