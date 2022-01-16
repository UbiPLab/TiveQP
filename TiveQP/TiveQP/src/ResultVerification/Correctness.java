package ResultVerification;

import QueryProcessing.Cloud_Query;

import java.util.List;

public class Correctness {
    public static boolean verify_Correctness_Decrypt(List<Cloud_Query.Proof_Result> C , String user_input){
        boolean flag = false;
        int right_count = 0;
        String[] strs_user = user_input.split("\\*\\*");
        //  "Restaurants**ATLANTA**33.78860826**-84.3690906**12**11"

        for (Cloud_Query.Proof_Result s:C) {
            //  Restaurants**ATLANTA**33.78860826**-84.3690906**16**0**22**0
            String decode = MyUtil.AESUtil.decryptByHexString(s.ciper,"2bc73dw20ebf4d46");
            String[] result = decode.split("\\*\\*");

            //  判断
            if (strs_user[0].equals(result[0]) && strs_user[1].equals(result[1]) &&
                    Integer.valueOf(strs_user[4]) >= Integer.valueOf(result[4]) &&
                    Integer.valueOf(strs_user[4]) <= Integer.valueOf(result[6])      ){
                right_count++;
            }else {
                System.out.println("存在一个假元素：" + decode );
                System.out.println(user_input );
                System.out.println(decode);
            }
        }
        if (right_count == C.size()){
            flag = true;
        }else {
            flag = false;
        }
        return flag;
    }

    public static boolean verify_Correctness_HV(List<byte[]> HV_list,List<Double> Height_list,byte[] root,double root_height){
        boolean flag = false;


//        while (Height_list.get(0) < root_height){
        while (Height_list.size()>1){
//            for (Double s:Height_list) {
//                System.out.print(s+",");
//            }
//            System.out.println();
            int old_len = Height_list.size();
            for (int i = 0; i < Height_list.size() -1; i++) {
                double h = Height_list.get(i);
                double h_1 = Height_list.get(i+1);
                if (h == h_1){
                    byte[] sum = MyUtil.HashFounction.mdinstance.digest(addBytes(HV_list.get(i),HV_list.get(i+1)));
                    HV_list.set(i,sum);
                    HV_list.remove(i+1);
                    Height_list.set(i,h+1);
                    Height_list.remove(i+1);
                }
            }
            int new_len = Height_list.size();
            if ((new_len == old_len)){
//            if ((new_len == old_len) && (Height_list.get(0) == Height_list.get(new_len/2))){
                return true;
            }

        }

//        byte[] sum = MyUtil.HashFounction.mdinstance.digest(addBytes(HV_list.get(HV_list.size()-2),HV_list.get(HV_list.size()-1)));
//
//        for (int i = HV_list.size()-3 ; i >= 0; i--) {
//            sum = MyUtil.HashFounction.mdinstance.digest(addBytes(HV_list.get(i),sum));
//        }
        if (root == null){
            return true;
        }
        for (int i = 0; i < root.length; i++) {
            if (HV_list.get(0)[i] == root[i]){
                flag = true;
            }
        }

        return flag;
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
}
