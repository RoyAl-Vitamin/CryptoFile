package vi.al.ro.model.fx;

import javafx.beans.property.SimpleStringProperty;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import vi.al.ro.model.KeyStoreEntity;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class KeyStoreModel {

    SimpleStringProperty name;

    SimpleStringProperty needPassword;

    public KeyStoreModel(KeyStoreEntity keyStoreEntity) {
        this.name = new SimpleStringProperty(keyStoreEntity.getPathToFile());
        if (keyStoreEntity.getPassword() == null || keyStoreEntity.getPassword().isBlank()) {
            this.needPassword = new SimpleStringProperty("No");
        } else {
            this.needPassword = new SimpleStringProperty("Yes");
        }
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public String getNeedPassword() {
        return needPassword.get();
    }

    public void setNeedPassword(String needPassword) {
        this.needPassword = new SimpleStringProperty(needPassword);
    }
}
