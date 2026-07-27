package vi.al.ro.service.cryptography;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JceCMSContentEncryptorBuilder;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientInfoGenerator;
import org.bouncycastle.operator.OutputEncryptor;
import vi.al.ro.service.key.asymmetric.AsymmetricKeyService;
import vi.al.ro.service.util.Base64Service;
import vi.al.ro.service.util.BouncyCastleBase64Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Collection;

@Log4j2
@RequiredArgsConstructor
public final class Aes128CbcCryptographyService implements CryptographyService {

    private final Base64Service base64Service = new BouncyCastleBase64Service();

    private final AsymmetricKeyService asymmetricKeyStoreService;

    @Override
    public Void encryptFile(File inFile, File outFile) throws IOException {
        CMSEnvelopedDataGenerator cmsEnvelopedDataGenerator = new CMSEnvelopedDataGenerator();

        try (InputStream is = new FileInputStream(inFile);
             OutputStream os = new FileOutputStream(outFile);) {
            JceKeyTransRecipientInfoGenerator jceKey = new JceKeyTransRecipientInfoGenerator((X509Certificate) asymmetricKeyStoreService.getCertificate());
            cmsEnvelopedDataGenerator.addRecipientInfoGenerator(jceKey);

            CMSTypedData msg = new CMSProcessableByteArray(is.readAllBytes());
            OutputEncryptor encryptor = new JceCMSContentEncryptorBuilder(CMSAlgorithm.AES128_CBC).setProvider("BC").build();
            CMSEnvelopedData cmsEnvelopedData = cmsEnvelopedDataGenerator.generate(msg, encryptor);
            os.write(cmsEnvelopedData.getEncoded());
        } catch (CertificateEncodingException | CMSException e) {
            log.error("", e);
            throw new IOException(e);
        }
        return null;
    }

    @Override
    public String encryptString(String src) {
        CMSEnvelopedDataGenerator cmsEnvelopedDataGenerator = new CMSEnvelopedDataGenerator();

        try {
            JceKeyTransRecipientInfoGenerator jceKey = new JceKeyTransRecipientInfoGenerator((X509Certificate) asymmetricKeyStoreService.getCertificate());
            cmsEnvelopedDataGenerator.addRecipientInfoGenerator(jceKey);

            CMSTypedData msg = new CMSProcessableByteArray(src.getBytes(StandardCharsets.UTF_8));
            OutputEncryptor encryptor = new JceCMSContentEncryptorBuilder(CMSAlgorithm.AES128_CBC).setProvider("BC").build();
            CMSEnvelopedData cmsEnvelopedData = cmsEnvelopedDataGenerator.generate(msg, encryptor);
            byte[] raw = cmsEnvelopedData.getEncoded();
            byte[] encodedByteArray = base64Service.encode(raw);
            return new String(encodedByteArray, StandardCharsets.UTF_8);
        } catch (CertificateEncodingException | CMSException | IOException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Void decryptFile(File inFile, File outFile) throws IOException {
        try (InputStream is = new FileInputStream(inFile);
             OutputStream os = new FileOutputStream(outFile);) {
            CMSEnvelopedData envelopedData = new CMSEnvelopedData(is.readAllBytes());

            Collection<RecipientInformation> recipients = envelopedData.getRecipientInfos().getRecipients();
            KeyTransRecipientInformation recipientInfo = (KeyTransRecipientInformation) recipients.iterator().next();
            JceKeyTransRecipient recipient = new JceKeyTransEnvelopedRecipient(asymmetricKeyStoreService.getPrivateKey());

            os.write(recipientInfo.getContent(recipient));
        } catch (CMSException e) {
            log.error("", e);
            throw new IOException(e);
        }
        return null;
    }

    @Override
    public String decryptString(String src) {
        try {
            byte[] byteArray = src.getBytes(StandardCharsets.UTF_8);
            byte[] decodedByteArray = base64Service.decode(byteArray);
            CMSEnvelopedData envelopedData = new CMSEnvelopedData(decodedByteArray);

            Collection<RecipientInformation> recipients = envelopedData.getRecipientInfos().getRecipients();
            KeyTransRecipientInformation recipientInfo = (KeyTransRecipientInformation) recipients.iterator().next();
            JceKeyTransRecipient recipient = new JceKeyTransEnvelopedRecipient(asymmetricKeyStoreService.getPrivateKey());

            byte[] raw = recipientInfo.getContent(recipient);
            return new String(raw, StandardCharsets.UTF_8);
        } catch (CMSException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }
}
