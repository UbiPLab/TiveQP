package QueryProcessing;

import ReadFileData.ReadFiledata;
import TiveTree.Construction;

import java.math.BigInteger;


import java.security.NoSuchAlgorithmException;
import java.util.List;

import static QueryProcessing.Cloud_Query_all.toByteArray;

public class Cloud_Query {

    public static class Proof_Tree{
        public String address;
        public byte[] HV;
//        public String[] bits_YCS;
//        public byte[][] HV_YCS;
        public String[] bits_YCS;
        public byte[][] HV_YCS;
        public byte[] bits;
        public String w_i;

    }

    public static class Proof_SubTree{
        public String address;
        public byte[] HV;
        public String[][] bits_TCS;
        public String[][] bits_LCS;
        public byte[][][] HV_LCS;
        public byte[][][] HV_TCS;

    }

    public static class Proof_Result{
        public String address;
        public byte[] HV;
        public String ciper;
    }

    public static class Proof_UNN{
        public String address;
        public byte[] HV;
        public double height;
        public boolean subroot;
    }

    public int check_time = 0;
    public int query_order = -1;

    public void Query_Tree(Construction.TiveTreeNode root , String[][] T1, String[][] T2, String[][] T3, int ibf_length ,
                           int rb, List<Proof_Tree> p_tree, List<Proof_SubTree> p_subtree, List<Proof_Result> result, List<Proof_UNN> Proof_UNN,
                           List<byte[]> HV_list,List<Double> list_height,
                           List<Long> time_query, List<Long> time_proof, int num_k) throws Exception {
//        long time = 0;
        if (check_time >= num_k){
            long s_proof = System.currentTimeMillis();
            Proof_UNN proof_unn = new Proof_UNN();
            proof_unn.address = root.address;
            proof_unn.HV = root.HV;
            if (root.subroot == true){
                proof_unn.subroot = true;
            }else {
                proof_unn.subroot = false;
            }
            proof_unn.height = root.height;
            HV_list.add(root.HV);
            Proof_UNN.add(proof_unn);
            query_order++;
            list_height.add(root.height);

            long e_proof = System.currentTimeMillis();
            time_proof.add(e_proof-s_proof);

            return;
        }else {
            if(root==null)
                System.out.println("当前节点为空，");

            //  从上往下查询
            //  根节点         T1
            if (root.subroot == true && root.subrootleaf == false){
//                System.out.println("开始检索 "+root.address);
                if (Query_T1(T1,root,ibf_length,rb,p_tree,p_subtree,HV_list,time_query,time_proof) == true){
//                    System.out.println("检索成功 "+root.address +" PMN");
                    query_order++;

                    Query_Tree(root.left,T1,T2,T3,ibf_length,rb,p_tree,p_subtree,result,Proof_UNN, HV_list,list_height,time_query,time_proof,num_k);
                    Query_Tree(root.right,T1,T2,T3,ibf_length,rb,p_tree,p_subtree,result,Proof_UNN, HV_list,list_height,time_query,time_proof,num_k);
                }else {
//                    System.out.println("检索失败 "+root.address+" UN");
//                    long s_proof = System.currentTimeMillis();
                    query_order++;
//                    list_height.add(root.height);
//                    HV_list.add(root.HV);
//                    long e_proof = System.currentTimeMillis();
//                    time_proof.add(e_proof-s_proof);
                }
            }else{
//                System.out.println("开始检索子树 "+root.address);
                if (Query_T2_T3(T2,T3,root,ibf_length,rb,p_tree,p_subtree,result,HV_list,time_query,time_proof) == true){
                    query_order++;
                    if (root.height == 1){
//                        System.out.println("当前节点满足："+root.address+" MLN");
//                        long s_proof = System.currentTimeMillis();
                        check_time++;
//                        list_height.add(root.height);
//                        HV_list.add(root.HV);
//                        list_height.add(root.height);
//                        long e_proof = System.currentTimeMillis();
//                        time_proof.add(e_proof-s_proof);
//                        root.showData();
                    }else {
//                        System.out.println("检索成功 "+root.address +" PMN");
//                        list_height.add(root.height);
                        Query_Tree(root.left,T1,T2,T3,ibf_length,rb,p_tree,p_subtree,result,Proof_UNN, HV_list,list_height,time_query,time_proof,num_k);
                        Query_Tree(root.right,T1,T2,T3,ibf_length,rb,p_tree,p_subtree,result,Proof_UNN, HV_list,list_height,time_query,time_proof,num_k);
                    }

                }else {
//                    System.out.println("检索失败 "+root.address+" UN");
//                    long s_proof = System.currentTimeMillis();
//                    query_order++;
//                    list_height.add(root.height);
//                    HV_list.add(root.HV);
//                    list_height.add(root.height);
//                    long e_proof = System.currentTimeMillis();
//                    time_proof.add(e_proof-s_proof);
                }
            }
        }
        //  中间节点        T1

        //  子树根节点       T2 + T3

        //  子树中间节点      T2 + T3

        //  子树叶子节点      T2 + T3

    }


