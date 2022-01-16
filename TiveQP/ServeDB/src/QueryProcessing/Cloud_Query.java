package QueryProcessing;

import PBTree.PBTreeConstruction;
import ReadFileData.ReadFiledata;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static PBTree.PBTreeConstruction.toPrimitives;
import static QueryProcessing.TrapdoorCompute.toHexString;

public class Cloud_Query {

    public static class Proof_UMT{
        public String address;
        public double height;
        public int rb;
        public int order;
        public String[][] umt_T1;
        public String[][] umt_T2;
        public String[][] umt_T3;
        public String[][] M_Q_T1;
        public String[][] M_Q_T2;
        public String[][] M_Q_T3;
        public byte[] HL;
        public byte[][] HB;
    }

    public static class Proof_MT{
        public String address;
        public double height;
        public int rb;
        public int order;
        public String ciper;
        public String[][] mt_T1;
        public String[][] mt_T2;
        public String[][] mt_T3;
        public String[][] M_Q_T1;
        public String[][] M_Q_T2;
        public String[][] M_Q_T3;
        public byte[] HL;
        public byte[][] HB;
    }
    public static class Proof_Mid_ALL{
        public String address;
        public double height;
        public int rb;
        public int order;
        public byte[] HL;
        public Proof_Mid mid_T1;
        public Proof_Mid mid_T2;
        public Proof_Mid mid_T3;

    }
    public static class Proof_Mid{
        public String address;
        public double height;
        public int rb;
        public String[][] umt;
        public String[][] M_Q;
        public String[][] mt;
        public byte[][][] mt_HB;
        public byte[][][] umt_HB;

    }
    public static class Proof_UNN{
        public String address;
        public double height;
        public byte[] HL;
        public int rb;
        public int order;
        public String[][] M_Q_T1;
        public String[][] M_Q_T2;
        public String[][] M_Q_T3;
    }

    public int segment_len = 200;
    public int check_time = 0;
    public int query_order = -1;

//    public List<byte[]> list_HL = new ArrayList<>();
//    public List<Double> list_height = new ArrayList<>();

    public void Query_Tree(PBTreeConstruction.PBNode root , String[][] T1, String[][] T2, String[][] T3, int ibf_length ,
                           List<Long> time_query, List<Long> time_proof, List<Long> time_proof_all, List<String> result, int num_k, List<byte[]> list_HL, List<Double> list_height, List<Proof_MT> mts, List<Proof_UMT> umts,
                           List<Proof_Mid_ALL> mids, List<Proof_UNN> unns) throws Exception {
//        long time = 0;
        if (check_time>=num_k){
            long s = System.currentTimeMillis();
            query_order++;
            Proof_UNN proof_unn = new Proof_UNN();
            proof_unn.address = root.address;
            proof_unn.height = root.height;
            proof_unn.HL = root.HL;
            proof_unn.order = query_order;
            proof_unn.M_Q_T1 = CopyMatrix(T1);
            proof_unn.M_Q_T2 = CopyMatrix(T2);
            proof_unn.M_Q_T3 = CopyMatrix(T3);
            list_HL.add(root.HL);
            list_height.add(root.height);
            long e = System.currentTimeMillis();
            time_proof_all.add(e-s);
            unns.add(proof_unn);
            return;
        }else {
            if(root==null) {
//                System.out.println("当前节点为空，" + root.address);
                return;
            }
            //  从上往下查询
            //  根节点

            if ((Query_T(T1,root,ibf_length,time_query) == true)&&(Query_T(T2,root,ibf_length,time_query) == true)&&(Query_T(T3,root,ibf_length,time_query) == true)){
                    if (root.height == 1) {
                        long s = System.currentTimeMillis();
                        System.out.println("检索成功 " + root.address);
                        result.add(root.address);
                        check_time++;
                        query_order++;
                        list_HL.add(root.HL);
                        list_height.add(root.height);
                        Proof_MT proof_mt = generate_MT(T1,T2,T3,root,time_proof);
                        proof_mt.order = query_order;
                        mts.add(proof_mt);
                        long e = System.currentTimeMillis();
                        time_proof_all.add(e-s);
                    }else {
                        long s = System.currentTimeMillis();
                        query_order++;
                        Proof_Mid_ALL proof_mid_all = new Proof_Mid_ALL();
                        proof_mid_all.HL = CopyHL(root.HL);
                        proof_mid_all.order = query_order;
//                        list_HL.add(root.HL);
//                        list_height.add(root.height);
                        proof_mid_all.address = root.address;
                        proof_mid_all.height = root.height;
                        proof_mid_all.mid_T1 = generate_MID(T1, root, ibf_length, time_proof);
                        proof_mid_all.mid_T2 = generate_MID(T2, root, ibf_length, time_proof);
                        proof_mid_all.mid_T3 = generate_MID(T3, root, ibf_length, time_proof);
                        mids.add(proof_mid_all);
                        long e = System.currentTimeMillis();
//                        time_proof_all.add(e-s);
//                        System.out.println(root.address + " 继续搜索");
                        Query_Tree(root.left, proof_mid_all.mid_T1.mt, proof_mid_all.mid_T2.mt, proof_mid_all.mid_T3.mt, ibf_length, time_query, time_proof,time_proof_all,result, num_k,list_HL,list_height,mts,umts,mids,unns);
                        Query_Tree(root.right, proof_mid_all.mid_T1.mt, proof_mid_all.mid_T2.mt, proof_mid_all.mid_T3.mt, ibf_length, time_query, time_proof,time_proof_all,result, num_k,list_HL,list_height,mts,umts,mids,unns);
                    }
            }else {
                long s = System.currentTimeMillis();
                query_order++;
                list_HL.add(root.HL);
                list_height.add(root.height);
                Proof_UMT proof_umt = generate_UMT(T1,T2,T3,root,time_proof);
                proof_umt.order = query_order;
                umts.add(proof_umt);
                long e = System.currentTimeMillis();
                time_proof_all.add(e-s);
            }
        }

    }


