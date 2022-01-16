package AdvanceTest;

import PBTree.PBTreeConstruction;
import QueryProcessing.Cloud_Query;
import QueryProcessing.ReLoadTree_Query;
import QueryProcessing.TrapdoorCompute;
import ResultVerification.Completeness;
import ResultVerification.Correctness;
import com.carrotsearch.sizeof.RamUsageEstimator;

import java.util.ArrayList;
import java.util.List;

import static ReadFileData.ReadFiledata.readArray_String;

public class Test10w {
    public static void main(String[] args) throws Exception {
        String fileName ="E:\\Gao\\0TiveQP\\DataSet\\Type\\ReDo\\2w_random.txt";

        String[][] dataSet = readArray_String(fileName);

        System.out.println("Read File ok");
//        MyUtil.Show.ShowMatrix(dataSet);

        System.out.println();
        System.out.println("IBF setting:");
        int ibf_length = 200000;
        System.out.println("ibf_length: "+ibf_length);

        System.out.print("KeyList[]:  ");
        //  ,"6518873307787549"
        String[] Keylist = { "2938879577741549","8729598049525437","8418086888563864","0128636306393258","2942091695121238","6518873307787549"};
        MyUtil.Show.showString_list(Keylist);
//        System.out.print("Random Number:  ");
//        int randNumber = 235648;
//        System.out.println(randNumber);

        System.out.println();
        System.out.println("TiveTreeConstruction:");
        System.out.println("建树：");

        //  建树
        long c_start = System.currentTimeMillis();
        PBTreeConstruction.PBNode root = new ReLoadTree_Query().BuildPBTree(dataSet);
        long c_end = System.currentTimeMillis();
        System.out.println("建树 ok : "+(c_end-c_start));

        long time = new ReLoadTree_Query().initNode(root,ibf_length,Keylist);

        System.out.println(root.address);

        String[] op_10 = {
               // 2w 36 - 978
                "Restaurants**ATLANTA**33.846335**-84.3635778**7**0**21**0",
        //  2w 1180 - 11178
        "Fast Food**AUSTIN**30.2795878**-97.806248**10**0**22**0",

       // 4w 10348 - 38216
        "Bars**COLUMBUS**39.938944**-83.01342**2**0**23**30",

       // 6w 21 - 48533
        "Dentists**CAMBRIDGE**42.3959744**-71.1283424**8**0**17**30",

       // 6w 22648 - 109610
        "Sushi Bars**RICHMOND**49.182433**-123.13389**17**0**23**0",

        //8w 53800 - 147330
        "Auto Parts & Supplies**RICHMOND**49.1944273**-123.0765504**7**30**18**0",

        //  10w 25925 - 161249
        "Italian**RICHMOND**49.1967507119**-123.1401739116**11**0**21**0"
        };

        String[] user_input = {
                // 2w 36 - 978
                "Restaurants**ATLANTA**33.846335**-84.3635778**21**11",
                //  2w 1180 - 11178
                "Fast Food**AUSTIN**30.2795878**-97.806248**5**11",

                // 4w 10348 - 38216
                "Bars**COLUMBUS**39.938944**-83.01342**7**11",

                // 6w 21 - 48533
                "Dentists**CAMBRIDGE**42.3959744**-71.1283424**19**11",

                // 6w 22648 - 109610
                "Sushi Bars**RICHMOND**49.182433**-123.13389**19**11",

                //8w 53800 - 147330
                "Auto Parts & Supplies**RICHMOND**49.1944273**-123.0765504**12**11",

                //  10w 25925 - 161249
                "Italian**RICHMOND**49.1967507119**-123.1401739116**12**11"
        };

//        String[] op_10 ={"Restaurants**ATLANTA**33.846335**-84.3635778**7**0**21**0",
//                "Fast Food**AUSTIN**30.2795878**-97.806248**10**0**22**0",
//                "Japanese**PORTLAND**45.4376440145**-122.7578334417**11**0**21**0",
//                "Pets**AUSTIN**30.442419**-97.769657**9**0**18**0",
//                "Automotive**AUSTIN**30.372847**-97.728096**10**0**19**0",
//                "Fast Food**AUSTIN**30.2795878**-97.806248**10**0**22**0",
//                "Accessories**PORTLAND**45.5200296**-122.6824882**11**0**19**0",
//                "Local Services**CAMBRIDGE**42.3718905**-71.1153984**11**0**19**0",
//                "Venues & Event Spaces**PORTLAND**45.5225117**-122.6707498**6**0**15**0",
//                "Chicken Wings**PORTLAND**45.590371**-122.75528**11**0**19**0"};
//
        String[][] eve = new String[op_10.length][8];
        for (int i = 0; i < op_10.length; i++) {
            eve[i] = op_10[i].split("\\*\\*");
        }
////
////        MyUtil.Show.ShowMatrix(eve);
        int choose = 0;
        int open_hour = 21;
        int open_min = 11;
//        String[] user_input = {"Restaurants**ATLANTA**33.846335**-84.3635778**17**11",
//                "Fast Food**AUSTIN**30.2795878**-97.806248**17**11",
//                "Japanese**PORTLAND**45.4376440145**-122.7578334417**17**11",
//                "Pets**AUSTIN**30.442419**-97.769657**17**11",
//                "Automotive**AUSTIN**30.372847**-97.728096**17**11",
//                "Fast Food**AUSTIN**30.2795878**-97.806248**17**11",
//                "Accessories**PORTLAND**45.5200296**-122.6824882**17**11",
//                "Local Services**CAMBRIDGE**42.3718905**-71.1153984**17**11",
//                "Venues & Event Spaces**PORTLAND**45.5225117**-122.6707498**17**11",
//                "Chicken Wings**PORTLAND**45.590371**-122.75528**17**11"
//        };

//        String[][] T1 = new TrapdoorCompute().T1(type[0],Keylist,randNumber);
////        System.out.println("T1 :");
////        MyUtil.Show.ShowMatrix(T1);
//
//        String[][] T2 = new TrapdoorCompute().T2(city[0],lat[0],lng[0],Keylist,randNumber);
////        System.out.println("T2 :");
////        MyUtil.Show.ShowMatrix(T2);
//
//        String[][] T3 = new TrapdoorCompute().T3(open_hour[0],open_min,Keylist,randNumber);
////        System.out.println("T3 :");
////        MyUtil.Show.ShowMatrix(T3);

        int[] num_k ={1,5,10,15,20};


        String[][] T1 = new TrapdoorCompute().T1(eve[choose][0], Keylist);
//        System.out.println("T1 :");
//        MyUtil.Show.ShowMatrix(T1);

        String[][] T2 = new TrapdoorCompute().T2(eve[choose][1], Double.parseDouble(eve[choose][2]), Double.parseDouble(eve[choose][3]), Keylist);
//        System.out.println("T2 :");
//        MyUtil.Show.ShowMatrix(T2);

        String[][] T3 = new TrapdoorCompute().T3(open_hour, open_min, Keylist);
        //        System.out.println("T3 :");
//        MyUtil.Show.ShowMatrix(T3);

//        for (int i = 0; i < num_k.length; i++) {
        for (int i = 0; i < 1; i++) {
            System.out.println("k = " + num_k[2]);
            long row_query = 0;
            long row_proof = 0;
            long row_vrifiy = 0;
            for (int j = 0; j < 1; j++) {
                List<byte[]> list_HL = new ArrayList<>();
                List<Double> list_height = new ArrayList<>();
                List<Long> time_query = new ArrayList<>();
                List<Long> time_proof = new ArrayList<>();
                List<Long> time_proof_all = new ArrayList<>();
                List<String> result = new ArrayList<>();
                List<Cloud_Query.Proof_MT> proof_mts = new ArrayList<>();
                List<Cloud_Query.Proof_UMT> proof_umts = new ArrayList<>();
                List<Cloud_Query.Proof_Mid_ALL> proof_mid_alls = new ArrayList<>();
                List<Cloud_Query.Proof_UNN> proof_unns = new ArrayList<>();
                System.out.println("开始 查询");
                new Cloud_Query().Query_Tree(root, T1, T2, T3, ibf_length, time_query,time_proof,time_proof_all, result, num_k[2],list_HL,list_height,proof_mts,proof_umts,proof_mid_alls,proof_unns);
                System.out.println("查询结束");
                long ttt = 0;
                for (long r : time_query) {
                    ttt = ttt + r;
                }
                System.out.println("time_query = " + (ttt));
                long ttt_p = 0;
                for (long r : time_proof) {
                    ttt_p = ttt_p + r;
                }

                System.out.println("time_proof = " + ( ttt_p));
//                System.out.println("time_proof = " + ttt_f);

                System.out.println("Query result :");
                System.out.println("满足条件个数：" + result.size());
                for (String s:result
                ) {
                    System.out.println(s);
                }
                List<Long> verifiy_IO = new ArrayList<>();
                long start_verifiy = System.currentTimeMillis();
                System.out.println("Verification :");
                boolean correct_decrypt = Correctness.verify_Correctness_Decrypt(proof_mts,user_input[choose]);
                System.out.println("correct_decrypt : " + correct_decrypt);
                long hv = System.currentTimeMillis();
                boolean correct_HV = Correctness.verify_Correctness_HV(list_HL,list_height,root.HL,root.height);
                long hv_e = System.currentTimeMillis();
                System.out.println("correct_HV : " + correct_HV);
                boolean complete = Completeness.verify_Completeness(proof_mid_alls,proof_mts,proof_umts,ibf_length,verifiy_IO);
                System.out.println("completeness : " + complete);
                boolean complete_path = Completeness.verify_Completeness_Path(proof_mid_alls,proof_mts,proof_umts,T1,T2,T3);
                System.out.println("complete_path : "+ complete_path);
                long end_verify = System.currentTimeMillis();
                long t_IO = 0;
                for (long r : verifiy_IO) {
                    t_IO = t_IO + r;
                }
                System.out.println("Verification time = "+ (end_verify-start_verifiy-t_IO));
//                System.out.println("HV ：" + (hv_e-hv));
                System.out.println("proof size ：");
//                System.out.println( RamUsageEstimator.sizeOf(proof_umts)+RamUsageEstimator.sizeOf(proof_mid_alls)+RamUsageEstimator.sizeOf(proof_mts)+RamUsageEstimator.sizeOf(proof_unns)+RamUsageEstimator.sizeOf(result)+RamUsageEstimator.sizeOf(list_HL)+RamUsageEstimator.sizeOf(list_height));
                System.out.println( RamUsageEstimator.sizeOf(proof_umts)+RamUsageEstimator.sizeOf(proof_mts));

//                int mid_size = 0;
//                for (Cloud_Query.Proof_Mid_ALL mid:proof_mid_alls) {
//                    mid_size = mid_size + mid.mid_T1.M_Q.length + mid.mid_T2.M_Q.length + mid.mid_T3.M_Q.length;
//                }
//                int unn_size = 0;
//                for (Cloud_Query.Proof_UNN unn:proof_unns) {
//                    unn_size = unn_size + unn.M_Q_T1.length + unn.M_Q_T2.length + unn.M_Q_T3.length;
//                }

                System.out.println((proof_mts.size()*3+proof_mts.size()*3)*(48+37)*5 +RamUsageEstimator.sizeOf(list_HL)+RamUsageEstimator.sizeOf(list_height));
//                System.out.println((mid_size+unn_size+proof_mts.size()*3+proof_mts.size()*3)*(48+37)*5);
                System.out.println("randnumer : " +(proof_mid_alls.size()+proof_mts.size()+proof_umts.size()+proof_unns.size()));
                row_query = row_query + ttt;
                row_proof = row_proof + ttt_p;
                row_vrifiy = row_vrifiy + (end_verify-start_verifiy-t_IO);
            }

            System.out.println();
            System.out.println("row_query = "+row_query);
            System.out.println("row_proof = "+row_proof);
            System.out.println("row_vrifiy = "+row_vrifiy);

        }

    }
}