    public static boolean Query_T1(String[][] Trapdoor, Construction.TiveTreeNode root, int ibf_length , int rb, List<Proof_Tree> p_tree,List<Proof_SubTree> p_subtree,List<byte[]> HV_list,
                                   List<Long> time_query, List<Long> time_proof) throws NoSuchAlgorithmException {
        long start = System.currentTimeMillis();
        boolean flag = false;

        long IO = 0;
        long start_read = System.currentTimeMillis();
        //  读取节点twinlist
        Byte[][] twinlist = ReadFiledata.readArray(root.address);
        long end_read = System.currentTimeMillis();
        IO = IO + end_read-start_read;

        String[][] user = new String[Trapdoor.length][Trapdoor[0].length];

        for (int i = 0; i < Trapdoor.length; i++) {
            //  每一行都符合
            int count = 0;  //一行的列计数
            for (int j = 0; j < Trapdoor[i].length; j++) {
                //  对 trapdoor （HMAC(w,k_i) , h_k+1) 拆分
                String[] twins_value = Trapdoor[i][j].split(",");

                byte[] outbytes = toByteArray(twins_value[0]);
                BigInteger bi = new BigInteger(1, outbytes);
                int twinindex = bi.mod(BigInteger.valueOf(ibf_length)).intValue();      //  twins_id

                byte[] hkp1 = toByteArray(twins_value[1]);
                BigInteger hkp1bi = new BigInteger(1, hkp1);
                byte[] sha1bytes = MyUtil.HashFounction.H.digest(hkp1bi.xor(BigInteger.valueOf(rb)).toByteArray());  //sha1_xor rb
                int location = new BigInteger(1, sha1bytes).mod(BigInteger.valueOf(2)).intValue();//mod2

                //  比较
                if (location == 0) {
//                        twinlist[0][twinindex] = 1;
//                        twinlist[1][twinindex] = 0;
                    user[i][j] = twinindex+"|"+1;     //  bits
                     } else {
//                        twinlist[1][twinindex] = 1;
//                        twinlist[0][twinindex] = 0;
                    user[i][j] = twinindex+"|"+0;
                }


                if (twinlist[location][twinindex] == 1){
//                    System.out.println("Tree: "+twinlist[location][twinindex]+",Trapdoor: "+ location +",twinindex :"+twinindex);
                    count++;
                }
            }
            if (count == Trapdoor[i].length){
                //  一行都满足
                flag =true;
                break;
            }else {
//                long s_proof = System.currentTimeMillis();
//
//                Proof_Tree proof = new Proof_Tree();
//                proof.address = root.address;
//                proof.line = i;
//
//                // return bits and Hv
//
//                proof.bits_YCS = root.bits_YCS[i];
//                proof.HV_YCS = root.HV_YCS[i];
//
//                proof.HV = root.HV;
//
//                p_tree.add(proof);
//                long e_proof = System.currentTimeMillis();
//                time_proof.add(e_proof-s_proof);
//                break;
//                System.out.println(count+"<"+Trapdoor[i].length);
            }
        }
        if (flag == false){
            long s_proof = System.currentTimeMillis();

//            GenerateProof_T1()

            Proof_Tree proof = new Proof_Tree();
            proof.address = root.address;
//            proof.line = 0;
            // return bits and Hv
            proof.bits_YCS = CopyString(root.bits_YCS[0]);
            proof.HV_YCS = CopyMatrix(root.HV_YCS[0]);
            proof.HV = CopyHL(root.HV);
            p_tree.add(proof);
            HV_list.add(root.HV);
            long e_proof = System.currentTimeMillis();
            time_proof.add(e_proof-s_proof);
        }
        long end = System.currentTimeMillis();
        time_query.add(end-start-IO);
        return flag;
    }




