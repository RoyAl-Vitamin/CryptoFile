package vi.al.ro.service;

import javax.crypto.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Замечание, можно зашифровать только (key_size_in_bits) / 8 - 11 байт
 * так для ключа 2048 bits максимальный размер данных 256 байт (-11 если используется смещение)
 */
public final class CryptographyService {

    public static void encryptFile(File inFile, File outFile, PublicKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        try (OutputStream os = new FileOutputStream(outFile);
             CipherOutputStream cipherOutputStream = new CipherOutputStream(os, cipher);
             InputStream is = new FileInputStream(inFile);
             BufferedInputStream bis = new BufferedInputStream(is)) {
            int i;
            byte[] array = new byte[2048];
            while ((i = bis.read(array)) != -1) {
                cipherOutputStream.write(array, 0, i);
            }
        }
    }

    public static void decryptFile(File inFile, File outFile, PrivateKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        try (InputStream is = new FileInputStream(inFile);
             CipherInputStream cipherInputStream = new CipherInputStream(is, cipher);
             OutputStream os = new FileOutputStream(outFile);
             BufferedOutputStream bos = new BufferedOutputStream(os)) {
            int i;
            byte[] array = new byte[2048];
            while ((i = cipherInputStream.read(array)) != -1) {
                bos.write(array, 0, i);
            }
        }
    }

    public static byte[] encrypt(PublicKey key, byte[] plaintext) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(plaintext);
    }

    public static byte[] decrypt(PrivateKey key, byte[] ciphertext) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(ciphertext);
    }
}
