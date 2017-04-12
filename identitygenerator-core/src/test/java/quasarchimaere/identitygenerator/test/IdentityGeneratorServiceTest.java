package quasarchimaere.identitygenerator.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import quasarchimaere.identitygenerator.core.model.Identity;
import quasarchimaere.identitygenerator.core.service.IdentityGeneratorService;
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

@RunWith(SpringJUnit4ClassRunner.class)
// ApplicationContext will be loaded from the static inner ContextConfiguration class
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class IdentityGeneratorServiceTest {
    @Configuration
    static class ContextConfiguration{
        @Bean
        public IdentityGeneratorService getIdentityGeneratorService() throws FileNotFoundException, NoSuchAlgorithmException {
            return new IdentityGeneratorServiceImpl();
        }
    }

    @Autowired
    private IdentityGeneratorService identityGeneratorService;

    @Test
    public void generateIdentityTest() throws UnsupportedEncodingException {
        Identity identity = identityGeneratorService.generateIdentity("hallo");

        assertEquals("Ekatarini", identity.getFirstName());
        assertEquals("Heiser-Simon", identity.getLastName());
        assertEquals("ekatariniheiser-simon@dekutree.com", identity.getEmail());
    }

    @Test
    public void collisionTest() throws UnsupportedEncodingException{
        collisionTest(1000000,0.99, false);
    }

    @Test
    public void collisionTestInclEmail() throws UnsupportedEncodingException{
        collisionTest(1000000,0.99, true);
    }

    private void collisionTest(int numberOfIdentities, double ratio, boolean includeEmail) throws UnsupportedEncodingException {
        List<Identity> identities = generateIdentities(numberOfIdentities, includeEmail);
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
