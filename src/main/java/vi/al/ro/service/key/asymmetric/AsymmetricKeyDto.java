package vi.al.ro.service.key.asymmetric;

import lombok.Getter;
import lombok.Setter;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

@Getter
@Setter
public class AsymmetricKeyDto implements AsymmetricKeyService {

    private Certificate certificate;

    private PrivateKey privateKey;

    private PublicKey publicKey;
}
