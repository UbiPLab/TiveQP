package QueryProcessing;

import IBFTree.IBFTreeConstruction;
import ReadFileData.ReadFiledata;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Cloud_Query {



    public int check_time = 0;

    public void Query_Tree(IBFTreeConstruction.IBFNode root , String[][] T1, String[][] T2, String[][] T3, int ibf_length ,
                           int rb,
                           List<Long> time_query,List<String> result, int num_k) throws Exception {
//        long time = 0;
        if (check_time>=num_k){
            return;
        }else {
            if(root==null) {

                return;
            }


            if ((Query_T(T1,root,ibf_length,rb,time_query) == true)&&(Query_T(T2,root,ibf_length,rb,time_query) == true)&&(Query_T(T3,root,ibf_length,rb,time_query) == true)){
                if (root.height == 1){
                    System.out.println("检索成功 "+root.address );
                    result.add(root.address);
                    check_time++;
                }else {
//
                }
                Query_Tree(root.left,T1,T2,T3,ibf_length,rb,time_query,result,num_k);
                Query_Tree(root.right,T1,T2,T3,ibf_length,rb,time_query,result,num_k);
            }else {
//
                return;
            }
        }

    }


    public static boolean Query_T(String[][] Trapdoor, IBFTreeConstruction.IBFNode root, int ibf_length , int rb, List<Long> time_query) throws NoSuchAlgorithmException {
        long start = System.currentTimeMillis();
        boolean flag = false;

        long IO = 0;
        long start_read = System.currentTimeMillis();
        //
        Byte[][] twinlist = ReadFiledata.readArray(root.address);
        long end_read = System.currentTimeMillis();
        IO = IO + end_read-start_read;


        for (int i = 0; i < Trapdoor.length; i++) {

            int count = 0;
            for (int j = 0; j < Trapdoor[i].length; j++) {

                String[] twins_value = Trapdoor[i][j].split(",");

                byte[] outbytes = toByteArray(twins_value[0]);
                BigInteger bi = new BigInteger(1, outbytes);
                int twinindex = bi.mod(BigInteger.valueOf(ibf_length)).intValue();

                byte[] hkp1 = toByteArray(twins_value[1]);
                BigInteger hkp1bi = new BigInteger(1, hkp1);
                byte[] sha1bytes = MyUtil.HashFounction.H.digest(hkp1bi.xor(BigInteger.valueOf(rb)).toByteArray());  //sha1_xor rb
                int location = new BigInteger(1, sha1bytes).mod(BigInteger.valueOf(2)).intValue();//mod2


                if (twinlist[location][twinindex] == 1){
//                    System.out.println("Tree: "+twinlist[location][twinindex]+",Trapdoor: "+ location +",twinindex :"+twinindex);
                    count++;
                }
            }
            if (count == Trapdoor[i].length){

                flag =true;
                break;
            }
        }
        long end = System.currentTimeMillis();
        time_query.add(end-start-IO);
        return flag;
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
