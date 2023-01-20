package vi.al.ro.service;

import lombok.extern.log4j.Log4j2;
import org.jooq.CloseableDSLContext;
import org.jooq.Result;
import org.jooq.impl.DSL;
import vi.al.ro.mapper.KeyStoreMapper;
import vi.al.ro.model.KeyStoreEntity;
import vi.al.ro.model.db.tables.records.KeyStoreRecord;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static vi.al.ro.model.db.Tables.KEY_STORE;

@Log4j2
public class DataBaseService {

//    private static final String DB_URL = "jdbc:h2:mem:crypto"; // In memory db
//    private static final String DB_URL = "jdbc:h2:./crypto"; // Without encryption
    private static final String DB_URL = "jdbc:h2:./crypto;CIPHER=AES"; // With encryption

    private static final String PASSWORD = "password user_password";

    private static final String USER = "user";

    private final CloseableDSLContext context;

    private static final DataBaseService service = new DataBaseService();

    private DataBaseService() {
        try {
            Class.forName("org.h2.Driver");
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException(e);
        }

        context = DSL.using(DB_URL, USER, PASSWORD);
        context.createTableIfNotExists("key_store")
                .column("id", BIGINT)
                .column("alias", VARCHAR(255))
                .column("password", VARCHAR(255))
                .column("path_file", VARCHAR(255))
                .primaryKey("id")
                .unique("path_file")
                .execute();
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
        service.context.close();
    }
}
