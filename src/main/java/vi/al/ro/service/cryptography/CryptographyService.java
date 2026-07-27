package vi.al.ro.service.cryptography;

import java.io.File;
import java.io.IOException;

public interface CryptographyService {

    Void encryptFile(File inFile, File outFile) throws IOException;

    String encryptString(String src);

    Void decryptFile(File inFile, File outFile) throws IOException;

    String decryptString(String src);
}
