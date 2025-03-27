package vi.al.ro.service.key.symmetric;

import lombok.Getter;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Getter
public final class DesKeyGeneratorService implements SymmetricKeyService {

    private final Key key;

    public DesKeyGeneratorService() throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance("DES");
        generator.init(new SecureRandom());
        this.key = generator.generateKey();
    }
}
