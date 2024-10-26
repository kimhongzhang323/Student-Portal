
# Password Hashing in the Student Portal Application

## Overview
In the Student Portal application, user passwords are hashed before being stored in the database. This is done to enhance security, ensuring that even if the database is compromised, user passwords remain protected. The application uses the PBKDF2 (Password-Based Key Derivation Function 2) algorithm for password hashing.

## Why Hash Passwords?
- **Security**: Storing plain-text passwords is insecure. If an attacker gains access to the database, they could easily read and misuse users' passwords. Hashing transforms the password into a non-reversible format.
- **Salted Hashing**: By using a unique salt for each password, we further increase security. Salting ensures that even if two users have the same password, their hashes will produce different hash values. This protects against pre-computed attacks such as rainbow tables.

## How Password Hashing Works

### 1. Password and Salt Generation
- **Salt**: A salt is a random value added to the password before hashing. It ensures that even if two users have the same password, their hashes will differ, making it more difficult for attackers to use pre-computed hash databases.
- The application generates a 16-byte salt using the `SecureRandom` class. This class provides a strong level of randomness, making the salt hard to guess.

#### Example of Salt Generation
```java
byte[] salt = PasswordHashing.generateSalt(); // Generates a random 16-byte salt
```

### 2. Hashing Process
The password hashing process is implemented in the `PasswordHashing` class using the `hashPassword` method.

#### Step-by-Step Process
1. **Input**: The method takes two parameters: the plain-text password and the salt.
   
2. **Create a Key Specification**: A `PBEKeySpec` object is created using:
   - The password converted to a character array.
   - The generated salt.
   - An iteration count of 10,000, which increases the computational workload for hashing. This makes brute-force attacks slower and more difficult.
   - A key length of 256 bits, which defines the length of the resulting hash.

   ```java
   PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10000, 256);
   ```

3. **Generate Hash**: A `SecretKeyFactory` for the PBKDF2 algorithm is obtained. The `generateSecret` method is called with the specification to produce the hashed password.

   ```java
   SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
   byte[] hash = factory.generateSecret(spec).getEncoded();
   ```

4. **Encoding**: The resulting hash is encoded to a Base64 string, which is suitable for storage in the database.

   ```java
   return Base64.getEncoder().encodeToString(hash); // Returns the hashed password as a Base64 string
   ```

#### Complete Hashing Example
Here's a complete example demonstrating how to hash a user's password:

```java
public class ExampleUsage {
    public static void main(String[] args) {
        try {
            // User's password input
            String password = "userPassword123";
            
            // Generate a salt
            byte[] salt = PasswordHashing.generateSalt();

            // Hash the password with the generated salt
            String hashedPassword = PasswordHashing.hashPassword(password, salt);

            // Output the results
            System.out.println("Original Password: " + password);
            System.out.println("Salt (Base64): " + Base64.getEncoder().encodeToString(salt));
            System.out.println("Hashed Password (Base64): " + hashedPassword);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.err.println("Error during password hashing: " + e.getMessage());
        }
    }
}
```

### 3. Verifying Passwords
When a user attempts to log in, the application follows these steps to verify the password:
1. Retrieve the stored salt and hashed password from the database for the user attempting to log in.
2. Hash the entered password using the same salt and PBKDF2 algorithm.
3. Compare the newly generated hash with the stored hash. If they match, authentication is successful.

#### Example of Password Verification
```java
public boolean verifyPassword(String enteredPassword, String storedHashedPassword, byte[] storedSalt) {
    try {
        // Hash the entered password with the stored salt
        String hashedEnteredPassword = PasswordHashing.hashPassword(enteredPassword, storedSalt);
        
        // Compare the hashed entered password with the stored hash
        return hashedEnteredPassword.equals(storedHashedPassword);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
        System.err.println("Error during password verification: " + e.getMessage());
        return false;
    }
}
```

## Conclusion
The password hashing implementation in the Student Portal ensures that user passwords are securely managed. By utilizing PBKDF2 with salting, the application provides a robust defense against common attacks like rainbow table attacks and brute-force attacks. This implementation highlights the importance of secure password management in software development.

For further details, refer to the `PasswordHashing` class in the source code.
