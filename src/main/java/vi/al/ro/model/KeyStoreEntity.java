package vi.al.ro.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KeyStoreEntity {

    Long id;

    String alias;

    String password;

    String pathToFile;
}
