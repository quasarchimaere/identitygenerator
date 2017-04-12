package quasarchimaere.identitygenerator.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("quasarchimaere.identitygenerator.core")
@ComponentScan("quasarchimaere.identitygenerator.web")
public class IdentityGeneratorApp {
    public static void main(String[] args) {
        SpringApplication.run(IdentityGeneratorApp.class, args);
    }
}
