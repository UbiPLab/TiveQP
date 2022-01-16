package AdvanceTest;

import IndexBuilding.Owner;
import IndexBuilding.User;
import QueryProcessing.TrapdoorCompute;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class TestPlaintextQuery {

    public static void main(String[] args) throws Exception {
        //  Restaurants**ATLANTA**33.772758**-84.380375**11**0**21**0
        String type = "Restaurants";
        String city = "ATLANTA";
        double lat = 33.772758;
        double lng = -84.380375;
        int open_hour = 11;
        int open_min = 0;
        int close_hour = 21;
        int close_min = 0;


        System.out.println();
        System.out.println("IBF setting:");
        int ibf_length = 10000;
        System.out.println("ibf_length: "+ibf_length);

        System.out.print("KeyList[]:  ");
        String[] Keylist = { "2938879577741549","8729598049525437","8418086888563864","0128636306393258","2942091695121238","6518873307787549"};
        MyUtil.Show.showString_list(Keylist);
        System.out.print("Random Number:  ");
        int randNumber = 235648;
        System.out.println(randNumber);
    /*
        Owner
     */
        String[] type_owner = new Owner().OwnerType(type);
        String[] location_owner = new Owner().OwnerLocation(city,lat,lng);
        String[] time_owner = new Owner().OwnerTime(open_hour,open_min,close_hour,close_min);




    /*
        User
     */

        String[] Type_prefix = new User().UserType(type);
        String[] Location = new User().UserLocation(city,lat,lng);
        String[] Time_prefix = new User().UserTime(open_hour,open_min);




        System.out.println("Type_prefix :");

        MyUtil.Show.showString_list(type_owner);
        MyUtil.Show.showString_list(Type_prefix);

//        System.out.println("Location :");
//
//        MyUtil.Show.showString_list(location_owner);
//        MyUtil.Show.showString_list(Location);
//
//        System.out.println("Time_prefix :");
//
//        MyUtil.Show.showString_list(time_owner);
//        MyUtil.Show.showString_list(Time_prefix);

        Byte[][] twinlist = new Byte[2][ibf_length];
        for (int i = 0; i < ibf_length; i++) {
            twinlist[0][i] = 0;
            twinlist[1][i] = 0;
        }

        String[][] T1 = new TrapdoorCompute().T1(type,Keylist,randNumber);
        String[][] owner_T = new String[type_owner.length][];
        for (int i = 0; i < type_owner.length; i++) {
            owner_T[i] = insert(twinlist,type_owner[i],Keylist,randNumber);
        }
//        System.out.println("User :");
//        MyUtil.Show.ShowMatrix(T1);
//        System.out.println("Owner :");
//        MyUtil.Show.ShowMatrix(owner_T);

        String[][] user_T = new String[Type_prefix.length][T1[0].length];
        for (int i = 0; i < T1.length; i++) {
            int count = 0;
            for (int j = 0; j < T1[i].length; j++) {
                String[] twins_value = T1[i][j].split(",");

                byte[] outbytes = toByteArray(twins_value[0]);
//                user_T[i][j] = outbytes;
                BigInteger bi = new BigInteger(1, outbytes);
                int twinindex = bi.mod(BigInteger.valueOf(ibf_length)).intValue();      //  twins_id

                byte[] hkp1 = toByteArray(twins_value[1]);
//                user_T[i][j] = hkp1;
                BigInteger hkp1bi = new BigInteger(1, hkp1);
                byte[] sha1bytes = MyUtil.HashFounction.H.digest(hkp1bi.xor(BigInteger.valueOf(randNumber)).toByteArray());  //sha1_xor rb
                int location = new BigInteger(1, sha1bytes).mod(BigInteger.valueOf(2)).intValue();//mod2
//                user_T[i][j] = sha1bytes;
                if (location == 0){
                    user_T[i][j] = twinindex+"|"+1+"|"+0;
                    System.out.println("Tree: "+twinlist[location][twinindex]+",Trapdoor: 1|0,twinindex :"+twinindex+","+location);
                }else {
                    user_T[i][j] = twinindex+"|"+0+"|"+1;
                    System.out.println("Tree: "+twinlist[location][twinindex]+",Trapdoor: 0|1,twinindex :"+twinindex+","+location);

                }


                if (twinlist[location][twinindex] == 1){

                    count++;
                }
            }
            System.out.println();
            if (count == T1[i].length) {
                System.out.println(i+" "+true);


            }
//            }else {
//                System.out.println(count+"<"+T1[i].length);
//            }
        }

        System.out.println("Owner :");
        for (int i = 0; i < owner_T.length; i++) {
            for (int j = 0; j < owner_T[i].length; j++) {

                    System.out.print(owner_T[i][j] +" ");

                    System.out.print(user_T[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println();

//        System.out.println("User :");
//        for (int i = 0; i < user_T.length; i++) {
//            for (int j = 0; j < user_T[i].length; j++) {
//                for (int k = 0; k < user_T[i][j].length; k++) {
//                    System.out.print(user_T[i][j][k]);
//                }
//                System.out.println();
//            }
//            System.out.println();
//        }


    }

    public static String[] insert(Byte[][] twinlist, String data ,String[] Keylist , int rb) throws Exception {
        String[] result = new String[Keylist.length-1];
        for (int i = 0; i < Keylist.length - 1; i++) {
            byte[] outbytes = MyUtil.HashFounction.HmacSHA256Encrypt(data,Keylist[i]);    //  HMAC(w,k_i)
            BigInteger bi = new BigInteger(1, outbytes);
            int twinindex = bi.mod(BigInteger.valueOf(twinlist[0].length)).intValue();      //  twins_id
            //now we get k twins

            //for each twin, compute the chosen location
            byte[] hkp1 = MyUtil.HashFounction.mdinstance.digest(addBytes(outbytes, Keylist[Keylist.length - 1].getBytes()));//h_k+1
            BigInteger hkp1bi = new BigInteger(1, hkp1);
            byte[] sha1bytes = MyUtil.HashFounction.H.digest(hkp1bi.xor(BigInteger.valueOf(rb)).toByteArray());  //sha1_xor rb
            int location = new BigInteger(1, sha1bytes).mod(BigInteger.valueOf(2)).intValue();//mod2
//            result[i]= byteToHexString(outbytes)+","+byteToHexString(hkp1);
//            result[i]= sha1bytes;

            if (location == 0) {
                twinlist[0][twinindex] = 1;
                twinlist[1][twinindex] = 0;
            } else {
                twinlist[1][twinindex] = 1;
                twinlist[0][twinindex] = 0;
            }
            result[i] = twinindex+"|"+twinlist[0][twinindex]+"|"+twinlist[1][twinindex];
        }
        return result;
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

    public static byte[] toByteArray(String hexString) {
        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() >> 1];
        int index = 0;
        for (int i = 0; i < hexString.length(); i++) {
            if (index  > hexString.length() - 1)
                return byteArray;
            byte highDit = (byte) (Character.digit(hexString.charAt(index), 16) & 0xFF);
            byte lowDit = (byte) (Character.digit(hexString.charAt(index + 1), 16) & 0xFF);
            byteArray[i] = (byte) (highDit << 4 | lowDit);
            index += 2;
        }
        return byteArray;
    }
}
