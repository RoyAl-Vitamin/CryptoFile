package vi.al.ro.service.util;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
public class BouncyCastleBase64ServiceTests {

    private final Base64Service base64Service = new BouncyCastleBase64Service();

    private static final String SUBJECT_PRINCIPAL_NAME = "2.5.4.12=#130f2b3432203338203737343720333631,1.2.840.113549.1.9.1=#1610646f7374616c656b407076742e6e6574,CN=Libor Dostalek,OU=VCU,O=PVT\\,a.s.,L=Ceske Budejovice,ST=2,C=CZ";

    private static final String ISSUER_PRINCIPAL_NAME = "1.2.840.113549.1.9.1=#161963612d6f706572407037307830332e62726e2e7076742e637a,CN=CA-PVT1,O=PVT a.s.,C=CZ";

    private static final String DER_CERT = """
            -----BEGIN CERTIFICATE-----
            MIICMTCCAZoCAS0wDQYJKoZIhvcNAQEEBQAwXDELMAkGA1UEBhMCQ1oxETAPBgNV
            BAoTCFBWVCBhLnMuMRAwDgYDVQQDEwdDQS1QVlQxMSgwJgYJKoZIhvcNAQkBFhlj
            YS1vcGVyQHA3MHgwMy5icm4ucHZ0LmN6MB4XDTk3MDgwNDA1MDQ1NloXDTk4MDIw
            MzA1MDQ1NlowgakxCzAJBgNVBAYTAkNaMQowCAYDVQQIEwEyMRkwFwYDVQQHExBD
            ZXNrZSBCdWRlam92aWNlMREwDwYDVQQKEwhQVlQsYS5zLjEMMAoGA1UECxMDVkNV
            MRcwFQYDVQQDEw5MaWJvciBEb3N0YWxlazEfMB0GCSqGSIb3DQEJARYQZG9zdGFs
            ZWtAcHZ0Lm5ldDEYMBYGA1UEDBMPKzQyIDM4IDc3NDcgMzYxMFwwDQYJKoZIhvcN
            AQEBBQADSwAwSAJBAORQnnnaTGhwrWBGK+qdvIGiBGyaPNZfnqXlbtXuSUqRHXhE
            acIYDtMVfK4wdROe6lmdlr3DuMc747/oT7SjO2UCAwEAATANBgkqhkiG9w0BAQQF
            AAOBgQBxfebIQCCxnVtyY/YVfsAct1dbmxrBkeb9Z+xN7i/Fc3XYLig8rag3cfWg
            wDqbnt8LKzvFt+FzlrO1qIm7miYlWNq26rlY3KGpWPNoWGJTkyrqX80/WAhU5B9l
            QOqgL9zXHhE65Qq0Wu/3ryRgyBgebSiFem10RZVavBHjgVcejw==
            -----END CERTIFICATE-----
            """;

    @SneakyThrows({CertificateException.class, IOException.class})
    @Test
    @DisplayName("Чтение сертификата в формате DER. Декодирование из Base64 и обратно")
    void decode() {
        // GIVEN
        var s = DER_CERT.substring("-----BEGIN CERTIFICATE-----".length(), DER_CERT.lastIndexOf("-----END CERTIFICATE-----"));
        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        // WHEN
        var is = new ByteArrayInputStream(base64Service.decode(s.getBytes(StandardCharsets.UTF_8)));
        final var cert = (X509Certificate) cf.generateCertificate(is);
        is.close();

        // THEN
        SoftAssertions.assertSoftly(assertions -> {
            var notBefore = Date.from(ZonedDateTime.of(1997, 8, 4, 11, 4, 56, 0, ZoneId.ofOffset("UTC", ZoneOffset.of("+06:00"))).toInstant());
            var notAfter = Date.from(ZonedDateTime.of(1998, 2, 3, 10, 4, 56, 0, ZoneId.ofOffset("UTC", ZoneOffset.of("+05:00"))).toInstant());
            assertions.assertThat(cert.getNotBefore()).isEqualTo(notBefore);
            assertions.assertThat(cert.getNotAfter()).isEqualTo(notAfter);
            assertions.assertThat(cert.getSubjectX500Principal().getName()).isEqualTo(SUBJECT_PRINCIPAL_NAME);
            assertions.assertThat(cert.getIssuerX500Principal().getName()).isEqualTo(ISSUER_PRINCIPAL_NAME);
            assertions.assertThat(cert.getSerialNumber().toString(16)).isEqualTo("2d");
        });
    }
}
