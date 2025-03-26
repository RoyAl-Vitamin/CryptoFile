package vi.al.ro.service.cryptography;

import lombok.extern.log4j.Log4j2;
import vi.al.ro.service.util.Base64Service;
import vi.al.ro.service.util.BouncyCastleBase64Service;

import javax.crypto.*;
import java.io.*;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Log4j2
public class DesEcbPkcs5PaddingCryptographyService implements CryptographyService {

    private final Base64Service base64Service = new BouncyCastleBase64Service();

    @Override
    public void encryptFile(File inFile, File outFile) throws IOException {
        Cipher cipher = getCipher();
        Key key = getKey();
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
        } catch (IllegalBlockSizeException e) {
            log.error("", e);
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
        byte[] encodedByteArray = base64Service.encode(raw); // Зашифрованные байты, но уже в виде Base64
        Files.write(outFile.toPath(), encodedByteArray);
    }

    @Override
    public void decryptFile(File inFile, File outFile) throws IOException {
        Cipher cipher = getCipher();
        Key key = getKey();
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
        } catch (IllegalBlockSizeException e) {
            log.error("", e);
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
        Files.write(outFile.toPath(), raw);
    }

    private Cipher getCipher() {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            log.error("", e);
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
        return cipher;
    }

    private Key getKey() throws IOException {
        Key key = null;
        try (FileInputStream fis = new FileInputStream("SecretKey.ser");
             ObjectInputStream in = new ObjectInputStream(fis);) {
            try {
                key = (Key) in.readObject();
            } catch (ClassNotFoundException e) {
                log.error("", e);
                throw new RuntimeException(e);
            }
        } catch (FileNotFoundException fnfe) {
            KeyGenerator generator = null;
            try {
                generator = KeyGenerator.getInstance("DES");
            } catch (NoSuchAlgorithmException e) {
                log.error("", e);
                throw new RuntimeException(e);
            }
            generator.init(new SecureRandom());
            key = generator.generateKey();
            try (FileOutputStream fis = new FileOutputStream("SecretKey.ser");
                 ObjectOutputStream out = new ObjectOutputStream(fis);) {
                out.writeObject(key);
            }
        }
        return key;
    }
}
