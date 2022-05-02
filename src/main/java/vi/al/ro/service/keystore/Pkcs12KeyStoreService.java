package vi.al.ro.service.keystore;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import vi.al.ro.service.keystore.KeyStoreService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class Pkcs12KeyStoreService implements KeyStoreService {

    Certificate certificate;

    PrivateKey privateKey;

    PublicKey publicKey;

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
