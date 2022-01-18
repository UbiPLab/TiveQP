package AdvanceTest;

import Parameter.RTree_parameters;
import QueryProcessing.Cloud_Query;
import Trapdoor.TrapdoorCompute;
import Tree.RTree;
import com.carrotsearch.sizeof.RamUsageEstimator;

import java.util.ArrayList;
import java.util.List;

import static ReadFileData.ReadFiledata.readArray_String;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/1/16 13:54
 * @Version 1.0
 */
public class Test {
    public static void main(String[] args) throws Exception {
        String fileName = RTree_parameters.fileName;

        String[][] dataSet = readArray_String(fileName);

        System.out.println("Read File ok");

        System.out.println("Construction:");
        //  Tree construction
        long c_start_2 = System.currentTimeMillis();
        RTree.RNode root = new RTree().BuildRTree(dataSet);
        long c_end_2 = System.currentTimeMillis();
        System.out.println("Tree construction ok : "+(c_end_2-c_start_2));
        //  Init node

        long time_2 = new RTree().initNode(root);
        long init_end_2 = System.currentTimeMillis();
        System.out.println("TiveTree is ok");
        System.out.println(init_end_2 - c_end_2);
        System.out.println(time_2);
        System.out.println("Size:");
        System.out.println(RamUsageEstimator.sizeOf(root));

        //  Restaurants**ATLANTA**33.7460355**-84.370798**7**0**20**0

        String type = "Restaurants";
        String city = "ATLANTA";
        double lat = 33.7460355;
        double lng = -84.370798;
        int open_hour = 12;
        int open_min = 11;

        String[] T1 = new TrapdoorCompute().T1(type);
        String[] T2 = new TrapdoorCompute().T2(city,lat,lng);
        String[] T3 = new TrapdoorCompute().T3(open_hour,open_min);

        int num_k = 10;
        int check_time = 50;
        long query_sum = 0;
        long q_start = System.currentTimeMillis();

        for (int i = 0; i < check_time; i++) {
            System.out.println("k = " + num_k);
            List<Long> time_query = new ArrayList<>();

            System.out.println("Query start");
            new Cloud_Query().Query_Tree(root, T1, T2, T3,time_query, num_k);
            System.out.println("Query end");


            long ttt = 0;
            for (long r : time_query) {
                ttt = ttt + r;
            }
            System.out.println("time_query = " + (ttt ));
            query_sum += ttt;

        }
        long q_end = System.currentTimeMillis();
        System.out.println("Sum query time: "+ query_sum);
        System.out.println("Sum query time: "+ (q_end - q_start));
    }
}
