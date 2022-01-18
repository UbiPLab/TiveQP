package QueryProcessing;

import Tree.RTree;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Cloud_Query {

    public int check_time = 0;

    public void Query_Tree(RTree.RNode root , String[] T1, String[] T2, String[] T3,
                           List<Long> time_query, int num_k) throws Exception {
//        long time = 0;
        if (check_time>=num_k){
            return;
        }else {
            if (root == null){
//                System.out.println("当前节点为空");
            }else if (Query_T1(T1,root,time_query) == true){

                    if (Query_T2_T3(T2,T3,root,time_query) == true) {
                        if (root.height == 1){
//                        System.out.println("当前节点满足："+root.address+" MLN");
                            check_time++;
                            root.showData();
//                            System.out.println();
                        }

                        Query_Tree(root.left, T1, T2, T3, time_query, num_k);
                        Query_Tree(root.right, T1, T2, T3, time_query, num_k);
                    }
                }else {
                    return;
                }

            }

    }


    public static boolean Query_T1(String[] Trapdoor, RTree.RNode root,
                                   List<Long> time_query) throws NoSuchAlgorithmException {
        long start = System.currentTimeMillis();
        boolean flag = false;
        List<String> trap = new ArrayList<>();
        for (int i = 0; i < Trapdoor.length; i++) {
            trap.add(Trapdoor[i]);
        }
        int count = 0;
        if (root.height != 1){
            for (int i = 0; i < root.type.length; i++) {
                if (trap.contains(root.type[i])){
                    count++;
                    break;
                }
            }
            if (count > 0){
                flag = true;
            }
        }else {
            for (int i = 0; i < root.type.length; i++) {
                if (trap.contains(root.type[i])){
                    count++;
                }
            }
            if (count == trap.size()){
                flag = true;
            }
        }

        long end = System.currentTimeMillis();
        time_query.add(end-start);
        return flag;
    }

    public static boolean Query_T2_T3(String[] T2, String[] T3, RTree.RNode root,
                                      List<Long> time_query) throws NoSuchAlgorithmException {
        long start = System.currentTimeMillis();
        boolean flag = false;
        List<String> trap_t2 = new ArrayList<>();
        for (int i = 0; i < T2.length; i++) {
            trap_t2.add(T2[i]);
        }

        int count = 0;
        for (int i = 0; i < root.location.length; i++) {
            if (trap_t2.contains(root.location[i])){
                count++;
                break;
            }

        }
        if (count > 0){
            flag = true;
        }
        if (flag==true){
            List<String> trap_t3 = new ArrayList<>();
            for (int i = 0; i < T3.length; i++) {
                trap_t3.add(T3[i]);
            }
            int time = 0;
            for (int i = 0; i < root.Time.length; i++) {
                if (trap_t3.contains(root.Time[i])) {
                    time++;
                    break;
                }
            }
            if (time > 0){
                flag =true;
            }else {
                flag = false;
            }

        }
        long end = System.currentTimeMillis();
        time_query.add(end-start);
        return flag;
    }


}
