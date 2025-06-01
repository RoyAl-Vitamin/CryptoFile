package vi.al.ro.service.db;

import lombok.extern.log4j.Log4j2;
import org.jooq.CloseableDSLContext;
import org.jooq.impl.DSL;
import vi.al.ro.model.db.KeyStoreEntity;

import java.sql.SQLException;
import java.util.List;

import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static vi.al.ro.model.db.Sequences.SEQ_KEY_STORE;
import static vi.al.ro.model.db.Tables.KEY_STORE;

@Log4j2
public class DataBaseService {

//    private static final String DB_URL = "jdbc:h2:mem:crypto"; // In memory db
//    private static final String DB_URL = "jdbc:h2:./crypto"; // Without encryption
    private static final String DB_URL = "jdbc:h2:./crypto;CIPHER=AES"; // With encryption

    private static final String PASSWORD = "password user_password";

    private static final String USER = "user";

    private final CloseableDSLContext dslContext;

    private static final DataBaseService service = new DataBaseService();

    private DataBaseService() {
        try {
            Class.forName("org.h2.Driver");
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException(e);
        }

        this.dslContext = DSL.using(DB_URL, USER, PASSWORD);
        this.dslContext
                .createSequenceIfNotExists("seq_key_store")
                .minvalue(1)
                .startWith(1)
                .execute();
        this.dslContext
                .createTableIfNotExists("key_store")
                .column("id", BIGINT.notNull().defaultValue(SEQ_KEY_STORE.nextval()))
                .column("alias", VARCHAR(255))
                .column("password", VARCHAR(255))
                .column("path_file", VARCHAR(255))
                .primaryKey("id")
                .unique("path_file")
                .execute();
    }

    public int insert(String alias, String password, String pathToFile) throws SQLException {
        return dslContext
                .insertInto(KEY_STORE)
                .set(KEY_STORE.ID, SEQ_KEY_STORE.nextval())
                .set(KEY_STORE.ALIAS, alias)
                .set(KEY_STORE.PASSWORD, password)
                .set(KEY_STORE.PATH_FILE, pathToFile)
                .execute();
    }

    public List<KeyStoreEntity> getAll() throws SQLException {
        return dslContext
                .selectFrom(KEY_STORE)
                .fetch(record -> {
                    KeyStoreEntity entity = new KeyStoreEntity();
                    entity.setId(record.get(KEY_STORE.ID));
                    entity.setAlias(record.get(KEY_STORE.ALIAS));
                    entity.setPassword(record.get(KEY_STORE.PASSWORD));
                    entity.setPathToFile(record.get(KEY_STORE.PATH_FILE));
                    return entity;
                });
    }

    public static DataBaseService getInstance() {
        return service;
    }

    public static void close() {
        service.dslContext.close();
        log.debug("Database connection close");
    }
}
