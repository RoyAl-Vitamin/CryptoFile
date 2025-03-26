package vi.al.ro.model.fx;

import javafx.beans.property.SimpleStringProperty;
import vi.al.ro.model.db.KeyStoreEntity;

public class KeyStoreModel {

    private SimpleStringProperty name;

    private SimpleStringProperty needPassword;

    public KeyStoreModel(final KeyStoreEntity keyStoreEntity) {
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
