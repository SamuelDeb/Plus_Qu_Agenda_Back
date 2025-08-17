package fr.sd.reservcreneaux.util;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CodeUtilTest {

    @Test
    public void testGenerateRandomCodeLength() {
        String code = CodeUtil.generateRandomCode();
        // Decode the Base64 encoded string to get the original byte array length
        byte[] decodedBytes = java.util.Base64.getUrlDecoder().decode(code);
        assertEquals(6, decodedBytes.length, "Generated code should be 6 bytes long");
    }

    @Test
    public void testGenerateRandomCodeUniqueness() {
        Set<String> generatedCodes = new HashSet<>();
        int numberOfCodesToGenerate = 1000;

        for (int i = 0; i < numberOfCodesToGenerate; i++) {
            String code = CodeUtil.generateRandomCode();
            // Ensure the code is unique
            assertFalse(generatedCodes.contains(code), "Duplicate code found: " + code);
            generatedCodes.add(code);
        }
    }

    @Test
    public void testGenerateRandomCodeFormat() {
        String code = CodeUtil.generateRandomCode();
        assertDoesNotThrow(() -> {
            // Try to decode the Base64 URL-safe encoded string
            java.util.Base64.getUrlDecoder().decode(code);
        }, "Generated code should be a valid Base64 URL-safe string");
    }
}