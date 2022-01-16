package AdvanceTest;

import TiveTree.Construction;
import com.carrotsearch.sizeof.RamUsageEstimator;
import jdk.jshell.execution.Util;

import static ReadFileData.ReadFiledata.readArray_String;

public class TestConstruction {
    public static void main(String[] args) throws Exception {
        String fileName ="E:\\Gao\\0TiveQP\\DataSet\\Type\\2w.txt";

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
        Construction.TiveTreeNode root = new Construction().BuildTiveSubTree(dataSet);
        long c_end = System.currentTimeMillis();
        System.out.println("建树 ok : "+(c_end-c_start));
        //  初始化节点

//        long time = new Construction().initNode(root,ibf_length,Keylist,randNumber);
        long init_end = System.currentTimeMillis();
        System.out.println("TiveTree is ok");
        System.out.println(init_end - c_end);
//        System.out.println(time);
        System.out.println("大小:");
        System.out.println(RamUsageEstimator.sizeOf(root));
    }
}