    public static boolean Query_T2_T3(String[][] T2, String[][] T3,Construction.TiveTreeNode root, int ibf_length , int rb, List<Proof_Tree> p_tree,List<Proof_SubTree> p_subtree, List<Proof_Result> result,List<byte[]> HV_list,
                                      List<Long> time_query, List<Long> time_proof) throws NoSuchAlgorithmException {
        long start = System.currentTimeMillis();
        boolean flag = false;

        long IO = 0;
        long start_read = System.currentTimeMillis();
        //  读取节点twinlist
        Byte[][] twinlist = ReadFiledata.readArray(root.address);
        long end_read = System.currentTimeMillis();
        IO = IO + end_read-start_read;



        //  对 trapdoor （HMAC(w,k_i) , h_k+1) 拆分
        for (int i = 0; i < T2.length; i++) {
            int count = 0;
            for (int j = 0; j < T2[i].length; j++) {
                String[] twins_value = T2[i][j].split(",");

                byte[] outbytes = toByteArray(twins_value[0]);
                BigInteger bi = new BigInteger(1, outbytes);
                int twinindex = bi.mod(BigInteger.valueOf(ibf_length)).intValue();      //  twins_id

                byte[] hkp1 = toByteArray(twins_value[1]);
                BigInteger hkp1bi = new BigInteger(1, hkp1);
                byte[] sha1bytes = MyUtil.HashFounction.H.digest(hkp1bi.xor(BigInteger.valueOf(rb)).toByteArray());  //sha1_xor rb
                int location = new BigInteger(1, sha1bytes).mod(BigInteger.valueOf(2)).intValue();//mod2


                if (twinlist[location][twinindex] == 1){
                    count++;
                }else {
//                    long s_proof = System.currentTimeMillis();
//                    String compute;
//                    if (location == 0) {
//                        compute = twinindex + "|" + 1;
//                    } else {
//                        compute = twinindex + "|" + 0;
//                    }
                    // return bits and Hv
//                    for (int k = 0; k < root.bits_LCS.length; k++) {
//                        for (int l = 0; l < root.bits_LCS[k].length; l++) {
//                            for (int m = 0; m < root.bits_LCS[k][l].length; m++) {
//                                if (compute.equals(root.bits_LCS[k][l][m]) ){
//                                    // compute
//                                    //  byte[] HvL
//                                    bits.add(compute);
//                                    Hv.add(root.HV_LCS[k][l][m]);
//                                }
//
//                            }
//                        }
//                    }
//                    HV.add(root.HV);
//                    w_i.add(T2[i][j]);
//                    long e_proof = System.currentTimeMillis();
//                    time_proof.add(e_proof-s_proof);
//                    break;
                }
            }
            if (count == T2[i].length){
                flag =true;
//                System.out.println("T2满足");
                break;
            }else {
//                System.out.println(count+"<"+T2[i].length + " T2不满足");
//                long s_proof = System.currentTimeMillis();
//                Proof_SubTree proof = new Proof_SubTree();
//                proof.address = root.address;
////                proof.line_lcs = i;
//                proof.bits_LCS = root.bits_LCS[0];
//                proof.HV_LCS = root.HV_LCS[0];
//
////                proof.HV = root.HV;
//                long e_proof = System.currentTimeMillis();
//                time_proof.add(e_proof-s_proof);
//                break;
            }
        }
        if (flag==true){
            int time = 0;
            for (int i = 0; i < T3.length; i++) {
                int count = 0;
                for (int j = 0; j < T3[i].length; j++) {
                    String[] twins_value = T3[i][j].split(",");

                    byte[] outbytes = toByteArray(twins_value[0]);
                    BigInteger bi = new BigInteger(1, outbytes);
                    int twinindex = bi.mod(BigInteger.valueOf(ibf_length)).intValue();      //  twins_id

                    byte[] hkp1 = toByteArray(twins_value[1]);
                    BigInteger hkp1bi = new BigInteger(1, hkp1);
                    byte[] sha1bytes = MyUtil.HashFounction.H.digest(hkp1bi.xor(BigInteger.valueOf(rb)).toByteArray());  //sha1_xor rb
                    int location = new BigInteger(1, sha1bytes).mod(BigInteger.valueOf(2)).intValue();//mod2

                    if (twinlist[location][twinindex] == 1){
                        count++;
                    }else {
//                        long s_proof = System.currentTimeMillis();
//                        String compute;
//                        if (location == 0) {
//                            compute = twinindex + "|" + 1;
//                        } else {
//                            compute = twinindex + "|" + 0;
//                        }
//                        // return bits and Hv
//                        for (int k = 0; k < root.bits_TCS.length; k++) {
//                            for (int l = 0; l < root.bits_TCS[k].length; l++) {
//                                for (int m = 0; m < root.bits_TCS[k][l].length; m++) {
//                                    if (compute.equals(root.bits_TCS[k][l][m])){
//                                        // compute
//                                        //  byte[] Hv
//                                        bits.add(compute);
//                                        Hv.add(root.HV_TCS[k][l][m]);
//                                    }
//                                }
//                            }
//                        }
//                        HV.add(root.HV);
//                        w_i.add(T3[i][j]);
//                        long e_proof = System.currentTimeMillis();
//                        time_proof.add(e_proof-s_proof);
//                        break;
                    }
                }
                if (count == T3[i].length){
                    time++;
                }else {
//                    System.out.println(count+"<"+T3[i].length+" T3不满足");
//                    long s_proof = System.currentTimeMillis();
//
//                    proof.line_tcs = i;
//                    proof.bits_TCS = root.bits_TCS[i];
//                    proof.HV_TCS = root.HV_TCS[i];
//
//                    long e_proof = System.currentTimeMillis();
//                    time_proof.add(e_proof-s_proof);
//                    break;
                }
            }
            if (time == 0){
                flag = false;
                long s_proof = System.currentTimeMillis();
                Proof_SubTree proof = new Proof_SubTree();

                proof.address = root.address;
                proof.HV = CopyHL(root.HV);
//                    proof.line_lcs = i;
//                proof.bits_LCS = root.bits_LCS;
//                proof.HV_LCS = root.HV_LCS;

//                    proof.line_tcs = i;
                proof.bits_TCS = CopyMatrix(root.bits_TCS[0]);
                proof.HV_TCS = CopyMatrix(root.HV_TCS[0]);
                HV_list.add(root.HV);
                p_subtree.add(proof);
                long e_proof = System.currentTimeMillis();
                time_proof.add(e_proof-s_proof);
            }else {
                if (root.height == 1) {

                    String plaintext = root.data[0][0] + "**" + root.data[0][1] + "**" + root.data[0][2] + "**" + root.data[0][3] + "**" + root.data[0][4] + "**" + root.data[0][5] + "**" + root.data[0][6] + "**" + root.data[0][7] ;
                    String key = "2bc73dw20ebf4d46";
                    String encode = MyUtil.AESUtil.encryptIntoHexString(plaintext, key);

                    long s_proof = System.currentTimeMillis();
                    Proof_Result result_proof = new Proof_Result();
                    result_proof.address = root.address;
                    result_proof.HV = CopyHL(root.HV);
                    result_proof.ciper = encode;
                    result.add(result_proof);
                    HV_list.add(root.HV);
                    long e_proof = System.currentTimeMillis();
                    time_proof.add(e_proof-s_proof);
//                    result.add(plaintext);
                }
            }
        }else {
            long s_proof = System.currentTimeMillis();

            Proof_SubTree proof = new Proof_SubTree();

            proof.address = root.address;
            proof.HV = CopyHL(root.HV);
//                    proof.line_lcs = i;
            proof.bits_LCS = CopyMatrix(root.bits_LCS[0]);
            proof.HV_LCS = CopyMatrix(root.HV_LCS[0]);
//                    proof.line_tcs = i;
//            proof.bits_TCS = root.bits_TCS;
//            proof.HV_TCS = root.HV_TCS;
            HV_list.add(root.HV);
            p_subtree.add(proof);
            long e_proof = System.currentTimeMillis();
            time_proof.add(e_proof-s_proof);
        }
        long end = System.currentTimeMillis();
        time_query.add(end-start-IO);
        return flag;
    }

