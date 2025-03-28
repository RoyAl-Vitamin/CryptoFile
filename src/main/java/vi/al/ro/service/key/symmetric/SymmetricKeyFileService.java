package vi.al.ro.service.key.symmetric;

import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.security.Key;

@Log4j2
public final class SymmetricKeyFileService {

    public static Key readKey(File file) {
        try (InputStream is = new FileInputStream(file);
             ObjectInputStream oin = new ObjectInputStream(is);) {
            return (Key) oin.readObject();
        } catch (ClassNotFoundException | IOException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }

    public static void writeFile(Key key, File file) {
        try (FileOutputStream fis = new FileOutputStream(file);
             ObjectOutputStream out = new ObjectOutputStream(fis);) {
            out.writeObject(key);
        } catch (IOException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }
}
