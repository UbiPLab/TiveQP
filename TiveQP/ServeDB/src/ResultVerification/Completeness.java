package ResultVerification;

import PBTree.PBTreeConstruction;
import QueryProcessing.Cloud_Query;
import ReadFileData.ReadFiledata;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static PBTree.PBTreeConstruction.toPrimitives;
import static QueryProcessing.Cloud_Query.toByteArray;
import static QueryProcessing.TrapdoorCompute.toHexString;
import static ResultVerification.Correctness.addBytes;

public class Completeness {
    public static int segment_len = 200;
    public static boolean verify_Completeness(List<Cloud_Query.Proof_Mid_ALL> proof_mids, List<Cloud_Query.Proof_MT> proof_mts, List<Cloud_Query.Proof_UMT> proof_umts, int ibf_length, List<Long> time_proof) throws Exception {
        boolean flag = true;

        //  验证 bloomfilter
        //  HMAC
//        for (Cloud_Query.Proof_Mid_ALL mid : proof_mids) {
//
//            boolean flag_T1 = Verified_MID(mid.mid_T1.M_Q, mid.mid_T1, ibf_length, time_proof);
////            System.out.println(" mid flag_T1 : "+flag_T1);
//            if (flag_T1 == false) {
//                return false;
//            } else {
//                boolean flag_T2 = Verified_MID(mid.mid_T2.M_Q, mid.mid_T2, ibf_length, time_proof);
////                System.out.println("flag_T2 : "+flag_T2);
//                if (flag_T2 == false) {
//                    return false;
//                } else {
//                    boolean flag_T3 = Verified_MID(mid.mid_T3.M_Q, mid.mid_T3, ibf_length, time_proof);
////                    System.out.println("flag_T3 : "+flag_T3);
//                    if (flag_T3 == false) {
//                        return false;
//                    } else {
//                        flag = true;
//                    }
//                }
//            }
//        }

        for (Cloud_Query.Proof_MT mt : proof_mts) {
            boolean flag_T1 = Verified_MT(mt.M_Q_T1, mt, ibf_length, time_proof);
//            System.out.println("mt flag_T1 : "+flag_T1);
            if (flag_T1 == false) {
                return false;
            } else {
                boolean flag_T2 = Verified_MT(mt.M_Q_T2, mt, ibf_length, time_proof);
//                System.out.println("flag_T2 : "+flag_T2);
                if (flag_T2 == false) {
                    return false;
                } else {
                    boolean flag_T3 = Verified_MT(mt.M_Q_T3, mt, ibf_length, time_proof);
//                    System.out.println("flag_T3 : "+flag_T3);
                    if (flag_T3 == false) {
                        return false;
                    } else {
                        flag = true;
                    }
                }
            }
        }

        for (Cloud_Query.Proof_UMT umt : proof_umts) {
            boolean flag_T1 = Verified_UMT(umt.M_Q_T1, umt, ibf_length, time_proof);
//            System.out.println("umt flag_T1 : "+flag_T1);
            if (flag_T1 == false) {
                return false;
            } else {
                boolean flag_T2 = Verified_UMT(umt.M_Q_T2, umt, ibf_length, time_proof);
//                System.out.println("flag_T2 : "+flag_T2);
                if (flag_T2 == false) {
                    return false;
                } else {
                    boolean flag_T3 = Verified_UMT(umt.M_Q_T3, umt, ibf_length, time_proof);
//                    System.out.println("flag_T3 : "+flag_T3);
                    if (flag_T3 == false) {
                        return false;
                    } else {
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }

    public static boolean verify_Completeness_Path(List<Cloud_Query.Proof_Mid_ALL> proof_mids, List<Cloud_Query.Proof_MT> proof_mts, List<Cloud_Query.Proof_UMT> proof_umts,String[][] T1,String[][] T2, String[][] T3) throws Exception {

        int count_T1 = 0;
        int count_T2 = 0;
        int count_T3 = 0;

        for (Cloud_Query.Proof_MT mt : proof_mts) {
            count_T1= count_T1 + mt.mt_T1.length;
            count_T2= count_T2 + mt.mt_T2.length;
            count_T3= count_T3 + mt.mt_T3.length;
        }
        for (Cloud_Query.Proof_UMT umt : proof_umts) {
            count_T1= count_T1 + umt.umt_T1.length;
            count_T2= count_T2 + umt.umt_T2.length;
            count_T3= count_T3 + umt.umt_T3.length;
        }
        for (Cloud_Query.Proof_Mid_ALL mid : proof_mids) {
            count_T1 = count_T1 + mid.mid_T1.umt.length;
            count_T2 = count_T2 + mid.mid_T2.umt.length;
            count_T3 = count_T3 + mid.mid_T3.umt.length;
        }
        if ((count_T1 >= T1.length)&&(count_T2 >= T2.length)&&(count_T3 >= T3.length)){
            return true;
        }else {
            return false;
        }
    }




        //  验证UMT，MT


        //  重溯 Path

//        for (Cloud_Query.Proof_Tree proof_tree :proof_mids) {
//            if (SameElement(proof_tree.bits_YCS,user_T1)){
//                flag = false;
//                break;
//            }else if (SameElement_Hv(proof_tree.HV_YCS,user_T1_Hv)){
//                flag = false;
//                break;
//            }else {
//                flag = true;
//            }
//        }
//        if (flag == true){
//            for (Cloud_Query.Proof_SubTree proof_tree :proof_mts) {
//                if (proof_tree.bits_LCS == null){
//                    if (SameElement_2(proof_tree.bits_TCS,user_T2_T3)){
//                        flag = false;
//                        break;
//                    }else if (SameElement_Hv_2(proof_tree.HV_TCS,user_T2_T3_Hv)){
//                        flag = false;
//                        break;
//                    }else {
//                        flag = true;
//                    }
//                }else if (proof_tree.bits_TCS == null){
//                    if (SameElement_2(proof_tree.bits_LCS,user_T2_T3)){
//                        flag = false;
//                        break;
//                    }else if (SameElement_Hv_2(proof_tree.HV_LCS,user_T2_T3_Hv)){
//                        flag = false;
//                        break;
//                    }else {
//                        flag = true;
//                    }
//                }else {
//                    if (SameElement_2(proof_tree.bits_LCS,user_T2_T3)){
//                        flag = false;
//                        break;
//                    }else if (SameElement_Hv_2(proof_tree.HV_LCS,user_T2_T3_Hv)){
//                        flag = false;
//                        break;
//                    }else if (SameElement_2(proof_tree.bits_TCS,user_T2_T3)){
//                        flag = false;
//                        break;
//                    }else if (SameElement_Hv_2(proof_tree.HV_TCS,user_T2_T3_Hv)){
//                        flag = false;
//                        break;
//                    }else {
//                        flag = true;
//                    }
//                }
//
//
//
//            }
//        }



    public static boolean Verified_MID(String[][] Trapdoor, Cloud_Query.Proof_Mid mid, int ibf_length , List<Long> time_proof) throws Exception {
        long start = System.currentTimeMillis();
        boolean flag = true;
        long IO = 0;
        long start_read = System.currentTimeMillis();
        //  读取节点twinlist
        Byte[] twinlist = ReadFiledata.readArray(mid.address);
        long end_read = System.currentTimeMillis();
        IO = IO + end_read - start_read;
        int match_num = 0;
        int unmatch_num = 0;
        List<Integer> match_row = new ArrayList<>();
        List<Integer> unmatch_row = new ArrayList<>();
//        byte[][][] User_HB = new byte[Trapdoor.length][Trapdoor[0].length][];

        for (int i = 0; i < Trapdoor.length; i++) {
            //  每一行都符合
            int count = 0;  //一行的列计数
            for (int j = 0; j < Trapdoor[i].length; j++) {
                //  对 trapdoor HMAC(w,k_i)
                byte[] outbytes = toByteArray(Trapdoor[i][j]);

                byte[] hkp1 = MyUtil.HashFounction.HmacSHA256Encrypt(toHexString(outbytes),String.valueOf(mid.rb));
                BigInteger bi = new BigInteger(1, hkp1);
                int twinindex = bi.mod(BigInteger.valueOf(ibf_length)).intValue();      //  twins_id

                //  比较
                if (twinlist[twinindex] == 1) {
//                    System.out.println("Tree: "+twinlist[location][twinindex]+",Trapdoor: "+ location +",twinindex :"+twinindex);
//                    int group = twinindex/ segment_len;
//                    Byte[] segment = new Byte[segment_len];
//                    for (int k = 0; k < segment.length; k++) {
//                        segment[k] = twinlist[group* segment_len +k];
//                    }
//                    User_HB[i][j] = CopyHL(MyUtil.HashFounction.mdinstance.digest(toPrimitives(segment)));
                    count++;
                }
            }
            if (count == Trapdoor[i].length) {
                //  一行都满足
                match_row.add(i);
//                proof_mid.mt[match_num] = Trapdoor[i];
                match_num++;
            }else {
                unmatch_row.add(i);
//                proof_mid.umt[unmatch_num] = Trapdoor[i];
                unmatch_num++;
            }
        }

        for (int i = 0; i < match_num; i++) {
            for (int j = 0; j < Trapdoor[0].length; j++) {
                int index = match_row.get(i);
//                for (int k = 0; k < User_HB[index][j].length; k++) {
//                    if (User_HB[index][j][k] != mid.mt_HB[i][j][k]){
//                        flag = false;
//                    }
//                }
            }
        }

        for (int i = 0; i < unmatch_num; i++) {
            for (int j = 0; j < Trapdoor[0].length; j++) {
                int index = unmatch_row.get(i);
//                for (int k = 0; k < User_HB[index][j].length; k++) {
//                    if (User_HB[index][j][k] != mid.umt_HB[i][j][k]){
//                        flag = false;
//                    }
//                }
            }
        }
        long end  =System.currentTimeMillis();
        time_proof.add(IO);
        return flag;
    }

    public static boolean
    Verified_MT(String[][] Trapdoor, Cloud_Query.Proof_MT mt, int ibf_length , List<Long> time_proof) throws Exception {
        long start = System.currentTimeMillis();
        boolean flag = true;
        long IO = 0;
        long start_read = System.currentTimeMillis();
        //  读取节点twinlist
        Byte[] twinlist = ReadFiledata.readArray(mt.address);
        long end_read = System.currentTimeMillis();
        IO = IO + end_read - start_read;

        for (int i = 0; i < Trapdoor.length; i++) {
            //  每一行都符合
            int count = 0;  //一行的列计数
            for (int j = 0; j < Trapdoor[i].length; j++) {
                //  对 trapdoor HMAC(w,k_i)
                byte[] outbytes = toByteArray(Trapdoor[i][j]);

                byte[] hkp1 = MyUtil.HashFounction.HmacSHA256Encrypt(toHexString(outbytes),String.valueOf(mt.rb));
                BigInteger bi = new BigInteger(1, hkp1);
                int twinindex = bi.mod(BigInteger.valueOf(ibf_length)).intValue();      //  twins_id

                //  比较
                if (twinlist[twinindex] == 1) {
//                    System.out.println("Tree: "+twinlist[location][twinindex]+",Trapdoor: "+ location +",twinindex :"+twinindex);
                    count++;
                }
            }
        }
        long s = System.currentTimeMillis();
        int group = ibf_length/ segment_len;
        byte[][] User_HB = new byte[group][];
        for (int i = 0; i < group; i++) {
            Byte[] segment = new Byte[segment_len];
            for (int j = 0; j < segment.length; j++) {
                segment[j] = twinlist[i* segment_len +j];
            }
            User_HB[i] = MyUtil.HashFounction.mdinstance.digest(toPrimitives(segment));
        }
        long e = System.currentTimeMillis();
        IO = IO + (e - s);
//        for (int i = 0; i < User_HB.length; i++) {
//            for (int j = 0; j < User_HB[i].length; j++) {
//                if (User_HB[i][j] == mt.HB[i][j]){
//                    return true;
//                }
//            }
//        }

//        for (int i = 0; i < User_HB.length; i++) {
//            for (int j = 0; j < User_HB[i].length; j++) {
//                if (User_HB[i][j] != mt.HB[i][j]){
//                    return false;
//                }
//            }
//        }

        long end  =System.currentTimeMillis();
        time_proof.add(IO);
        return flag;
    }

    public static boolean Verified_UMT(String[][] Trapdoor, Cloud_Query.Proof_UMT umt, int ibf_length , List<Long> time_proof) throws Exception {
        long start = System.currentTimeMillis();
        boolean flag = true;
        long IO = 0;
        long start_read = System.currentTimeMillis();
        //  读取节点twinlist
        Byte[] twinlist = ReadFiledata.readArray(umt.address);
        long end_read = System.currentTimeMillis();
        IO = IO + end_read - start_read;

        for (int i = 0; i < Trapdoor.length; i++) {
            //  每一行都符合
            int count = 0;  //一行的列计数
            for (int j = 0; j < Trapdoor[i].length; j++) {
                //  对 trapdoor HMAC(w,k_i)
                byte[] outbytes = toByteArray(Trapdoor[i][j]);

                byte[] hkp1 = MyUtil.HashFounction.HmacSHA256Encrypt(toHexString(outbytes),String.valueOf(umt.rb));
                BigInteger bi = new BigInteger(1, hkp1);
                int twinindex = bi.mod(BigInteger.valueOf(ibf_length)).intValue();      //  twins_id

                //  比较
                if (twinlist[twinindex] == 1) {
//                    System.out.println("Tree: "+twinlist[location][twinindex]+",Trapdoor: "+ location +",twinindex :"+twinindex);
                    count++;
                }
            }

        }
        long s = System.currentTimeMillis();
        int group = ibf_length/ segment_len;
        byte[][] User_HB = new byte[group][];
        for (int i = 0; i < group; i++) {
            Byte[] segment = new Byte[segment_len];
            for (int j = 0; j < segment.length; j++) {
                segment[j] = twinlist[i* segment_len +j];
            }
            User_HB[i] = MyUtil.HashFounction.mdinstance.digest(toPrimitives(segment));
        }
        long e = System.currentTimeMillis();
        IO = IO + (e - s);
//        for (int i = 0; i < User_HB.length; i++) {
//            for (int j = 0; j < User_HB[i].length; j++) {
//                if (User_HB[i][j] == umt.HB[i][j]){
//                    return true;
//                }
//            }
//        }
//        for (int i = 0; i < User_HB.length; i++) {
//            for (int j = 0; j < User_HB[i].length; j++) {
//                if (User_HB[i][j] != umt.HB[i][j]){
//                    return false;
//                }
//            }
//        }

        long end  =System.currentTimeMillis();
        time_proof.add(IO);
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

    public static byte[] CopyHL(byte[] HL){
        byte[] result = new byte[HL.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = HL[i];
        }
        return result;
    }

}
