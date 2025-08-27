package vi.al.ro.service.key.asymmetric;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.SoftAssertions;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

@Log4j2
public class RsaKeyGeneratorServiceTests {

    private RsaKeyGeneratorService rsaKeyGeneratorService;

    @BeforeAll
    static void initResource() {
        var provider = Security.getProvider("BC");
        if (Objects.isNull(provider)) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    @BeforeEach
    void initParam() {
        try {
            rsaKeyGeneratorService = new RsaKeyGeneratorService();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | CertIOException | CertificateException |
                 OperatorCreationException e) {
            fail(e);
        }
    }

    @SneakyThrows
    @Test
    @DisplayName("Проверка генератора RSA ключей")
    void rsaKeyGeneratorMatches() {
        // GIVEN
        var publicKey = rsaKeyGeneratorService.getPublicKey();
        var privateKey = rsaKeyGeneratorService.getPrivateKey();
        var certificate = rsaKeyGeneratorService.getCertificate();
        final var value = "THIS IS A DATA".getBytes(StandardCharsets.UTF_8);

        // WHEN
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initSign(privateKey);
        sig.update(value);
        byte[] signature = sig.sign();
        sig.initVerify(publicKey);
        sig.update(value);
        final var keyPairMatches = sig.verify(signature);

        // THEN
        SoftAssertions.assertSoftly(assertions -> {
            assertions.assertThat(certificate.getPublicKey()).isEqualTo(publicKey);
            assertions.assertThat(publicKey.getAlgorithm()).isEqualTo(privateKey.getAlgorithm());
            assertions.assertThat(keyPairMatches).isTrue();
        });
    }
}
