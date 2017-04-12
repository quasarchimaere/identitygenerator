package quasarchimaere.identitygenerator.core.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import quasarchimaere.identitygenerator.core.enums.EmailNameType;
import quasarchimaere.identitygenerator.core.model.Identity;
import org.apache.commons.lang3.ArrayUtils;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.Scanner;

@Service
public class IdentityGeneratorServiceImpl implements IdentityGeneratorService, InitializingBean {
    private String[] firstNames;
    private String[] lastNames;
    private String[] emailProvider;

    private MessageDigest digest;

    @Value("classpath:firstnames.csv")
    private Resource firstNameResource;

    @Value("classpath:lastnames.csv")
    private Resource lastNameResource;

    @Value("classpath:emailprovider.csv")
    private Resource emailProviderResource;

    public IdentityGeneratorServiceImpl() throws NoSuchAlgorithmException {
        this("SHA-256");
    }

    public IdentityGeneratorServiceImpl(String hashAlgorithm) throws NoSuchAlgorithmException {
        this(MessageDigest.getInstance(hashAlgorithm));
    }

    public IdentityGeneratorServiceImpl(MessageDigest digest) throws NoSuchAlgorithmException {
        this.digest = digest;
    }

    @Override
    public Identity generateIdentity(int i) throws UnsupportedEncodingException{
        return generateIdentity(ByteBuffer.allocate(4).putInt(i).array());
    }

    @Override
    public Identity generateIdentity(String str) throws UnsupportedEncodingException{
        return generateIdentity(str.getBytes("UTF-8"));
    }

    @Override
    public Identity generateIdentity(byte[] bytes) throws UnsupportedEncodingException {
        Identity identity = new Identity(generateFirstName(bytes), generateLastName(bytes));
        identity.setEmail(generateEmail(bytes));

        return identity;
    }

    private byte[] generateByteHash(byte[] original, String salt) throws UnsupportedEncodingException{
        byte[] saltBytes = salt.getBytes("UTF-8");

        return ArrayUtils.addAll(original, saltBytes);
    }

    private String generateFirstName(byte[] original) throws UnsupportedEncodingException{
        digest.update(generateByteHash(original, "fn"));
        long index = getLongValueFromHash(digest.digest()) % firstNames.length;

        return firstNames[(int) index];
    }

    private String generateLastName(byte[] original) throws UnsupportedEncodingException{
        String lastName;

        digest.update(generateByteHash(original, "ln"));
        long index = getLongValueFromHash(digest.digest()) % lastNames.length;

        lastName=lastNames[(int) index];

        digest.update(generateByteHash(original, "lnhyphen"));
        if(getLongValueFromHash(digest.digest()) % 7 == 1L) {
            digest.update(generateByteHash(original, "ln2p"));
            long index2 = getLongValueFromHash(digest.digest()) % lastNames.length;

            if(index!=index2){
                lastName=lastName+"-"+lastNames[(int) index2];
            }
        }

        return lastName;
    }

    private String generateEmail(byte[] original) throws UnsupportedEncodingException{
        StringBuilder email = new StringBuilder();

        String firstNamePart = "";

        digest.update(generateByteHash(original, "fnmail"));
        int firstnameIndex = (int) (getLongValueFromHash(digest.digest()) % EmailNameType.values().length);
        switch (EmailNameType.values()[firstnameIndex]) {
            case FIRSTLETTER:
                firstNamePart = generateFirstName(original).substring(0,1);
                break;
            case FULLNAME:
                firstNamePart = generateFirstName(original);
                break;
            default:
            case NONAME:
                break;
        }
        email.append(firstNamePart);

        if(firstNamePart.length()>0) {
            digest.update(generateByteHash(original, "dot"));
            if (getLongValueFromHash(digest.digest()) % 1 == 1L) {
                email.append('.');
            }
        }

        String lastNamePart = "";
        digest.update(generateByteHash(original, "lnmail"));
        int lastnameIndex = (int) (getLongValueFromHash(digest.digest()) % EmailNameType.values().length);
        switch (EmailNameType.values()[lastnameIndex]) {
            case FIRSTLETTER:
                //use the full lastname for the email address if the firstname was anything else than the fullname
                if(EmailNameType.values()[firstnameIndex] != EmailNameType.FULLNAME) {
                    lastNamePart = generateLastName(original).substring(0, 1);
                    break;
                }
            case NONAME:
            case FULLNAME:
                lastNamePart = generateLastName(original);
                break;
            default:
                break;
        }
        email.append(lastNamePart);

        digest.update(generateByteHash(original, "usenumber"));
        if(getLongValueFromHash(digest.digest()) % 10 == 1L){ /*10 percent change that the mail will end in a number*/
            digest.update(generateByteHash(original, "nummail"));
            int number = (int) (getLongValueFromHash(digest.digest()) % 1000);
            email.append(number);
        }

        email.append('@');

        digest.update(generateByteHash(original, "provider"));
        int providerIndex = (int) (getLongValueFromHash(digest.digest()) % emailProvider.length);
        email.append(emailProvider[providerIndex]);

        return Normalizer.normalize(email.toString().toLowerCase(), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    private long getLongValueFromHash(byte[] hash){
        long value = 0;

        for(int i=0; i < hash.length; i++){
            value += (long) hash[i] & 0xffL << (8 * i);
        }
        return (value < 0)? value * -1 : value;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Scanner sc = new Scanner(firstNameResource.getFile());
        this.firstNames = sc.nextLine().split(";");

        sc = new Scanner(lastNameResource.getFile());
        this.lastNames = sc.nextLine().split(";");

        sc = new Scanner(emailProviderResource.getFile());
        this.emailProvider = sc.nextLine().split(";");
    }
}
