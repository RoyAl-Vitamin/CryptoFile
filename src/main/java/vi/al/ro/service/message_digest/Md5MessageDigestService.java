package vi.al.ro.service.message_digest;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Log4j2
public class Md5MessageDigestService implements MessageDigestService {

    @Override
    public String getMessageDigest(@NonNull File file) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }

        byte[] byteArray = null;
        try {
            byteArray = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }

        md.update(byteArray);

        byte[] encodedArray = org.bouncycastle.util.encoders.Base64.encode(md.digest());
        return new String(encodedArray);
    }
}
