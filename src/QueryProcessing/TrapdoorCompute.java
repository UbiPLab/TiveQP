package QueryProcessing;

import IndexBuilding.User;

import java.math.BigInteger;
import java.security.MessageDigest;

public class TrapdoorCompute {

    public static String[][] T1(String type ,String[] Keylist , int rb) throws Exception {
        String[] type_Prefix = new User().UserType(type);
        String[][] result = new String[type_Prefix.length][Keylist.length - 1];

        for (int w=0;w<type_Prefix.length;w++) {                     // twice hash
            for (int i = 0; i < Keylist.length - 1; i++) {
                byte[] outbytes = MyUtil.HashFounction.HmacSHA256Encrypt(type_Prefix[w] ,Keylist[i]);    //  HMAC(w,k_i)

                byte[] hkp1 = MyUtil.HashFounction.mdinstance.digest(addBytes(outbytes, Keylist[Keylist.length - 1].getBytes()));//h_k+1

                result[w][i]= toHexString(outbytes)+","+toHexString(hkp1);    //trapdoor M element
            }
        }
        return result;                     // return trapdoor
    }

    public static String[][] T2(String city, double lat, double lng ,String[] Keylist , int rb) throws Exception {
        String[] Location_Prefix = new User().UserLocation(city,lat,lng);
        String[][] result = new String[Location_Prefix.length][Keylist.length - 1];

        for (int w=0;w<Location_Prefix.length;w++) {                     // twice hash
            for (int i = 0; i < Keylist.length - 1; i++) {
                byte[] outbytes = MyUtil.HashFounction.HmacSHA256Encrypt(Location_Prefix[w] ,Keylist[i]);    //  HMAC(w,k_i)

                byte[] hkp1 = MyUtil.HashFounction.mdinstance.digest(addBytes(outbytes, Keylist[Keylist.length - 1].getBytes()));//h_k+1

                result[w][i]= toHexString(outbytes)+","+toHexString(hkp1);    //trapdoor M element
            }
        }
        return result;                     // return trapdoor
    }

    public static String[][] T3(int hour_open,int min_open ,String[] Keylist , int rb) throws Exception {
        String[] time_Prefix = new User().UserTime(hour_open,min_open);
        String[][] result = new String[time_Prefix.length][Keylist.length - 1];

        for (int w=0;w<time_Prefix.length;w++) {                     // twice hash
            for (int i = 0; i < Keylist.length - 1; i++) {
                byte[] outbytes = MyUtil.HashFounction.HmacSHA256Encrypt(time_Prefix[w] ,Keylist[i]);    //  HMAC(w,k_i)

                byte[] hkp1 = MyUtil.HashFounction.mdinstance.digest(addBytes(outbytes, Keylist[Keylist.length - 1].getBytes()));//h_k+1

                result[w][i]= toHexString(outbytes)+","+toHexString(hkp1);    //trapdoor M element
            }
        }
        return result;                     // return trapdoor
    }




    public static byte[] addBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;

    }

    public static byte[] addBytes(Byte[] data1, Byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        for (int i = 0; i < data1.length; i++) {
            data3[i] = data1[i];
        }
        for (int i = 0; i < data2.length; i++) {
            data3[i+data1.length] = data2[i];
        }
        return data3;
    }

    public static String byteToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String strHex = Integer.toHexString(bytes[i]);
            if (strHex.length() > 3) {
                sb.append(strHex.substring(6));
            } else {
                if (strHex.length() < 2) {
                    sb.append("0" + strHex);
                } else {
                    sb.append(strHex);
                }
            }
        }
        return sb.toString();
    }
    public static String toHexString(byte[] byteArray) {
        final StringBuilder hexString = new StringBuilder("");
        if (byteArray == null || byteArray.length <= 0)
            return null;
        for (int i = 0; i < byteArray.length; i++) {
            int v = byteArray[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                hexString.append(0);
            }
            hexString.append(hv);
        }
        return hexString.toString().toLowerCase();
    }
}
