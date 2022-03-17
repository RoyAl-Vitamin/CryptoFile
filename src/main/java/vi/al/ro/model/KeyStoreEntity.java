package vi.al.ro.model;

public class KeyStoreEntity {

    private Long id;

    private String pathToKeyStore;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPathToKeyStore() {
        return pathToKeyStore;
    }

    public void setPathToKeyStore(String pathToKeyStore) {
        this.pathToKeyStore = pathToKeyStore;
    }
}