    public static boolean Query_T(String[][] Trapdoor, PBTreeConstruction.PBNode root, int ibf_length ,List<Long> time_query) throws Exception {
        long start = System.currentTimeMillis();
        boolean flag = false;


        long IO = 0;
        long start_read = System.currentTimeMillis();
        //  读取节点twinlist
        Byte[] twinlist = ReadFiledata.readArray(root.address);
        long end_read = System.currentTimeMillis();
        IO = IO + end_read - start_read;

        int all_row = 0;
        for (int i = 0; i < Trapdoor.length; i++) {
            //  每一行都符合
            int count = 0;  //一行的列计数
            for (int j = 0; j < Trapdoor[i].length; j++) {
                //  对 trapdoor HMAC(w,k_i)
                byte[] outbytes = toByteArray(Trapdoor[i][j]);

                byte[] hkp1 = MyUtil.HashFounction.HmacSHA256Encrypt(toHexString(outbytes),String.valueOf(root.flag));
                BigInteger bi = new BigInteger(1, hkp1);
                int twinindex = bi.mod(BigInteger.valueOf(ibf_length)).intValue();      //  twins_id

                //  比较
                if (twinlist[twinindex] == 1) {
//                    System.out.println("Tree: "+twinlist[location][twinindex]+",Trapdoor: "+ location +",twinindex :"+twinindex);
                    count++;
                }
            }
            if (count == Trapdoor[i].length) {
                //  一行都满足
                flag = true;

                break;
            }
        }


        long end = System.currentTimeMillis();
        time_query.add(end - start - IO);
        return flag;
    }

    public Proof_UMT generate_UMT(String[][] Trapdoor,String[][] T2,String[][] T3, PBTreeConstruction.PBNode root ,List<Long> time_proof){
        long start = System.currentTimeMillis();
        Proof_UMT proof_umt = new Proof_UMT();
        proof_umt.address = root.address;
        proof_umt.height = root.height;
        proof_umt.M_Q_T1 = CopyMatrix(Trapdoor);
        proof_umt.umt_T1 = CopyMatrix(Trapdoor);
        proof_umt.M_Q_T2 = CopyMatrix(T2);
        proof_umt.umt_T2 = CopyMatrix(T2);
        proof_umt.M_Q_T3 = CopyMatrix(T3);
        proof_umt.umt_T3 = CopyMatrix(T3);
        proof_umt.rb = root.flag;

//        long HB_strat = System.currentTimeMillis();
//        byte[][] Hb = ReadFiledata.readArray_B(root.address_HB);
//        long HB_end = System.currentTimeMillis();

//        proof_umt.HB = CopyMatrix(Hb);
        proof_umt.HL = CopyHL(root.HL);
        long end  =System.currentTimeMillis();
        time_proof.add((end-start));
        return proof_umt;
    }



