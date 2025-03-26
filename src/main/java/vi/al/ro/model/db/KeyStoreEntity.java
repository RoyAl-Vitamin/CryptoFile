package vi.al.ro.model.db;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyStoreEntity {

    private Long id;

    private String alias;

    private String password;

    private String pathToFile;
}
