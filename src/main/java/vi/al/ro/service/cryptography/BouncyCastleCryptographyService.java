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

import java.io.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Collection;

@Log4j2
@RequiredArgsConstructor
public final class BouncyCastleCryptographyService implements CryptographyService {

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
}
