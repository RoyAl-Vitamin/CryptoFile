package vi.al.ro.service.keystore;

import lombok.Getter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

@Getter
public final class Pkcs12KeyStoreService implements KeyStoreService {

    private final Certificate certificate;

    private final PrivateKey privateKey;

    private final PublicKey publicKey;

    public Pkcs12KeyStoreService(final String alias, final String password, final File keyStoreFile) throws CertificateException, IOException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        Security.addProvider(new BouncyCastleProvider());
        KeyStore store;
        try (InputStream stream = new FileInputStream(keyStoreFile)) {
            store = KeyStore.getInstance("PKCS12");
            store.load(stream, password.toCharArray());
        }
        this.privateKey = (PrivateKey) store.getKey(alias, password.toCharArray());
        this.certificate = store.getCertificate(alias);
        this.publicKey = this.certificate.getPublicKey();
    }
}
