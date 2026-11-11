package vi.al.ro.service.key.asymmetric;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import static org.junit.jupiter.api.Assertions.fail;

public class Pkcs11KeyStoreServiceTests {

    private AsymmetricKeyService readerService;

    @Test
    @DisplayName("Проверка доступности хранилища, получения по alias Ec256bits-ключа")
    void readEcKey_successfully() {
        // GIVEN
        final String alias = "hsm-master-key";
        final String password = "648219";

        // WHEN
        try {
            this.readerService = new Pkcs11KeyStoreService(alias, password);
        } catch (CertificateException | IOException | NoSuchAlgorithmException | KeyStoreException |
                 UnrecoverableKeyException e) {
            fail(e);
        }

        // THEN
        SoftAssertions.assertSoftly(assertions -> {
            assertions.assertThat(readerService.getCertificate()).isNotNull();
            assertions.assertThat(readerService.getPublicKey()).isNotNull();
            assertions.assertThat(readerService.getPrivateKey()).isNotNull();
        });
    }

    @Test
    @DisplayName("Проверка доступности хранилища, получения по alias Rsa2048-ключа")
    void readRsa2048Key_successfully() {
        // GIVEN
        final String alias = "hsm-rsa-key";
        final String password = "648219";

        // WHEN
        try {
            this.readerService = new Pkcs11KeyStoreService(alias, password);
        } catch (CertificateException | IOException | NoSuchAlgorithmException | KeyStoreException |
                 UnrecoverableKeyException e) {
            fail(e);
        }

        // THEN
        SoftAssertions.assertSoftly(assertions -> {
            assertions.assertThat(readerService.getCertificate()).isNotNull();
            assertions.assertThat(readerService.getPublicKey()).isNotNull();
            assertions.assertThat(readerService.getPrivateKey()).isNotNull();
        });
    }
}
