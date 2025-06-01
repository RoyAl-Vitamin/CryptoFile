package vi.al.ro.service.key.asymmetric;

import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.SoftAssertions;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.operator.OperatorCreationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import static org.junit.jupiter.api.Assertions.fail;

@Log4j2
class Pkcs12KeyStoreServiceTest {

    private AsymmetricKeyService readerService;

    private static final String ALIAS = "alias";

    private static final String PASSWORD = "password";

    private static final String PATH_TO_TEMP_DIR = System.getProperty("java.io.tmpdir");

    @Test
    @DisplayName("Проверка доступности хранилища")
    void test0() {
        AsymmetricKeyService asymmetricKeyService = null;
        try {
            asymmetricKeyService = new RsaKeyGeneratorService();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | CertIOException | CertificateException |
                 OperatorCreationException e) {
            fail(e);
        }
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load( null);
        } catch (KeyStoreException | NoSuchAlgorithmException | IOException | CertificateException e) {
            fail(e);
        }
        try {
            keyStore.setKeyEntry(ALIAS, asymmetricKeyService.getPrivateKey(), PASSWORD.toCharArray(), new Certificate[]{asymmetricKeyService.getCertificate()});
        } catch (KeyStoreException e) {
            fail(e);
        }
        File pfxFile = new File(PATH_TO_TEMP_DIR, "keystore.pfx");
        pfxFile.deleteOnExit();
        try (FileOutputStream fos = new FileOutputStream(pfxFile)) {
            // store the keystore protected with password
            keyStore.store(fos, PASSWORD.toCharArray());
        } catch (IOException | CertificateException | KeyStoreException | NoSuchAlgorithmException e) {
            fail(e);
        }

        try {
            this.readerService = new Pkcs12KeyStoreService(ALIAS, PASSWORD, pfxFile);
        } catch (CertificateException | IOException | NoSuchAlgorithmException | KeyStoreException |
                 UnrecoverableKeyException e) {
            fail(e);
        }

        AsymmetricKeyService finalAsymmetricKeyService = asymmetricKeyService;
        SoftAssertions.assertSoftly(assertions -> {
            assertions.assertThat(finalAsymmetricKeyService.getCertificate()).isEqualTo(readerService.getCertificate());
            assertions.assertThat(finalAsymmetricKeyService.getPublicKey()).isEqualTo(readerService.getPublicKey());
            assertions.assertThat(finalAsymmetricKeyService.getPrivateKey()).isEqualTo(readerService.getPrivateKey());
            assertions.assertThat(pfxFile.delete()).isTrue();
        });
    }
}