package MyUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

public class AESUtil{

    private static final IvParameterSpec IV_PARAMETER_SPEC = new IvParameterSpec("0000000000000000".getBytes());


    public static String encryptIntoHexString(String data, String key){
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"), IV_PARAMETER_SPEC);
            return bytesConvertHexString(cipher.doFinal(Arrays.copyOf(data.getBytes(), 16 * ((data.getBytes().length / 16) + 1))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String decryptByHexString(String data, String key){
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"), IV_PARAMETER_SPEC);
            return new String(cipher.doFinal(hexStringConvertBytes(data.toLowerCase())),"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    private static String bytesConvertHexString(byte [] data){
        StringBuffer result = new StringBuffer();
        String hexString = "";
        for (byte b : data) {
            // 补码成正十进制后转换成16进制
            hexString = Integer.toHexString(b & 255);
            result.append(hexString.length() == 1 ? "0" + hexString : hexString);
        }
        return result.toString().toUpperCase();
    }


    private static byte [] hexStringConvertBytes(String data){
        int length = data.length() / 2;
        byte [] result = new byte[length];
        for (int i = 0; i < length; i++) {
            int first = Integer.parseInt(data.substring(i * 2, i * 2 + 1), 16);
            int second = Integer.parseInt(data.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (first * 16 + second);
        }
        return result;
    }


}