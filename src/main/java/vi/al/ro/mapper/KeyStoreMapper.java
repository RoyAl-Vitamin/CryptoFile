package vi.al.ro.mapper;

import vi.al.ro.model.KeyStoreEntity;
import vi.al.ro.model.db.tables.records.KeyStoreRecord;

import static vi.al.ro.model.db.Tables.KEY_STORE;

public class KeyStoreMapper {

    public static KeyStoreEntity toEntity(KeyStoreRecord element) {
        KeyStoreEntity entity = new KeyStoreEntity();
        entity.setId(element.get(KEY_STORE.ID));
        entity.setAlias(element.get(KEY_STORE.ALIAS));
        entity.setPassword(element.get(KEY_STORE.PASSWORD));
        entity.setPathToFile(element.get(KEY_STORE.PATH_FILE));
        return entity;
    }
}