    public Proof_MT generate_MT(String[][] Trapdoor,String[][] T2,String[][] T3, PBTreeConstruction.PBNode root,List<Long> time_proof){
        long start = System.currentTimeMillis();
        Proof_MT proof_mt = new Proof_MT();
        proof_mt.address = root.address;;
        proof_mt.height = root.height;
        proof_mt.M_Q_T1 = CopyMatrix(Trapdoor);
        proof_mt.M_Q_T2 = CopyMatrix(T2);
        proof_mt.M_Q_T3 = CopyMatrix(T3);
        proof_mt.mt_T1 = CopyMatrix(Trapdoor);
        proof_mt.mt_T2 = CopyMatrix(T2);
        proof_mt.mt_T3 = CopyMatrix(T3);
        proof_mt.ciper = root.ciper;
        proof_mt.rb = root.flag;

//        long HB_strat = System.currentTimeMillis();
//        byte[][] Hb = ReadFiledata.readArray_B(root.address_HB);
//        long HB_end = System.currentTimeMillis();
//
//        proof_mt.HB = CopyMatrix(Hb);
        proof_mt.HL = CopyHL(root.HL);
        long end  =System.currentTimeMillis();
        time_proof.add((end-start));
        return proof_mt;
    }
    public Proof_Mid generate_MID(String[][] Trapdoor, PBTreeConstruction.PBNode root, int ibf_length ,List<Long> time_proof) throws Exception {
        long start = System.currentTimeMillis();
        Proof_Mid proof_mid = new Proof_Mid();
        proof_mid.address = root.address;
        proof_mid.height = root.height;

        long IO = 0;
        long start_read = System.currentTimeMillis();
        //  读取节点twinlist
        Byte[] twinlist = ReadFiledata.readArray(root.address);
        long end_read = System.currentTimeMillis();
        IO = IO + end_read - start_read;
        int match_num = 0;
        int unmatch_num = 0;
        List<Integer> match_row = new ArrayList<>();
        List<Integer> unmatch_row = new ArrayList<>();
        byte[][][] Use_HB = new byte[Trapdoor.length][Trapdoor[0].length][];

        for (int i = 0; i < Trapdoor.length; i++) {
            //  每一行都符合
            int count = 0;  //一行的列计数
            for (int j = 0; j < Trapdoor[i].length; j++) {
                //  对 trapdoor HMAC(w,k_i)
                byte[] outbytes = toByteArray(Trapdoor[i][j]);

                byte[] hkp1 = MyUtil.HashFounction.HmacSHA256Encrypt(toHexString(outbytes),String.valueOf(root.flag));
                BigInteger bi = new BigInteger(1, hkp1);
                int twinindex = bi.mod(BigInteger.valueOf(ibf_length)).intValue();      //  twins_id

                //  比较
                if (twinlist[twinindex] == 1) {
//                    System.out.println("Tree: "+twinlist[location][twinindex]+",Trapdoor: "+ location +",twinindex :"+twinindex);
                    int group = twinindex/ segment_len;
                    Byte[] segment = new Byte[segment_len];
                    for (int k = 0; k < segment.length; k++) {
                        segment[k] = twinlist[group* segment_len +k];
                    }
                    Use_HB[i][j] = CopyHL(MyUtil.HashFounction.mdinstance.digest(toPrimitives(segment)));
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

        proof_mid.mt = new String[match_num][];
        proof_mid.mt_HB = new byte[match_num][Trapdoor[0].length][];

        for (int i = 0; i < match_num; i++) {
            String[] temp = new String[Trapdoor[0].length];

            for (int j = 0; j < Trapdoor[match_row.get(i)].length; j++) {
                temp[j] = Trapdoor[match_row.get(i)][j];
                proof_mid.mt_HB[i][j] = Use_HB[i][j];
            }
            proof_mid.mt[i] = temp;

        }

        proof_mid.umt = new String[unmatch_num][];
        proof_mid.umt_HB = new byte[unmatch_num][Trapdoor[0].length][];

        for (int i = 0; i < unmatch_num; i++) {
            String[] temp = new String[Trapdoor[0].length];
//            byte[] hb = new byte[root.HB[0].length];
            for (int j = 0; j < Trapdoor[unmatch_row.get(i)].length; j++) {
                temp[j] = Trapdoor[unmatch_row.get(i)][j];
                proof_mid.umt_HB[i][j] = Use_HB[i][j];
            }
            proof_mid.umt[i] = temp;
        }

        proof_mid.M_Q = CopyMatrix(Trapdoor);

        long end  =System.currentTimeMillis();
        time_proof.add((end-start)-IO);
        return proof_mid;
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

    public String[][] CopyMatrix(String[][] trapdoor) {
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
    public byte[][] CopyMatrix(byte[][] trapdoor) {
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
    public byte[] CopyHL(byte[] HL){
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

    public static byte[][] toPrimitives_b(Byte[][] oBytes)
    {
        byte[][] bytes = new byte[oBytes.length][];

        for(int i = 0; i < oBytes.length; i++) {
            for (int j = 0; j < oBytes[i].length; j++) {
                bytes[i][j] = oBytes[i][j];
            }
        }

        return bytes;
    }
}
