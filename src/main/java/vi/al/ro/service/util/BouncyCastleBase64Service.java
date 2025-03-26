package vi.al.ro.service.util;

import lombok.extern.log4j.Log4j2;
import org.bouncycastle.util.encoders.Base64;

@Log4j2
public class BouncyCastleBase64Service implements Base64Service {

    @Override
    public byte[] decode(byte[] byteArray) {
        return Base64.decode(byteArray);
    }

    @Override
    public byte[] encode(byte[] byteArray) {
        return Base64.encode(byteArray);
    }
}
