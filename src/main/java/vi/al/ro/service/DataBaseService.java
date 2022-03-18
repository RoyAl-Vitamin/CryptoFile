package vi.al.ro.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vi.al.ro.model.KeyStoreEntity;

import java.sql.*;
import java.util.Random;

public class DataBaseService {

    private static final Logger log = LogManager.getLogger(DataBaseService.class);

//    private static final String DB_URL = "jdbc:h2:mem:crypto";
    private static final String DB_URL = "jdbc:h2:./crypto;CIPHER=AES";

    private static final String PASSWORD = "password user_password";

    private static final String USER = "user";

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS key_store(id BIGINT PRIMARY KEY, alias VARCHAR(255), password VARCHAR(255), path_file VARCHAR(255));";

    private static final String INSERT_INTO = "INSERT INTO key_store(id, alias, password, path_file) VALUES (?, ?, ?, ?)";

    public static void init() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement statement = connection.createStatement();) {
            statement.execute(CREATE_TABLE);
        }
    }

    public static int save(KeyStoreEntity entity) throws SQLException {
        entity.setId(new Random().nextLong());
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(INSERT_INTO);) {
            statement.setLong(1, entity.getId());
            statement.setString(2, entity.getAlias());
            statement.setString(3, entity.getPassword());
            statement.setString(4, entity.getPathToFile());
            return statement.executeUpdate();
        } catch (SQLException e) {
            log.error("", e);
            throw e;
        }
    }
}
