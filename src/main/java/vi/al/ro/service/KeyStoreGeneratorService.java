package vi.al.ro.service;

import java.security.*;

public final class KeyStoreGeneratorService {

    private final PrivateKey privateKey;

    private final PublicKey publicKey;

    public KeyStoreGeneratorService() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();

        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
