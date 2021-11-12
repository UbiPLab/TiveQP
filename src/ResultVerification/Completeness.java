package ResultVerification;

import QueryProcessing.Cloud_Query;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static QueryProcessing.Cloud_Query.toByteArray;
import static ResultVerification.Correctness.addBytes;

public class Completeness {
    public static boolean verify_Completeness(List<Cloud_Query.Proof_Tree> p_tree, List<Cloud_Query.Proof_SubTree> p_subtree, String[][] T1,String[][] T2, String[][] T3, int ibf_length, int rb, String[] Keylist){
        boolean flag = true;
        List<String> user_T1 = new ArrayList<>();
        List<String> user_T2_T3 = new ArrayList<>();
        List<byte[]> user_T1_Hv = new ArrayList<>();
        List<byte[]> user_T2_T3_Hv = new ArrayList<>();

        for (int i = 0; i < T1.length; i++) {
            //  每一行都符合
            int count = 0;  //一行的列计数
            for (int j = 0; j < T1[i].length; j++) {
                //  对 trapdoor （HMAC(w,k_i) , h_k+1) 拆分
                String[] twins_value = T1[i][j].split(",");

                byte[] outbytes = toByteArray(twins_value[0]);
                BigInteger bi = new BigInteger(1, outbytes);
                int twinindex = bi.mod(BigInteger.valueOf(ibf_length)).intValue();      //  twins_id

                byte[] hkp1 = toByteArray(twins_value[1]);
                BigInteger hkp1bi = new BigInteger(1, hkp1);
                byte[] sha1bytes = MyUtil.HashFounction.H.digest(hkp1bi.xor(BigInteger.valueOf(rb)).toByteArray());  //sha1_xor rb
                int location = new BigInteger(1, sha1bytes).mod(BigInteger.valueOf(2)).intValue();//mod2

                String compute;
                if (location == 0) {
                    compute = twinindex + "|" + 1;
                } else {
                    compute = twinindex + "|" + 0;
                }

                user_T1.add(compute);
                byte[] hv = MyUtil.HashFounction.mdinstance.digest(addBytes(T1[i][j].getBytes(), Keylist[Keylist.length - 1].getBytes()));
                user_T1_Hv.add(hv);
            }
        }

        for (int i = 0; i < T2.length; i++) {
            //  每一行都符合
            int count = 0;  //一行的列计数
            for (int j = 0; j < T2[i].length; j++) {
                //  对 trapdoor （HMAC(w,k_i) , h_k+1) 拆分
                String[] twins_value = T2[i][j].split(",");

                byte[] outbytes = toByteArray(twins_value[0]);
                BigInteger bi = new BigInteger(1, outbytes);
                int twinindex = bi.mod(BigInteger.valueOf(ibf_length)).intValue();      //  twins_id

                byte[] hkp1 = toByteArray(twins_value[1]);
                BigInteger hkp1bi = new BigInteger(1, hkp1);
                byte[] sha1bytes = MyUtil.HashFounction.H.digest(hkp1bi.xor(BigInteger.valueOf(rb)).toByteArray());  //sha1_xor rb
                int location = new BigInteger(1, sha1bytes).mod(BigInteger.valueOf(2)).intValue();//mod2

                String compute;
                if (location == 0) {
                    compute = twinindex + "|" + 1;
                } else {
                    compute = twinindex + "|" + 0;
                }
                user_T2_T3.add(compute);
                byte[] hv = MyUtil.HashFounction.mdinstance.digest(addBytes(T2[i][j].getBytes(), Keylist[Keylist.length - 1].getBytes()));
                user_T2_T3_Hv.add(hv);
            }
        }

        for (int i = 0; i < T3.length; i++) {
            //  每一行都符合
            int count = 0;  //一行的列计数
            for (int j = 0; j < T3[i].length; j++) {
                //  对 trapdoor （HMAC(w,k_i) , h_k+1) 拆分
                String[] twins_value = T3[i][j].split(",");

                byte[] outbytes = toByteArray(twins_value[0]);
                BigInteger bi = new BigInteger(1, outbytes);
                int twinindex = bi.mod(BigInteger.valueOf(ibf_length)).intValue();      //  twins_id

                byte[] hkp1 = toByteArray(twins_value[1]);
                BigInteger hkp1bi = new BigInteger(1, hkp1);
                byte[] sha1bytes = MyUtil.HashFounction.H.digest(hkp1bi.xor(BigInteger.valueOf(rb)).toByteArray());  //sha1_xor rb
                int location = new BigInteger(1, sha1bytes).mod(BigInteger.valueOf(2)).intValue();//mod2

                String compute;
                if (location == 0) {
                    compute = twinindex + "|" + 1;
                } else {
                    compute = twinindex + "|" + 0;
                }
                user_T2_T3.add(compute);
                byte[] hv = MyUtil.HashFounction.mdinstance.digest(addBytes(T3[i][j].getBytes(), Keylist[Keylist.length - 1].getBytes()));
                user_T2_T3_Hv.add(hv);
            }
        }


        for (Cloud_Query.Proof_Tree proof_tree :p_tree) {
            if (SameElement(proof_tree.bits_YCS,user_T1)){
                flag = false;
                break;
            }else if (SameElement_Hv(proof_tree.HV_YCS,user_T1_Hv)){
                flag = false;
                break;
            }else {
                flag = true;
            }
        }
        if (flag == true){
            for (Cloud_Query.Proof_SubTree proof_tree :p_subtree) {
                if (proof_tree.bits_LCS == null){
                    if (SameElement_2(proof_tree.bits_TCS,user_T2_T3)){
                        flag = false;
                        break;
                    }else if (SameElement_Hv_2(proof_tree.HV_TCS,user_T2_T3_Hv)){
                        flag = false;
                        break;
                    }else {
                        flag = true;
                    }
                }else if (proof_tree.bits_TCS == null){
                    if (SameElement_2(proof_tree.bits_LCS,user_T2_T3)){
                        flag = false;
                        break;
                    }else if (SameElement_Hv_2(proof_tree.HV_LCS,user_T2_T3_Hv)){
                        flag = false;
                        break;
                    }else {
                        flag = true;
                    }
                }else {
                    if (SameElement_2(proof_tree.bits_LCS,user_T2_T3)){
                        flag = false;
                        break;
                    }else if (SameElement_Hv_2(proof_tree.HV_LCS,user_T2_T3_Hv)){
                        flag = false;
                        break;
                    }else if (SameElement_2(proof_tree.bits_TCS,user_T2_T3)){
                        flag = false;
                        break;
                    }else if (SameElement_Hv_2(proof_tree.HV_TCS,user_T2_T3_Hv)){
                        flag = false;
                        break;
                    }else {
                        flag = true;
                    }
                }



            }
        }

        return flag;
    }

