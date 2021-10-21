package com.uber.driver.onboarding.core.util;

import com.uber.driver.onboarding.model.enums.UserType;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

public class SecurityUtil {

    private static final Logger logger= LoggerFactory.getLogger(SecurityUtil.class);

    public static final String AUTH_COOKIE_NAME = "digiAuth";
    public static final String RST_PWD_TOKEN = "resetPasswordToken";
    public static final String RST_PWD_LINK = "resetPasswordLink";
    public static final String RST_PWD_TOKEN_EXPIRY_MINUTES = "resetPasswordTokenExpiry";

    public static final String PIPE_SEPARATOR = "|";
    public static final String PIPE_REGEX = "\\|";

    private static final Object AES_KEY_MUTEX = new Object();
    private static SecretKey AES_SECRET_KEY = getSecretKey();

    private static final byte[] defaultAuthKey = {65, (byte) 128, 127, (byte) 230, 30, (byte) 252, (byte) 136, (byte) 224, (byte) 135, (byte) 187, (byte) 142, (byte) 208, 104, (byte) 183, (byte) 183, 23, (byte) 184, 11, 104, 102, 84, 107, 21, (byte) 240, (byte) 182, (byte) 242, (byte) 146, 107, 73, 11, 11, 3};

    public static String getRandomSaltForPasswordHash() {
        return getBase64EncodedString(getSecureRandomSalt());
    }

    public static byte[] getPasswordHash(String password, byte[] salt) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            return md.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] getSecureRandomSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public static String getBase64EncodedString(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    public static boolean isPasswordMatch(String base64EncodedPwdHash, String base64EncodedSalt, String password) {
        return base64EncodedPwdHash.equalsIgnoreCase(getBase64EncodedString(getPasswordHash(password, getBase64Decoded(base64EncodedSalt))));
    }

    private static byte[] getBase64Decoded(String encodedString) {
        return Base64.decodeBase64(encodedString);
    }

    private static String getBase64DecodedString(String encodedString) {
        return new String(Base64.decodeBase64(encodedString));
    }

    public static Cookie generateAuthenticationCookie(UserType userType, String profileId) {
        Cookie authenticationCookie = new Cookie(AUTH_COOKIE_NAME, aesEncrypt(createAuthCookieValue(userType, profileId), getAuthKey()));
        setDefaultCookieParams(authenticationCookie);
        authenticationCookie.setMaxAge(60 * 1440);
        return authenticationCookie;
    }

    public static Cookie generateExpiredAuthenticationCookie() {
        Cookie expiredAuthCookie = new Cookie(AUTH_COOKIE_NAME, "");
        setDefaultCookieParams(expiredAuthCookie);
        expiredAuthCookie.setMaxAge(0);
        return expiredAuthCookie;
    }

    private static void setDefaultCookieParams(Cookie cookie) {
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setDomain(".uber.com");
    }

    public static String createAuthCookieValue(UserType userType, String profileId) {
        return userType.name() + PIPE_SEPARATOR + profileId;
    }

    public static String decryptAuthCookie(Cookie cookie) {
        return aesDecrypt(cookie.getValue(), getAuthKey());
    }

    private static SecretKey getSecretKey() {
        try {
            if(AES_SECRET_KEY == null) {
                synchronized (AES_KEY_MUTEX) {
                    if(AES_SECRET_KEY == null) {
                        String secretPassword = "DriverOnBoarding";
                        String secretSalt = "@2021";
                        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
                        KeySpec spec = new PBEKeySpec(secretPassword.toCharArray(), secretSalt.getBytes(), 65536, 256);
                        SecretKey tmp = factory.generateSecret(spec);
                        AES_SECRET_KEY = new SecretKeySpec(tmp.getEncoded(), "AES");
                    }
                }
            }
        }
        catch (Exception ex) {
            logger.error("Error in creating secret key.", ex);
        }
        return AES_SECRET_KEY;
    }

    private static String aesEncryptionV2(String originalString) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());
            AlgorithmParameters params = cipher.getParameters();
            byte[] ivParams = params.getParameterSpec(IvParameterSpec.class).getIV();
            byte[] cipherText = cipher.doFinal(originalString.getBytes(StandardCharsets.UTF_8));
            return (new String(Base64.encodeBase64(ivParams)) + PIPE_SEPARATOR + new String(Base64.encodeBase64(cipherText)));
        }
        catch (Exception ex) {
            logger.error("Exception while AES ecryption V2.");
            throw new RuntimeException(ex);
        }
    }

    private static String aesDecryptionV2(String encryptedString) {
        try {
            String[] encryptedArray = encryptedString.split(PIPE_REGEX);
            byte[] ivParams = Base64.decodeBase64(encryptedArray[0].getBytes());
            byte[] cipherText = Base64.decodeBase64(encryptedArray[1].getBytes());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), new IvParameterSpec(ivParams));
            return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
        }
        catch (Exception ex) {
            logger.error("Exception while AES ecryption V2.");
            throw new RuntimeException(ex);
        }
    }

    public static byte[] getAuthKey() {
        byte[] authKeyArr = new byte[32];
        System.arraycopy(defaultAuthKey, 0, authKeyArr, 0, defaultAuthKey.length);
        return authKeyArr;
    }

    public static String aesEncrypt(String originalString, byte[] authKey) {
        String encryptedString = "";
        if (originalString != null) {
            byte[] encoded = aesEncrypt(originalString.getBytes(), authKey);
            if (encoded != null) {
                encoded = Base64.encodeBase64(encoded);
                encryptedString = new String(encoded);
            }
        }
        return encryptedString;
    }

    public static byte[] aesEncrypt(byte[] input, byte[] authKey) {
        byte[] encoded = null;
        try {
            int rem = input.length % 16;
            if (rem > 0) {
                byte[] paddedArray = new byte[input.length + 16 - rem];
                System.arraycopy(input, 0, paddedArray, 0, input.length);
                input = paddedArray;
            }
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            //Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(authKey, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            encoded = cipher.doFinal(input);
        } catch (Exception e) {
            String encodedInput = "undefined";
            String encodedKey = "undefined";
            try {
                encodedInput = GenUtil.asHex(input);
                encodedKey = GenUtil.asHex(authKey);
            } catch (Exception ignored) {
            }
            logger.warn("Error encrypting '" + encodedInput + "' using aesEncrypt with key: " + encodedKey, e);
        }
        return encoded;
    }

    public static String aesDecrypt(String encryptedString, byte[] authKey) {
        String originalString = "";
        if (encryptedString != null) {
            byte[] byteArrToDecode = encryptedString.getBytes();
            byte[] decodedString = Base64.decodeBase64(byteArrToDecode);
            byte[] original = aesDecrypt(decodedString, authKey);
            if (original != null) {
                originalString = new String(original);
            }
        }
        return originalString;
    }

    public static byte[] aesDecrypt(byte[] encrypted, byte[] authKey) {
        byte[] original = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            //Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(authKey, "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decypted = cipher.doFinal(encrypted);
            // Ignore 0 padded bytes.
            int i = decypted.length;
            do {
                i--;
            } while (i >= 0 && decypted[i] == 0);
            i++;
            original = new byte[i];
            System.arraycopy(decypted, 0, original, 0, i);
        } catch (Exception e) {
            String encodedInput = "undefined";
            String encodedKey = "undefined";
            try {
                encodedInput = GenUtil.asHex(encrypted);
                encodedKey = GenUtil.asHex(authKey);
            } catch (Exception ignored) {
            }
            logger.warn("Error decrypting '" + encodedInput + "' using aesEncrypt with key: " + encodedKey, e);
        }
        return original;
    }

}
