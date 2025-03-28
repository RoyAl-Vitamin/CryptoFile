package vi.al.ro.service.key.symmetric;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Log4j2
@Getter
public final class DesKeyGeneratorService implements SymmetricKeyService {

    private final Key key;

    public DesKeyGeneratorService() {
        KeyGenerator generator = null;
        try {
            generator = KeyGenerator.getInstance("DES");
        } catch (NoSuchAlgorithmException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
        generator.init(new SecureRandom());
        this.key = generator.generateKey();
    }
}