    public static boolean SameElement(String[][] ycs,List<String> list){
        boolean flag = false;
        for (int i = 0; i < ycs.length; i++) {
            for (int j = 0; j < ycs[i].length; j++) {
                if (list.contains(ycs[i][j])){
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    public static boolean SameElement_Hv(byte[][][] ycs_Hv,List<byte[]> list){
        boolean flag = false;
        for (int i = 0; i < ycs_Hv.length; i++) {
            for (int j = 0; j < ycs_Hv[i].length; j++) {
                if (list.contains(ycs_Hv[i][j])){
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    public static boolean SameElement_2(String[][][] lcs,List<String> list){
        boolean flag = false;
        for (int i = 0; i < lcs.length; i++) {
            for (int j = 0; j < lcs[i].length; j++) {
                if (list.contains(lcs[i][j])){
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    public static boolean SameElement_Hv_2(byte[][][][] lcs_Hv,List<byte[]> list){
        boolean flag = false;
        for (int i = 0; i < lcs_Hv.length; i++) {
            for (int j = 0; j < lcs_Hv[i].length; j++) {
                for (int k = 0; k < lcs_Hv[i][j].length; k++) {
                    if (list.contains(lcs_Hv[i][j][k])){
                        flag = true;
                        break;
                    }
                }
            }
        }
        return flag;
    }

}
