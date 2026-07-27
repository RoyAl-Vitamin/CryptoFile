package vi.al.ro.service.cryptography;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.SoftAssertions;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import vi.al.ro.service.key.asymmetric.RsaKeyGeneratorService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.util.Objects;

@Log4j2
public class RsaCryptographyServiceTests {

    private final CryptographyService rsaCryptographyService;

    public RsaCryptographyServiceTests() {
        try {
            this.rsaCryptographyService = new RsaCryptographyService(new RsaKeyGeneratorService());
        } catch (CertificateException | NoSuchAlgorithmException | NoSuchProviderException | OperatorCreationException |
                 CertIOException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }

    private static final String DATA = "Hello world!";

    @BeforeAll
    static void initResource() {
        if (Objects.isNull(Security.getProvider("BC"))) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    @SneakyThrows(IOException.class)
    @Test
    @DisplayName("Проверка шифрования и деширования RSA")
    void test0() {
        // GIVEN
        var pathToTempInFile = Files.createTempFile("DES_ECB_PKCS5Padding_IN_", ".txt");
        var inFile = pathToTempInFile.toFile();
        inFile.deleteOnExit();
        var pathToTempOutFile = Files.createTempFile("DES_ECB_PKCS5Padding_OUT_", ".txt");
        var outFile = pathToTempOutFile.toFile();
        outFile.deleteOnExit();
        Files.writeString(pathToTempInFile, DATA);

        // WHEN
        // 1. Зашифровывыем данные из inFile в outFile
        rsaCryptographyService.encryptFile(inFile, outFile);
        // 2. Отчищаем данные из inFile
        Files.newBufferedWriter(pathToTempInFile, StandardOpenOption.TRUNCATE_EXISTING).close();
        // 3. Зашифровывыем данные из outFile в inFile
        rsaCryptographyService.decryptFile(outFile, inFile);

        final var lines = Files.readAllLines(pathToTempInFile);
        // THEN
        SoftAssertions.assertSoftly(assertions -> {
            assertions.assertThat(lines).hasSize(1);
            assertions.assertThat(lines.getFirst()).isEqualTo(DATA);
            assertions.assertThat(inFile.delete()).isTrue();
            assertions.assertThat(outFile.delete()).isTrue();
        });
    }
}
