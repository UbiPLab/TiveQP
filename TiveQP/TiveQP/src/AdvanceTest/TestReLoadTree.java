package AdvanceTest;

import QueryProcessing.Cloud_Query;
import QueryProcessing.ReLoadTree;
import QueryProcessing.ReloadNode;
import QueryProcessing.TrapdoorCompute;
import ResultVerification.Completeness;
import ResultVerification.Correctness;
import TiveTree.Construction;
import com.carrotsearch.sizeof.RamUsageEstimator;

import java.util.ArrayList;
import java.util.List;

import static ReadFileData.ReadFiledata.readArray_String;

public class TestReLoadTree {
    public static void main(String[] args) throws Exception {
        String fileName ="E:\\Gao\\0TiveQP\\DataSet\\Type\\10w.txt";

        String[][] dataSet = readArray_String(fileName);

        System.out.println("Read File ok");
//        MyUtil.Show.ShowMatrix(dataSet);

        System.out.println();
        System.out.println("IBF setting:");
        int ibf_length = 200000;
        System.out.println("ibf_length: "+ibf_length);

        System.out.print("KeyList[]:  ");
        String[] Keylist = { "2938879577741549","8729598049525437","8418086888563864","0128636306393258","2942091695121238","6518873307787549"};
        MyUtil.Show.showString_list(Keylist);
        System.out.print("Random Number:  ");
        int randNumber = 235648;
        System.out.println(randNumber);


        System.out.println();
        System.out.println("TiveTreeConstruction:");
        System.out.println("建树：");

        //  建树
        long c_start = System.currentTimeMillis();
        Construction.TiveTreeNode root = new ReloadNode().BuildTiveSubTree(dataSet);
        long c_end = System.currentTimeMillis();
        System.out.println("建树 ok : "+(c_end-c_start));
//        System.out.println(RamUsageEstimator.sizeOf(root));

        //  初始化节点
        List<Long> sizeofCS = new ArrayList<>();

        long time = new ReloadNode().initNode(root,ibf_length,Keylist,randNumber);
        long init_end = System.currentTimeMillis();
//        long size = 0;
//        for (long r : sizeofCS) {
//            size = size + r;
//        }
//        System.out.println("size:"+ size);

        System.out.println("TiveTree is ok");
        System.out.println(init_end - c_end);
        System.out.println(time);
//        long all_size = RamUsageEstimator.sizeOf(root);
//        System.out.println("总大小:"+ all_size);
//        System.out.println("树大小:" + (all_size-size));
//
        System.out.println(root.address);

        String[] op_10 = {
                //  2w
                "Restaurants**ATLANTA**33.846335**-84.3635778**7**0**21**0",
                "Fast Food**AUSTIN**30.2795878**-97.806248**10**0**22**0",
                // 4w
                "Bars**COLUMBUS**39.938944**-83.01342**2**0**23**30",
                // 6w
                "Dentists**CAMBRIDGE**42.3959744**-71.1283424**8**0**17**30",
                "Sushi Bars**RICHMOND**49.182433**-123.13389**17**0**23**0",
                //  8w
                "Auto Parts & Supplies**RICHMOND**49.1944273**-123.0765504**7**30**18**0",
                //  10w
                "Italian**RICHMOND**49.1967507119**-123.1401739116**11**0**21**0"};

        String[][] eve = new String[op_10.length][8];
        for (int i = 0; i < op_10.length; i++) {
            eve[i] = op_10[i].split("\\*\\*");
        }

        String[] user_input = {//  2w
                "Restaurants**ATLANTA**33.846335**-84.3635778**12**12",
                "Fast Food**AUSTIN**30.2795878**-97.806248**12**12",
                // 4w
                "Bars**COLUMBUS**39.938944**-83.01342**12**12",
                // 6w
                "Dentists**CAMBRIDGE**42.3959744**-71.1283424**12**12",
//                "Sushi Bars**RICHMOND**49.182433**-123.13389**17**0**23**0",
                //  8w
                "Auto Parts & Supplies**RICHMOND**49.1944273**-123.0765504**12**12",
                //  10w
                "Italian**RICHMOND**49.1967507119**-123.1401739116**12**12"
        };

//        String[][] T1 = new Trap().T1(type[0],Keylist,randNumber);
////        System.out.println("T1 :");
////        MyUtil.Show.ShowMatrix(T1);
//
//        String[][] T2 = new Trap().T2(city[0],lat[0],lng[0],Keylist,randNumber);
////        System.out.println("T2 :");
////        MyUtil.Show.ShowMatrix(T2);
//
//        String[][] T3 = new Trap().T3(open_hour[0],open_min,Keylist,randNumber);
////        System.out.println("T3 :");
////        MyUtil.Show.ShowMatrix(T3);
        int choose = 0;
        int open_hour = 12;
        int open_min = 12;

        int[] num_k ={1,5,10,15,20};

        int check_time = 50;


//        for (int i = 0; i < num_k.length; i++) {
        for (int i = 0; i < 1; i++) {
            System.out.println("k = "+num_k[2]);
            System.out.println("eve = "+op_10[0]);
            long row_query = 0;
            long row_proof = 0;
            long row_vrifiy = 0;
            for (int j = 0; j < 50; j++) {

                String[][] T1 = new TrapdoorCompute().T1(eve[choose][0],Keylist,randNumber);
//        System.out.println("T1 :");
//        MyUtil.Show.ShowMatrix(T1);

                String[][] T2 = new TrapdoorCompute().T2(eve[choose][1],Double.parseDouble(eve[choose][2]),Double.parseDouble(eve[choose][3]),Keylist,randNumber);
//        System.out.println("T2 :");
//        MyUtil.Show.ShowMatrix(T2);

                String[][] T3 = new TrapdoorCompute().T3(open_hour,open_min,Keylist,randNumber);
//        System.out.println("T3 :");
//        MyUtil.Show.ShowMatrix(T3);
                List<Cloud_Query.Proof_Tree> proof_tree_list = new ArrayList<>();
                List<Cloud_Query.Proof_SubTree> proof_subTree_list = new ArrayList<>();
                List<Cloud_Query.Proof_Result> result = new ArrayList<>();
                List<Cloud_Query.Proof_UNN> proof_unns = new ArrayList<>();
                List<Long> time_query = new ArrayList<>();
                List<Long> time_proof = new ArrayList<>();
                List<byte[]> HV_list = new ArrayList<>();
                List<Double> list_height = new ArrayList<>();
                System.out.println("开始 查询");
                new Cloud_Query().Query_Tree(root, T1, T2, T3, ibf_length, randNumber, proof_tree_list, proof_subTree_list, result, proof_unns,HV_list,list_height,time_query, time_proof, num_k[2]);
                System.out.println("查询结束");
                long ttt_f = 0;
                for (long r : time_proof) {
                    ttt_f = ttt_f + r;
                }
//                System.out.println("time_proof = " + ttt_f);

                long ttt = 0;
                for (long r : time_query) {
                    ttt = ttt + r;
                }
//                System.out.println("time_query = " + (ttt - ttt_f));
//                System.out.println("time_proof = " + ttt_f);

                System.out.println("Verification :");
                System.out.println("满足条件个数："+result.size());

//            for (Cloud_Query.Proof_Tree proof:proof_tree_list) {
//                proof.showProof();
//            }
//            for (Cloud_Query.Proof_SubTree proof:proof_subTree_list) {
//                proof.showProof();
//            }
                long v_start = System.currentTimeMillis();
                System.out.println("Correctness :");
                System.out.println(Correctness.verify_Correctness_Decrypt(result,user_input[choose]));
                System.out.println(Correctness.verify_Correctness_HV(HV_list,list_height,root.HV,root.height));

                System.out.println("Completeness : ");
                System.out.println(Completeness.verify_Completeness(proof_tree_list,proof_subTree_list,T1,T2,T3,ibf_length,randNumber,Keylist));


//                System.out.println("Verification :");
//            System.out.println(result.size());
                long v_end = System.currentTimeMillis();
                row_query = (ttt - ttt_f) + row_query;
                row_proof = (ttt_f) + row_proof;
                row_vrifiy = v_end - v_start + row_vrifiy;
                System.out.println("time_query = " + (ttt - ttt_f));
                System.out.println("time_proof = " + ttt_f);
                System.out.println("Verification 时间: " + (v_end - v_start));
                System.out.println("proof size ：");
//                System.out.println( RamUsageEstimator.sizeOf(proof_subTree_list)+RamUsageEstimator.sizeOf(proof_tree_list) - bits*32*5 + bits/8);
                int bits = Cloud_Query.bit_num(proof_subTree_list,proof_tree_list);
//                System.out.println("bits_num : " + bits);
                long bits_size = bits * 5;
                long bits_Hv = bits * 48 * 5;
                long bits_index = bits * 12 * 5;
                System.out.println(RamUsageEstimator.sizeOf(HV_list)+RamUsageEstimator.sizeOf(list_height)+RamUsageEstimator.sizeOf(result) + bits*48*5 + bits*12*5 + bits * 5);
            }
            System.out.println("row_query = "+row_query);
            System.out.println("row_proof = "+row_proof);
            System.out.println("row_vrifiy = "+row_vrifiy);


        }

        System.out.println();
    }
}
