package vi.al.ro.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

class KeyStorePkcs12ServiceTest {

    private static final Logger log = LogManager.getLogger(KeyStorePkcs12ServiceTest.class);

    private final KeyStorePkcs12Service service;

    private static final String PATH_TO_KEYSTORE = "";

    public KeyStorePkcs12ServiceTest() {
        try {
            this.service = new KeyStorePkcs12Service(ALIAS, PASSWORD, new File(PATH_TO_KEYSTORE));
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