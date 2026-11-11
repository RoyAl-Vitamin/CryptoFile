package vi.al.ro.service.key.asymmetric;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Optional;

@Log4j2
@Getter
public class Pkcs11KeyStoreService implements AsymmetricKeyService {

    private final Certificate certificate;

    private final PrivateKey privateKey;

    private final PublicKey publicKey;

    public Pkcs11KeyStoreService(final String alias, final String pin) throws CertificateException, IOException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        final var path = Paths.get("src", "main", "resources", "pkcs11.cfg");
        if (!Files.exists(path)) {
            throw new FileNotFoundException("Не удалось найти файл по адресу " + path.toAbsolutePath().toString());
        }
        final var provider = Security.getProvider("SunPKCS11").configure(path.toAbsolutePath().toString());
        Security.addProvider(provider);

        final var store = KeyStore.getInstance("PKCS11", provider);
        store.load(null, pin.toCharArray());
        var aliases = store.aliases();
        while (aliases.hasMoreElements()) {
            log.info("ALIAS = {}", aliases.nextElement());
        }

        this.privateKey = (PrivateKey) store.getKey(alias, null);
        this.certificate = store.getCertificate(alias);
        this.publicKey = Optional.ofNullable(this.certificate).map(Certificate::getPublicKey).orElse(null);
    }
}
