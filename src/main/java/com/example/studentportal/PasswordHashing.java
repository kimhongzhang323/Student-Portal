package com.example.studentportal;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * The PasswordHashing class provides methods for securely hashing passwords using
 * the PBKDF2 (Password-Based Key Derivation Function 2) algorithm. It also includes
 * functionality to generate a random salt value to enhance the security of password 
 * storage.
 */
public class PasswordHashing {

    /**
     * Hashes a password using PBKDF2 with HMAC SHA-256 algorithm.
     * 
     * @param password the plain-text password to be hashed
     * @param salt the salt value used to enhance hashing security
     * @return the hashed password encoded as a Base64 string
     * @throws NoSuchAlgorithmException if the specified algorithm is not available
     * @throws InvalidKeySpecException if the key specification is invalid
     */
    public static String hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Create a PBEKeySpec with the password, salt, iteration count, and key length
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10000, 256);
        
        // Get a SecretKeyFactory for the PBKDF2 algorithm
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        
        // Generate the secret (hashed password) based on the specification
        byte[] hash = factory.generateSecret(spec).getEncoded();
        
        // Encode the hash using Base64 and return as a string
        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Generates a random salt value for use in password hashing.
     * 
     * @return a byte array containing the randomly generated salt
     */
    public static byte[] generateSalt() {
        // Create a SecureRandom instance for generating random bytes
        SecureRandom random = new SecureRandom();
        
        // Create a byte array for the salt with a length of 16 bytes
        byte[] salt = new byte[16];
        
        // Fill the byte array with random values
        random.nextBytes(salt);
        
        return salt; // Return the generated salt
    }
}