    public static void GenerateProof_T1(String[][] Trapdoor, Construction.TiveTreeNode root, int ibf_length , int rb, List<Proof_Tree> p_tree,List<Proof_SubTree> p_subtree,List<byte[]> HV_list,
                                        List<Long> time_query, List<Long> time_proof) {
        long IO = 0;
        long start_read = System.currentTimeMillis();
        //  读取节点twinlist
        Byte[][] twinlist = ReadFiledata.readArray(root.address);
        long end_read = System.currentTimeMillis();
        IO = IO + end_read-start_read;

        String[][] user = new String[Trapdoor.length][Trapdoor[0].length];

        for (int i = 0; i < Trapdoor.length; i++) {
            //  每一行都符合
            int count = 0;  //一行的列计数
            for (int j = 0; j < Trapdoor[i].length; j++) {
                //  对 trapdoor （HMAC(w,k_i) , h_k+1) 拆分
                String[] twins_value = Trapdoor[i][j].split(",");

                byte[] outbytes = toByteArray(twins_value[0]);
                BigInteger bi = new BigInteger(1, outbytes);
                int twinindex = bi.mod(BigInteger.valueOf(ibf_length)).intValue();      //  twins_id

                byte[] hkp1 = toByteArray(twins_value[1]);
                BigInteger hkp1bi = new BigInteger(1, hkp1);
                byte[] sha1bytes = MyUtil.HashFounction.H.digest(hkp1bi.xor(BigInteger.valueOf(rb)).toByteArray());  //sha1_xor rb
                int location = new BigInteger(1, sha1bytes).mod(BigInteger.valueOf(2)).intValue();//mod2

                //  比较
                if (location == 0) {
//                        twinlist[0][twinindex] = 1;
//                        twinlist[1][twinindex] = 0;
                    user[i][j] = twinindex + "|" + 1;     //  bits
                } else {
//                        twinlist[1][twinindex] = 1;
//                        twinlist[0][twinindex] = 0;
                    user[i][j] = twinindex + "|" + 0;
                }
                for (int k = 0; k < root.bits_YCS.length; k++) {
                    for (int l = 0; l < root.bits_YCS[i].length; l++) {
                        if (user[i][j] == root.bits_YCS[k][l]){
                            count++;
                        }
                    }
                }
            }
            if (count == Trapdoor[i].length){
                long s_proof = System.currentTimeMillis();
                Proof_Tree proof = new Proof_Tree();
                proof.address = root.address;
//            proof.line = 0;
                // return bits and Hv
                proof.bits_YCS = CopyString(root.bits_YCS[i]);
                proof.HV_YCS = CopyMatrix(root.HV_YCS[i]);
                proof.HV = CopyHL(root.HV);
                p_tree.add(proof);
                HV_list.add(root.HV);
                long e_proof = System.currentTimeMillis();
                time_proof.add(e_proof-s_proof);
                break;
            }
        }

    }

