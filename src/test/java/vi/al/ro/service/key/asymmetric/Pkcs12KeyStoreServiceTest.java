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

    @Test
    @DisplayName("Проверка доступности хранилища")
    void test0() {
        // GIVEN
        File pfxFile = new File(System.getProperty("java.io.tmpdir"), "keystore.pfx");
        pfxFile.deleteOnExit();
        AsymmetricKeyService asymmetricKeyService = getInitializedAsymmetricKeyService(pfxFile);

        // WHEN
        try {
            this.readerService = new Pkcs12KeyStoreService(ALIAS, PASSWORD, pfxFile);
        } catch (CertificateException | IOException | NoSuchAlgorithmException | KeyStoreException |
                 UnrecoverableKeyException e) {
            fail(e);
        }

        // THEN
        SoftAssertions.assertSoftly(assertions -> {
            assertions.assertThat(asymmetricKeyService.getCertificate()).isEqualTo(readerService.getCertificate());
            assertions.assertThat(asymmetricKeyService.getPublicKey()).isEqualTo(readerService.getPublicKey());
            assertions.assertThat(asymmetricKeyService.getPrivateKey()).isEqualTo(readerService.getPrivateKey());
            assertions.assertThat(pfxFile.delete()).isTrue();
        });
    }

    private AsymmetricKeyService getInitializedAsymmetricKeyService(File pfxFile) {
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

        try (FileOutputStream fos = new FileOutputStream(pfxFile)) {
            // store the keystore protected with password
            keyStore.store(fos, PASSWORD.toCharArray());
        } catch (IOException | CertificateException | KeyStoreException | NoSuchAlgorithmException e) {
            fail(e);
        }
        return asymmetricKeyService;
    }
}