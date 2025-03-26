package vi.al.ro.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import vi.al.ro.service.keystore.Pkcs12KeyStoreService;

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

    private final Pkcs12KeyStoreService service;

    private static final String PATH_TO_KEYSTORE = "C:\\Users\\rogozhnikov.aleksei.OTR\\IdeaProjects\\CryptoFile\\keystore.pfx";

    public Pkcs12KeyStoreServiceTest() {
        try {
            this.service = new Pkcs12KeyStoreService(ALIAS, PASSWORD, new File(PATH_TO_KEYSTORE));
        } catch (CertificateException | IOException | NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException e) {
            log.error("", e);
            fail(e);
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Проверка доступности хранилища")
    void test0() {
        if (service.getCertificate() == null) {
            fail("Не удалось получить сертификат");
        }

        if (service.getPublicKey() == null) {
            fail("Не удалось получить публичный ключ");
        }

        if (service.getPrivateKey() == null) {
            fail("Не удалось получить приватный ключ");
        }
    }
}