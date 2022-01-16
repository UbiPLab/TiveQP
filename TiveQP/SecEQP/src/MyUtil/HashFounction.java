package MyUtil;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class HashFounction {
    public static MessageDigest mdinstance;

    static {
        try {
            mdinstance = MessageDigest.getInstance("sha-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static MessageDigest H;

    static {
        try {
            H = MessageDigest.getInstance("sha-384");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static byte[] HmacSHA256Encrypt(String encryptText, String encryptKey) throws Exception {
        byte[] data=encryptKey.getBytes("utf-8");
        //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, "HmacSHA256");
        //生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance("HmacSHA1");
        //用给定密钥初始化 Mac 对象
        mac.init(secretKey);

        byte[] text = encryptText.getBytes("utf-8");
        //完成 Mac 操作
        return mac.doFinal(text);
    }

    public static String[] CreateSecretKey(int keylength) {
        String[] keylist = new String[keylength];

        Random random = new Random();
        int length = 16;//自定义的位数
        for (int j = 0; j < keylist.length; j++) {
            StringBuffer bigstring = new StringBuffer();
            for (int i = 0; i < length; i++) {
                bigstring.append(random.nextInt(10));
            }
            keylist[j] = bigstring.toString();
        }
        return keylist;
    }
}
