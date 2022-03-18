package vi.al.ro.mapper;

import vi.al.ro.model.KeyStoreEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KeyStoreMapper {

    public static List<KeyStoreEntity> toEntity(ResultSet rs) throws SQLException {
        List<KeyStoreEntity> list = new ArrayList<>(rs.getFetchSize());

        while (rs.next()) {
            KeyStoreEntity entity = new KeyStoreEntity();
            entity.setId(rs.getLong("id"));
            entity.setAlias(rs.getString("alias"));
            entity.setPassword(rs.getString("password"));
            entity.setPathToFile(rs.getString("path_file"));
            list.add(entity);
        }

        return list;
    }
}
