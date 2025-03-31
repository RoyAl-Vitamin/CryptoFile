package vi.al.ro.service.cryptography;

import lombok.extern.log4j.Log4j2;
import vi.al.ro.service.key.symmetric.SymmetricKeyService;
import vi.al.ro.service.util.Base64Service;
import vi.al.ro.service.util.BouncyCastleBase64Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

@Log4j2
public class DesEcbPkcs5PaddingCryptographyService implements CryptographyService {

    private final Base64Service base64Service;

    private final SymmetricKeyService symmetricKeyService;

    public DesEcbPkcs5PaddingCryptographyService(final SymmetricKeyService symmetricKeyService) {
        this.base64Service = new BouncyCastleBase64Service();
        this.symmetricKeyService = symmetricKeyService;
    }

    @Override
    public Void encryptFile(File inFile, File outFile) throws IOException {
        Cipher cipher = getCipher();
        Key key = symmetricKeyService.getKey();
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
        } catch (InvalidKeyException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
        byte[] byteArray = Files.readAllBytes(inFile.toPath()); // Файл, который нужно зашифровать, в виде массива байтов
        byte[] raw = null;
        try {
            raw = cipher.doFinal(byteArray); // Набор зашифрованных байтов
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
        byte[] encodedByteArray = base64Service.encode(raw); // Зашифрованные байты, но уже в виде Base64
        Files.write(outFile.toPath(), encodedByteArray);
        log.info("Encrypted file store in {} file size = {} bytes", outFile.getAbsolutePath(), outFile.length());
        return null;
    }

    @Override
    public Void decryptFile(File inFile, File outFile) throws IOException {
        Cipher cipher = getCipher();
        Key key = symmetricKeyService.getKey();
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
        } catch (InvalidKeyException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
        byte[] byteArray = Files.readAllBytes(inFile.toPath()); // Файл, который содержит зашифрованные байты информации в Base64
        byte[] decodedByteArray = base64Service.decode(byteArray); // Декодированный массив байтов из Base64
        byte[] raw = null;
        try {
            raw = cipher.doFinal(decodedByteArray); // Расшифрованные данные в виде массива байтов
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
        Files.write(outFile.toPath(), raw);
        log.info("Decrypted file store in {} file size = {} bytes", outFile.getAbsolutePath(), outFile.length());
        return null;
    }

    private Cipher getCipher() {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
        return cipher;
    }
}
