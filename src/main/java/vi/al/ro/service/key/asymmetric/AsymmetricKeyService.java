package vi.al.ro.service.key.asymmetric;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

public interface AsymmetricKeyService {

    Certificate getCertificate();

    PrivateKey getPrivateKey();

    PublicKey getPublicKey();
}
