package vi.al.ro.service.key.asymmetric;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import static org.junit.jupiter.api.Assertions.fail;
import static vi.al.ro.constants.KeyStoreData.ALIAS;
import static vi.al.ro.constants.KeyStoreData.PASSWORD;

@Log4j2
class Pkcs12KeyStoreServiceTest {

    private final AsymmetricKeyService readerService;

    private static final String PATH_TO_KEYSTORE = "C:\\Users\\rogozhnikov.aleksei.OTR\\IdeaProjects\\CryptoFile\\keystore.pfx";

    public Pkcs12KeyStoreServiceTest() {
        try {
            this.readerService = new Pkcs12KeyStoreService(ALIAS, PASSWORD, new File(PATH_TO_KEYSTORE));
        } catch (CertificateException | IOException | NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException e) {
            log.error("", e);
            fail(e);
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Проверка доступности хранилища")
    void test0() {
        if (readerService.getCertificate() == null) {
            fail("Не удалось получить сертификат");
        }

        if (readerService.getPublicKey() == null) {
            fail("Не удалось получить публичный ключ");
        }

        if (readerService.getPrivateKey() == null) {
            fail("Не удалось получить приватный ключ");
        }
    }
}