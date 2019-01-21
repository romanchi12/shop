package org.romanchi.validators;

import org.romanchi.Wired;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.logging.Logger;

public class EmailValidator {

    @Wired
    Logger logger;

    public boolean validate(String email){
        logger.info(email);
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        logger.info(String.valueOf(result));
        return result;
    }

    public EmailValidator(){}
}
