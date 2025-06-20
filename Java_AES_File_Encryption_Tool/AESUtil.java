import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.util.Arrays;

public class AESUtil {
    public static SecretKey generateKey(int keySize) throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(keySize);
        return generator.generateKey();
    }

    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static byte[] encrypt(byte[] input, SecretKey key, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return cipher.doFinal(input);
    }

    public static byte[] decrypt(byte[] input, SecretKey key, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return cipher.doFinal(input);
    }

    public static void saveKey(SecretKey key, String path) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(key.getEncoded());
        }
    }

    public static SecretKey loadKey(String path) throws IOException {
        byte[] keyBytes = java.nio.file.Files.readAllBytes(new File(path).toPath());
        return new SecretKeySpec(keyBytes, "AES");
    }
}