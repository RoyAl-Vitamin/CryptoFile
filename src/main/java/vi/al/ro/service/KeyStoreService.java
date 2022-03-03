package vi.al.ro.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public final class KeyStoreService {

    public static PublicKey readPublicKey(File publicKeyFile) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] file = Files.readAllBytes(publicKeyFile.toPath());
        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(file);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(publicSpec);
    }

    public static PrivateKey readPrivateKey(File privateKeyFile) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] file = Files.readAllBytes(privateKeyFile.toPath());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(file);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }
}
