package vi.al.ro.service.cryptography;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import vi.al.ro.service.key.asymmetric.AsymmetricKeyService;

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
@Log4j2
@RequiredArgsConstructor
public final class JCECryptographyService implements CryptographyService {

    private final AsymmetricKeyService asymmetricKeyStoreService;

    @Override
    public Void encryptFile(File inFile, File outFile) throws IOException {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, asymmetricKeyStoreService.getPublicKey());
        } catch (InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            log.error(e);
            throw new IOException(e);
        }
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
        return null;
    }

    @Override
    public Void decryptFile(File inFile, File outFile) throws IOException {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, asymmetricKeyStoreService.getPrivateKey());
        } catch (InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            log.error(e);
            throw new IOException(e);
        }
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
        return null;
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
