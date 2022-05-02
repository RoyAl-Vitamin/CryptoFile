package vi.al.ro.service.keystore;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

public interface KeyStoreService {

    Certificate getCertificate();

    PrivateKey getPrivateKey();

    PublicKey getPublicKey();
}
