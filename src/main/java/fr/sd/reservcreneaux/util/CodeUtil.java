package fr.sd.reservcreneaux.util;

import java.security.SecureRandom;
import java.util.Base64;

public class CodeUtil {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public static String generateRandomCode() {
        byte[] randomBytes = new byte[6]; // Generate a 6-byte code
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
