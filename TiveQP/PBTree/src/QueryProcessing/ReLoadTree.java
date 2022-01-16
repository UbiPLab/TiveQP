package QueryProcessing;

import IndexBuilding.IndexElementEncoding;
import IndexBuilding.Owner;
import PBTree.PBTreeConstruction;
import ReadFileData.ReadFiledata;


import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class ReLoadTree {
    public String path = "F:\\TiveQP\\SecEQP\\10w_1km\\";
    public int round = 0;


    public PBTreeConstruction.PBNode BuildPBTree(String[][] nums){

        if(nums.length==0)
            return null;

        if (nums.length==1){
            PBTreeConstruction.PBNode root = new PBTreeConstruction.PBNode(nums);
            root.flag = round++;
            root.height = 1;
            return root;
        }



        int num_left = 0;
        if (nums.length%2 == 0){
            num_left = nums.length/2;
        }else {
            num_left = nums.length/2 + 1;
        }


        String[][] tree_left = new String[num_left][];
        String[][] tree_right = new String[nums.length-num_left][];

        for (int i = 0, j = nums.length - 1; i <=j ; i++,j--) {
            if (i==j) {
                tree_left[i] = nums[i];
            }else {
                tree_left[i] = nums[i];
                tree_right[j-num_left] = nums[j];
            }
        }
        PBTreeConstruction.PBNode root = new PBTreeConstruction.PBNode(nums);
        root.flag = round++;
//        root.flag = new Random().nextInt(2000000);
        root.height = (Math.ceil(Math.log(nums.length) / Math.log(2))) + 1;
        root.left = BuildPBTree(tree_left);
        root.right = BuildPBTree(tree_right);
        return root;
    }

    public long initNode(PBTreeConstruction.PBNode root , int ibf_length , String[] Keylist ) throws Exception {
        long time = 0;
        if(root==null)
            return time;

        if (root.height != 1){
            time = time + initMidNode(root,ibf_length,Keylist);
        }else {
            time = time + initLeafNode(root,ibf_length,Keylist);
        }
//        System.out.println(root.address + "is ok");
        time = time + initNode(root.left,ibf_length,Keylist);
        time = time + initNode(root.right,ibf_length,Keylist);

        return time;
    }


    public long initLeafNode(PBTreeConstruction.PBNode leafnode , int ibf_length , String[] Keylist ) throws Exception {
        long start = System.currentTimeMillis();

        leafnode.address =path+ "leafnode "+leafnode.flag+".txt";
        long end = System.currentTimeMillis();

        return end - start;
    }

    public long initMidNode(PBTreeConstruction.PBNode midnode , int ibf_length , String[] Keylist ) throws Exception {
        long start = System.currentTimeMillis();

        midnode.address =path+ "midnode "+midnode.flag+".txt";
        long end = System.currentTimeMillis();

        return end - start;
    }
}
