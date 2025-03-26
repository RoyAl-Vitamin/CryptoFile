package vi.al.ro.service.util;

public interface Base64Service {

    byte[] decode(byte[] byteArray);

    byte[] encode(byte[] byteArray);
}
