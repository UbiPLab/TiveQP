package AdvanceTest;

import IBFTree.IBFTreeConstruction;
import Parameter.Parameter;
import QueryProcessing.Cloud_Query;
import QueryProcessing.TrapdoorCompute;
import com.carrotsearch.sizeof.RamUsageEstimator;

import java.util.ArrayList;
import java.util.List;

import static ReadFileData.ReadFiledata.readArray_String;

public class TestIBFTree {
    public static void main(String[] args) throws Exception {

        String fileName = Parameter.fileName;

        String[][] dataSet = readArray_String(fileName);

        System.out.println("Read File ok");
//        MyUtil.Show.ShowMatrix(dataSet);

        System.out.println();
        System.out.println("IBF setting:");
        int ibf_length = 20000;
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
        IBFTreeConstruction.IBFNode root = new IBFTreeConstruction().BuildPBTree(dataSet);
        long c_end = System.currentTimeMillis();
        System.out.println("Tree Construction ok : "+(c_end-c_start));
        System.out.println(RamUsageEstimator.sizeOf(root));

        //  初始化节点

        long time = new IBFTreeConstruction().initNode(root,ibf_length,Keylist,randNumber);
        long init_end = System.currentTimeMillis();
        System.out.println("TiveTree is ok");
        System.out.println(init_end - c_end);
        System.out.println(time);
        System.out.println("Size:");
        System.out.println(RamUsageEstimator.sizeOf(root));

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
        String[] user_input = {"Restaurants**ATLANTA**33.846335**-84.3635778**11**11",
                "Restaurants**ATLANTA**33.846335**-84.3635778**11**11",
                "Nightlife**BOSTON**42.3651733**-71.055268**18**11",
                "Health & Medical**VANCOUVER**49.2762964**-123.1192143**12**11",
                "Home Services**RICHMOND**49.1926413**-123.0830374**11**11",
                "American (Traditional)**CHAMBLEE**33.919491**-84.3033448**15**11",
                "Sandwiches**COLUMBUS**39.9793561**-83.0633482**11**11",
                "Breakfast & Brunch**MEDFORD**42.4223599**-71.0923733**19**11",
                "American (New)**AUSTIN**30.3986894995**-97.747419944**19**11",
                "Fashion**PORTLAND**45.5306635**-122.6888074**15**11",
                "Hair Salons**ORLANDO**28.540095**-81.372697**13**11"
        };

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

        for (int i = 0; i < num_k.length; i++) {
            System.out.println("k = " + num_k[i]);
            for (int j = 0; j < 11; j++) {

                String[][] T1 = new TrapdoorCompute().T1(type[0], Keylist ,randNumber);
//        System.out.println("T1 :");
//        MyUtil.Show.ShowMatrix(T1);

                String[][] T2 = new TrapdoorCompute().T2(city[0], lat[0], lng[0], Keylist ,randNumber);
//        System.out.println("T2 :");
//        MyUtil.Show.ShowMatrix(T2);

                String[][] T3 = new TrapdoorCompute().T3(open_hour[0], open_min, Keylist ,randNumber);
//        System.out.println("T3 :");
//        MyUtil.Show.ShowMatrix(T3);
                List<Long> time_query = new ArrayList<>();
                List<String> result = new ArrayList<>();
                System.out.println("Query start");
                new Cloud_Query().Query_Tree(root, T1, T2, T3, ibf_length, randNumber,time_query, result,num_k[i]);
                System.out.println("Query end");
                long ttt = 0;
                for (long r : time_query) {
                    ttt = ttt + r;
                }
                System.out.println("time_query = " + (ttt));
//                System.out.println("time_proof = " + ttt_f);

                System.out.println("Verification :");
                System.out.println("Result number：" + result.size());
                for (String s:result
                ) {
                    System.out.println(s);
                }

            }

            System.out.println();


        }


    }
}
