package vi.al.ro.service.cryptography;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import vi.al.ro.service.key.symmetric.DesKeyGeneratorService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

@Log4j2
public class DesEcbPkcs5PaddingCryptographyServiceTests {

    private final CryptographyService desEcbPkcs5PaddingCryptographyService = new DesEcbPkcs5PaddingCryptographyService(new DesKeyGeneratorService());

    private static final String DATA = "Hello world!";

    @SneakyThrows(IOException.class)
    @Test
    @DisplayName("Проверка шифрования и деширования DES/ECB/PKCS5Padding")
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
        desEcbPkcs5PaddingCryptographyService.encryptFile(inFile, outFile);
        // 2. Отчищаем данные из inFile
        Files.newBufferedWriter(pathToTempInFile, StandardOpenOption.TRUNCATE_EXISTING).close();
        // 3. Зашифровывыем данные из outFile в inFile
        desEcbPkcs5PaddingCryptographyService.decryptFile(outFile, inFile);

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
