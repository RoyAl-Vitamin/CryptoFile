package vi.al.ro.service.message_digest;

import lombok.extern.log4j.Log4j2;
import vi.al.ro.service.util.Base64Service;
import vi.al.ro.service.util.BouncyCastleBase64Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Log4j2
public class Md5MessageDigestService implements MessageDigestService {

    private final Base64Service base64Service = new BouncyCastleBase64Service();

    @Override
    public String getMessageDigest(byte[] byteArray) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }

        md.update(byteArray);

        byte[] encodedArray = base64Service.encode(md.digest());
        return new String(encodedArray);
    }
}
