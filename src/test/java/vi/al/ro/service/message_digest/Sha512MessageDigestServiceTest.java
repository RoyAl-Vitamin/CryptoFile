package vi.al.ro.service.message_digest;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Log4j2
public class Sha512MessageDigestServiceTest {

    private final MessageDigestService messageDigestService = new Sha512MessageDigestService();

    @Test
    @DisplayName("Проверка алгоритма хеширования Sha512")
    void test0() {
        String test = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has " +
                "been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley " +
                "of type and scrambled it to make a type specimen book. It has survived not only five centuries, but " +
                "also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in " +
                "the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently " +
                "with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";
        String md5 = messageDigestService.getMessageDigest(test.getBytes(StandardCharsets.UTF_8));
        assertEquals("H9sWxa4dSHGIq/NqHrAFHdFrpFPZeJPDpsZCgJrElVQucX8zVFsd9AgbbFO91JVbATitFmABb0ULly1mHfRPQg==", md5);
    }
}
