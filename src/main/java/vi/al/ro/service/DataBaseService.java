package vi.al.ro.service;

import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.codegen.GenerationTool;
import org.jooq.impl.DSL;
import vi.al.ro.mapper.KeyStoreMapper;
import vi.al.ro.model.KeyStoreEntity;
import vi.al.ro.model.db.tables.records.KeyStoreRecord;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import static vi.al.ro.model.db.Tables.KEY_STORE;

@Log4j2
public class DataBaseService {

//    private static final String DB_URL = "jdbc:h2:mem:crypto"; // In memory db
    private static final String DB_URL = "jdbc:h2:./crypto;CIPHER=AES"; // With encryption
//    private static final String DB_URL = "jdbc:h2:./crypto"; // Without encryption

    private static final String PASSWORD = "password user_password";

    private static final String USER = "user";

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS key_store(id BIGINT PRIMARY KEY, alias VARCHAR(255), password VARCHAR(255), path_file VARCHAR(255) UNIQUE);";

    private final Connection connection;

    private final DSLContext context;

    private static final DataBaseService service = new DataBaseService();

    private DataBaseService() {
        try {
            // TODO заменить создание таблиц на liquibase or jooq
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            try (Statement statement = connection.createStatement();) {
                statement.execute(CREATE_TABLE);
            }
//            DSL.using("jdbc:url:something", "username", "password").close();
            context = DSL.using(connection, SQLDialect.H2);

            Path path = Paths.get(Objects.requireNonNull(DataBaseService.class.getClassLoader().getResource("jooq-config.xml")).toURI());
            GenerationTool.generate(Files.readString(path));
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }

    public int save(String alias, String password, String pathToFile) throws SQLException {
        KeyStoreRecord keyStoreRecord = context.newRecord(KEY_STORE);
        long id = new Random().nextLong();
        keyStoreRecord.setId(id);
        keyStoreRecord.setAlias(alias);
        keyStoreRecord.setPassword(password);
        keyStoreRecord.setPathFile(pathToFile);
        return keyStoreRecord.store();
    }

    public List<KeyStoreEntity> getAll() throws SQLException {
        Result<KeyStoreRecord> keyStoreRecords = context.selectFrom(KEY_STORE).fetch();
        return keyStoreRecords.stream().map(KeyStoreMapper::toEntity).collect(Collectors.toList());
    }

    public static DataBaseService getInstance() {
        return service;
    }

    public static void close() {
        try {
            service.connection.close();
        } catch (SQLException e) {
            log.error("", e);
        }
    }
}
