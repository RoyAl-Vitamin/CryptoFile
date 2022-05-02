package vi.al.ro.service.cryptography;

import java.io.File;
import java.io.IOException;

public interface CryptographyService {

    void encryptFile(File inFile, File outFile) throws IOException;

    void decryptFile(File inFile, File outFile) throws IOException;
}
