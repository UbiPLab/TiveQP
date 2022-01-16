package AdvanceTest;

import IndexBuilding.User;
import QueryProcessing.Cloud_Query;
import QueryProcessing.ReLoadTree;
import QueryProcessing.ReloadNode;
import QueryProcessing.TrapdoorCompute;
import ResultVerification.Completeness;
import ResultVerification.Correctness;
import TiveTree.Construction;
import TiveTree.Construction_Size;
import com.carrotsearch.sizeof.RamUsageEstimator;

import java.util.ArrayList;
import java.util.List;

import static ReadFileData.ReadFiledata.readArray_String;

public class Test {
    public static void main(String[] args) throws Exception {
        String fileName ="C:\\Users\\CYF\\Desktop\\Test\\2w.txt";

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
        System.out.println("Tree Construction：");

        //  建树
        long c_start = System.currentTimeMillis();
        //Construction.TiveTreeNode root = new ReloadNode().BuildTiveSubTree(dataSet);
        Construction.TiveTreeNode root = new Construction().BuildTiveSubTree(dataSet);
        long c_end = System.currentTimeMillis();
        System.out.println("Tree Construction ok : "+(c_end-c_start));
//        System.out.println(RamUsageEstimator.sizeOf(root));

        //  初始化节点
        List<Long> sizeofCS = new ArrayList<>();

        long time = new Construction().initNode(root,ibf_length,Keylist,randNumber,sizeofCS);
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


//        byte[] HV = root.HV;
//        for (int i = 0; i < HV.length; i++) {
//            System.out.print(HV[i]+" ");
//        }
//        System.out.println();


//        System.out.println("ReLoad:");
//        //  建树
//        long c_start_2 = System.currentTimeMillis();
//        ReLoadTree.TiveTreeNode root_2 = new ReLoadTree().BuildTiveSubTree(dataSet);
//        long c_end_2 = System.currentTimeMillis();
//        System.out.println("建树 ok : "+(c_end_2-c_start_2));
//        //  初始化节点
//
//        long time_2 = new ReLoadTree().initNode(root_2,ibf_length,Keylist,randNumber);
//        long init_end_2 = System.currentTimeMillis();
//        System.out.println("TiveTree is ok");
//        System.out.println(init_end_2 - c_end_2);
//        System.out.println(time_2);
//        System.out.println("大小:");
//        System.out.println(RamUsageEstimator.sizeOf(root_2));

//        System.out.println(root_2.address);
//
//        System.out.println("bits_YCS :");
//        String[][] bits_YCS = root_2.bits_YCS;
//        MyUtil.Show.ShowMatrix(bits_YCS);
//
//        System.out.println("HV_YCS :");
//
//        byte[][][] HV_YCS = root_2.HV_YCS;
//        for (int i = 0; i < HV_YCS.length; i++) {
//            for (int j = 0; j < HV_YCS[i].length; j++) {
//                for (int k = 0; k < HV_YCS[i][j].length; k++) {
//                    System.out.print(HV_YCS[i][j][k] + " ");
//                }
//                System.out.println();
//            }
//            System.out.println();
//        }
//
//        System.out.println("HV :");
//        byte[] HV_2 = root_2.HV;
//        for (int i = 0; i < HV_2.length; i++) {
//            System.out.print(HV_2[i]+" ");
//        }
//        System.out.println();

        //  Restaurants**ATLANTA**33.772758**-84.380375**11**0**21**0

        //-22 -52 110 -47 -80 8 122 104 -69 -83 -13 52 8 105 -116 -67 72 119 93 70 -37 -79 89 -91 -7 23 102 46 -108 -104 -128 105


        //  Trapdoor 构建



        //  Restaurants**ATLANTA**33.846335**-84.3635778**7**0**21**0
        //  Nightlife**BOSTON**42.3651733**-71.055268**16**0**22**0
        //  Health & Medical**VANCOUVER**49.2762964**-123.1192143**10**0**19**0
        //  Home Services**RICHMOND**49.1926413**-123.0830374**8**30**17**0
        //  American (Traditional)**CHAMBLEE**33.919491**-84.3033448**11**0**20**0
        //  Sandwiches**COLUMBUS**39.9793561**-83.0633482**9**0**15**0
        //  Breakfast & Brunch**MEDFORD**42.4223599**-71.0923733**16**0**22**0
        //  American (New)**AUSTIN**30.3986894995**-97.747419944**17**30**21**0
        //  Fashion**PORTLAND**45.5306635**-122.6888074**12**0**19**0
        //  Hair Salons**ORLANDO**28.540095**-81.372697**10**0**20**0

        String[] type = {"Restaurants","Restaurants","Nightlife","Health & Medical","Home Services","American (Traditional)","Sandwiches","Breakfast & Brunch","American (New)","Fashion","Hair Salons"};
        String[] city = {"ATLANTA","ATLANTA","BOSTON","VANCOUVER","RICHMOND","CHAMBLEE","COLUMBUS","MEDFORD","AUSTIN","PORTLAND","ORLANDO"};
        double[] lat = {33.846335,33.846335,42.3651733,49.2762964,49.1926413,33.919491,39.9793561,42.4223599,30.3986894995,45.5306635,28.540095};
        double[] lng = {-84.3635778,-84.3635778,-71.055268,-123.1192143,-123.0830374,-84.3033448,-83.0633482,-71.0923733,-97.747419944,-122.6888074,-81.372697};
        int[] open_hour = {11,11,18,12,11,15,11,19,19,15,13};
        int open_min = 11;
//        String[] user_input = {"Restaurants**ATLANTA**33.846335**-84.3635778**11**11",
//                    "Restaurants**ATLANTA**33.846335**-84.3635778**11**11",
//                    "Nightlife**BOSTON**42.3651733**-71.055268**18**11",
//                    "Health & Medical**VANCOUVER**49.2762964**-123.1192143**12**11",
//                    "Home Services**RICHMOND**49.1926413**-123.0830374**11**11",
//                    "American (Traditional)**CHAMBLEE**33.919491**-84.3033448**15**11",
//                    "Sandwiches**COLUMBUS**39.9793561**-83.0633482**11**11",
//                    "Breakfast & Brunch**MEDFORD**42.4223599**-71.0923733**19**11",
//                    "American (New)**AUSTIN**30.3986894995**-97.747419944**19**11",
//                    "Fashion**PORTLAND**45.5306635**-122.6888074**15**11",
//                    "Hair Salons**ORLANDO**28.540095**-81.372697**13**11"
//        };

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

        int[] num_k ={1,5,10,15,20};

//        for (int i = 0; i < num_k.length; i++) {
        for (int i = 0; i < 5; i++) {
            System.out.println("k = "+num_k[i]);
            System.out.println("eve = "+op_10[0]);
            for (int j = 0; j < 11; j++) {

                String[][] T1 = new TrapdoorCompute().T1(eve[0][0],Keylist,randNumber);
//        System.out.println("T1 :");
//        MyUtil.Show.ShowMatrix(T1);

                String[][] T2 = new TrapdoorCompute().T2(eve[0][1],Double.parseDouble(eve[0][2]),Double.parseDouble(eve[0][3]),Keylist,randNumber);
//        System.out.println("T2 :");
//        MyUtil.Show.ShowMatrix(T2);

                String[][] T3 = new TrapdoorCompute().T3(12,12,Keylist,randNumber);
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
                System.out.println("Query start");
                new Cloud_Query().Query_Tree(root, T1, T2, T3, ibf_length, randNumber, proof_tree_list, proof_subTree_list, result, proof_unns,HV_list,list_height,time_query, time_proof, num_k[i]);
                System.out.println("Query end");
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
                System.out.println("Result number："+result.size());

//            for (Cloud_Query.Proof_Tree proof:proof_tree_list) {
//                proof.showProof();
//            }
//            for (Cloud_Query.Proof_SubTree proof:proof_subTree_list) {
//                proof.showProof();
//            }
                long v_start = System.currentTimeMillis();
                System.out.println("Correctness :");
                System.out.println(Correctness.verify_Correctness_Decrypt(result,user_input[0]));
                System.out.println(Correctness.verify_Correctness_HV(HV_list,list_height,root.HV,root.height));

                System.out.println("Completeness : ");
                System.out.println(Completeness.verify_Completeness(proof_tree_list,proof_subTree_list,T1,T2,T3,ibf_length,randNumber,Keylist));

//                System.out.println("Verification :");
//            System.out.println(result.size());
                long v_end = System.currentTimeMillis();
                System.out.println("time_query = " + (ttt - ttt_f));
                System.out.println("time_proof = " + ttt_f);
                System.out.println("Verification time: " + (v_end - v_start));
                System.out.println("proof size ：");
                System.out.println( RamUsageEstimator.sizeOf(proof_subTree_list)+RamUsageEstimator.sizeOf(proof_tree_list)+RamUsageEstimator.sizeOf(proof_unns)+RamUsageEstimator.sizeOf(result)+RamUsageEstimator.sizeOf(HV_list)+RamUsageEstimator.sizeOf(list_height));
            }
        }

        System.out.println();






    }
}
