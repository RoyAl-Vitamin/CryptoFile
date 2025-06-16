package vi.al.ro.service.cryptography;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
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
public class DesEcbPkcs5PaddingCryptographyTaskService implements CryptographyAndCheckStatusService {

    private final Base64Service base64Service;

    private final SymmetricKeyService symmetricKeyService;

    @Getter
    private final ThreadLocal<StringProperty> message;

    public DesEcbPkcs5PaddingCryptographyTaskService(final SymmetricKeyService symmetricKeyService) {
        this.base64Service = new BouncyCastleBase64Service();
        this.symmetricKeyService = symmetricKeyService;
        this.message = ThreadLocal.withInitial(() -> new SimpleStringProperty(this, ""));
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
        updateMessage("Шифр для дешифровки инициализирован");
        byte[] byteArray = Files.readAllBytes(inFile.toPath()); // Файл, который нужно зашифровать, в виде массива байтов
        updateMessage("Исходный файл загружен");
        byte[] raw = null;
        try {
            raw = cipher.doFinal(byteArray); // Набор зашифрованных байтов
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
        updateMessage("Исходный файл зашифрован");
        byte[] encodedByteArray = base64Service.encode(raw); // Зашифрованные байты, но уже в виде Base64
        updateMessage("Зашифрованный файл переведён в Base64");
        Files.write(outFile.toPath(), encodedByteArray);
        updateMessage("Зашифрованный файл записан");
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
        updateMessage("Шифр для дешифровки инициализирован");
        byte[] byteArray = Files.readAllBytes(inFile.toPath()); // Файл, который содержит зашифрованные байты информации в Base64
        updateMessage("Исходный файл загружен");
        byte[] decodedByteArray = base64Service.decode(byteArray); // Декодированный массив байтов из Base64
        updateMessage("Исходный файл декодирован из Base64");
        byte[] raw = null;
        try {
            raw = cipher.doFinal(decodedByteArray); // Расшифрованные данные в виде массива байтов
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
        updateMessage("Исходный файл дешифрован");
        Files.write(outFile.toPath(), raw);
        updateMessage("Дешифрованный файл записан");
        log.info("Decrypted file store in {} file size = {} bytes", outFile.getAbsolutePath(), outFile.length());
        return null;
    }

    private void updateMessage(String value) {
        message.get().set(value);
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
