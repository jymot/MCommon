package im.wangchao.mcommon.utils;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * <p>Description  : DES Encrypt.</p>
 * <p/>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 15/8/4.</p>
 * <p>Time         : 下午1:48.</p>
 */
public class DESEncryptUtils {
    /**
     * 密钥算法
     */
    private static final String KEY_ALGORITHM_3DES = "desede";

    private static final String KEY_ALGORITHM_DES = "des";

    /**
     * 加密/解密算法 / 工作模式 / 填充方式
     * Java 6支持PKCS5PADDING填充方式
     * Bouncy Castle支持PKCS7Padding填充方式
     */
    private static final String CIPHER_ALGORITHM_ECB_3DES = KEY_ALGORITHM_3DES.concat("/ECB/PKCS5Padding");
    private static final String CIPHER_ALGORITHM_CBC_3DES = KEY_ALGORITHM_3DES.concat("/CBC/PKCS5Padding");
    private static final String CIPHER_ALGORITHM_ECB_DES = KEY_ALGORITHM_DES.concat("/ECB/PKCS5Padding");
    private static final String CIPHER_ALGORITHM_CBC_DES = KEY_ALGORITHM_DES.concat("/CBC/PKCS5Padding");

    private DESEncryptUtils(){
        throw new AssertionError();
    }

    /**
     * ECB 3DES 加密
     *
     * @param key   byte[]
     * @param data  byte[]
     * @return byte[]
     * @throws Exception
     */
    public static byte[] des3EncodeECB(byte[] key, byte[] data)
            throws Exception {
        return encodeECB(KEY_ALGORITHM_3DES, CIPHER_ALGORITHM_ECB_3DES, key, data);
    }

    public static byte[] desEncodeECB(byte[] key, byte[] data)
            throws Exception {
        return encodeECB(KEY_ALGORITHM_DES, CIPHER_ALGORITHM_ECB_DES, key, data);
    }

    /**
     * ECB 3DES 解密
     *
     * @param key   byte[]
     * @param data  byte[]
     * @return byte[]
     * @throws Exception
     */
    public static byte[] des3DecodeECB(byte[] key, byte[] data)
            throws Exception {
        return decodeECB(KEY_ALGORITHM_3DES, CIPHER_ALGORITHM_ECB_3DES, key, data);
    }

    public static byte[] desDecodeECB(byte[] key, byte[] data)
            throws Exception {
        return decodeECB(KEY_ALGORITHM_DES, CIPHER_ALGORITHM_ECB_DES, key, data);
    }

    /**
     * CBC 3DES 加密
     *
     * @param key   byte[]
     * @param keyiv byte[]
     * @param data  byte[]
     * @return byte[]
     * @throws Exception
     */
    public static byte[] des3EncodeCBC(byte[] key, byte[] keyiv, byte[] data)
            throws Exception {
        return encodeCBC(KEY_ALGORITHM_3DES, CIPHER_ALGORITHM_CBC_3DES, key, keyiv, data);
    }

    public static byte[] desEncodeCBC(byte[] key, byte[] keyiv, byte[] data)
            throws Exception {
        return encodeCBC(KEY_ALGORITHM_DES, CIPHER_ALGORITHM_CBC_DES, key, keyiv, data);
    }

    /**
     * CBC 3DES 解密
     *
     * @param key   byte[]
     * @param keyiv byte[]
     * @param data  byte[]
     * @return byte[]
     * @throws Exception
     */
    public static byte[] des3DecodeCBC(byte[] key, byte[] keyiv, byte[] data)
            throws Exception {
        return decodeCBC(KEY_ALGORITHM_3DES, CIPHER_ALGORITHM_CBC_3DES, key, keyiv, data);
    }

    public static byte[] desDecodeCBC(byte[] key, byte[] keyiv, byte[] data)
            throws Exception {
        return decodeCBC(KEY_ALGORITHM_DES, CIPHER_ALGORITHM_CBC_DES, key, keyiv, data);
    }

    public static byte[] genDESKey() throws NoSuchAlgorithmException {
        return genKey(KEY_ALGORITHM_DES, 56);
    }

    public static byte[] gen3DESKey() throws NoSuchAlgorithmException {
        return genKey(KEY_ALGORITHM_3DES, 168);
    }

    private static byte[] encodeECB(String algorithm, String cipherAlgorithm, byte[] key, byte[] data) throws Exception {
        Key deskey = algorithm.equals(KEY_ALGORITHM_DES) ? toDesKey(key) : to3DESKey(key);

        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        cipher.init(Cipher.ENCRYPT_MODE, deskey);
        return cipher.doFinal(data);
    }

    private static byte[] decodeECB(String algorithm, String cipherAlgorithm, byte[] key, byte[] data)
            throws Exception {
        Key deskey = algorithm.equals(KEY_ALGORITHM_DES) ? toDesKey(key) : to3DESKey(key);

        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        cipher.init(Cipher.DECRYPT_MODE, deskey);
        return cipher.doFinal(data);
    }

    private static byte[] encodeCBC(String algorithm, String cipherAlgorithm, byte[] key, byte[] keyiv, byte[] data)
            throws Exception {
        Key deskey = algorithm.equals(KEY_ALGORITHM_DES) ? toDesKey(key) : to3DESKey(key);

        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        return cipher.doFinal(data);
    }

    private static byte[] decodeCBC(String algorithm, String cipherAlgorithm, byte[] key, byte[] keyiv, byte[] data)
            throws Exception {
        Key deskey = algorithm.equals(KEY_ALGORITHM_DES) ? toDesKey(key) : to3DESKey(key);

        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        IvParameterSpec ips = new IvParameterSpec(keyiv);

        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

        return cipher.doFinal(data);
    }

    /**
     * 随机生成一个二进制密钥
     *
     * @return 二进制密钥
     * @throws NoSuchAlgorithmException
     */
    private static byte[] genKey(String algorithm, int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);

        keyGenerator.init(keySize);

        SecretKey secretKey = keyGenerator.generateKey();

        return secretKey.getEncoded();
    }

    /**
     * 转换密钥 desede
     *
     * @param key 二进制密钥
     * @return Key 密钥
     * @throws Exception
     */
    private static Key to3DESKey(byte[] key) throws Exception {
        // 实例化DES密钥材料
        DESedeKeySpec dks = new DESedeKeySpec(key);

        // 实例化秘密密钥工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM_3DES);

        // 生成秘密密钥
        return keyFactory.generateSecret(dks);
    }

    /**
     * 转换密钥 des
     *
     * @param key 二进制密钥
     * @return Key 密钥
     * @throws Exception
     */
    private static Key toDesKey(byte[] key) throws Exception {

        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM_DES);

        return keyFactory.generateSecret(dks);
    }
}
