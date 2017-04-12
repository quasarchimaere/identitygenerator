package IdentityGeneratorTest;

import quasarchimaere.identitygenerator.core.model.Identity;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import quasarchimaere.identitygenerator.core.service.IdentityGeneratorServiceImpl;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Theories.class)
public class IdentityGeneratorServiceTest {
    @DataPoints
    //public static int[] numberOfIdentities = {10, 100, 1000, 10000, 100000};
    public static int[] numberOfIdentities = {10, 100, 1000, 10000, 100000, 1000000};

    @DataPoints
    public static double[] ratio = {0.90, 1.0};

    private IdentityGeneratorServiceImpl identityGeneratorService;

    @Before
    public void init() throws NoSuchAlgorithmException, FileNotFoundException{
        identityGeneratorService = new IdentityGeneratorServiceImpl();
    }


    @Test
    public void generateIdentityTest() throws UnsupportedEncodingException {
        Identity identity = identityGeneratorService.generateIdentity("hallo");

        assertEquals("Ekatarini", identity.getFirstName());
        assertEquals("Heider", identity.getLastName());
        assertEquals("ekatariniheider@dekutree.com", identity.getEmail());
    }

    @Theory
    public void collisionTest(int numberOfIdentities, double ratio) throws UnsupportedEncodingException {
        List<Identity> identities = generateIdentities(numberOfIdentities, ratio == 1.0);
        Set<Identity> identitySet = new HashSet<>();

        identitySet.addAll(identities);

        double necessarySetSize = numberOfIdentities * ratio;
        System.out.println("numberOfIdentities: "+numberOfIdentities+" ratio:" + ratio+ " necessarySetSize: "+ necessarySetSize +" actualSetSize: "+identitySet.size());

        assertTrue(identitySet.size() >= necessarySetSize);
    }

    private List<Identity> generateIdentities(int numberOfIdentities, boolean includeEmail) throws UnsupportedEncodingException{
        List<Identity> identities = new ArrayList<>();

        for(int i=0; i < numberOfIdentities; i++) {
            Identity identity = identityGeneratorService.generateIdentity(i);
            if(!includeEmail){
                identity.setEmail(null);
            }

            identities.add(identity);
        }

        return identities;
    }
}