    public static String[][] CopyMatrix(String[][] trapdoor) {
        String[][] result = new String[trapdoor.length][];
        for (int i = 0; i < trapdoor.length; i++) {
            String[] temp = new String[trapdoor[i].length];
            for (int j = 0; j < trapdoor[i].length; j++) {
                temp[j] = trapdoor[i][j];
            }
            result[i] = temp;
        }
        return result;
    }

    public static String[][][] CopyMatrix(String[][][] trapdoor) {
        String[][][] result = new String[trapdoor.length][][];
        for (int i = 0; i < trapdoor.length; i++) {
            result[i] = CopyMatrix(trapdoor[i]);
        }
        return result;
    }

    public static byte[][] CopyMatrix(byte[][] trapdoor) {
        byte[][] result = new byte[trapdoor.length][];
        for (int i = 0; i < trapdoor.length; i++) {
            byte[] temp = new byte[trapdoor[i].length];
            for (int j = 0; j < trapdoor[i].length; j++) {
                temp[j] = trapdoor[i][j];
            }
            result[i] = temp;
        }
        return result;
    }

    public static byte[][][] CopyMatrix(byte[][][] trapdoor) {
        byte[][][] result = new byte[trapdoor.length][][];
        for (int i = 0; i < trapdoor.length; i++) {
            result[i] = CopyMatrix(trapdoor[i]);
        }
        return result;
    }

    public static byte[][][][] CopyMatrix(byte[][][][] trapdoor) {
        byte[][][][] result = new byte[trapdoor.length][][][];
        for (int i = 0; i < trapdoor.length; i++) {
            result[i] = CopyMatrix(trapdoor[i]);
        }
        return result;
    }

    public static byte[] CopyHL(byte[] HL){
        if (HL != null){
            byte[] result = new byte[HL.length];
            for (int i = 0; i < result.length; i++) {
                result[i] = HL[i];
            }
            return result;
        }else {
            return null;
        }
    }

    public static String[] CopyString(String[] arrs){
        if (arrs != null){
            String[] result = new String[arrs.length];
            for (int i = 0; i < result.length; i++) {
                result[i] = arrs[i];
            }
            return result;
        }else {
            return null;
        }
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

    public static int bit_num (List<Cloud_Query.Proof_SubTree> proof_subTree_list, List<Cloud_Query.Proof_Tree> proof_tree_list){
        int num = 0;
        for (Cloud_Query.Proof_SubTree sub:proof_subTree_list) {
            if (sub.bits_TCS == null){
                num = num+1;
            }else {
                num = num + 2;
            }
        }
        return num + proof_tree_list.size();
    }
}
