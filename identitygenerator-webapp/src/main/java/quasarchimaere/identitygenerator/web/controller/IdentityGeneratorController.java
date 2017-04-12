package quasarchimaere.identitygenerator.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import quasarchimaere.identitygenerator.core.model.Identity;
import quasarchimaere.identitygenerator.core.service.IdentityGeneratorServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
public class IdentityGeneratorController implements ErrorController{
    @Autowired
    private IdentityGeneratorServiceImpl identityGeneratorService;

    @Autowired
    private ErrorAttributes errorAttributes;

    private static final String ERROR_PATH = "/error";

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @RequestMapping(value= "/error")
    public ResponseEntity<Map<String, Object>> errorHandling(HttpServletRequest request, HttpServletResponse response){
        return new ResponseEntity<>(getErrorAttributes(request, false), HttpStatus.valueOf(response.getStatus()));
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
    }


    @RequestMapping(value = "/{value}", method = RequestMethod.GET)
    public ResponseEntity<Identity> generateIdentity(@PathVariable String value) throws UnsupportedEncodingException, FileNotFoundException, NoSuchAlgorithmException {
        Identity identity = identityGeneratorService.generateIdentity(value);

        return new ResponseEntity<Identity>(identity, HttpStatus.OK);
    }
}
