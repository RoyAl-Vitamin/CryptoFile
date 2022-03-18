package vi.al.ro.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class KeyStorePkcs12Service {

    Certificate certificate;

    PrivateKey privateKey;

    PublicKey publicKey;

    public KeyStorePkcs12Service(String alias, String password, File keyStoreFile) throws CertificateException, IOException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
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
