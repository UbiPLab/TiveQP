package AdvanceTest;

import PBTree.PBTreeConstruction;
import PBTree.PBTreeConstruction_HB;
import com.carrotsearch.sizeof.RamUsageEstimator;

import static ReadFileData.ReadFiledata.readArray_String;

public class TestHB {
    public static void main(String[] args) throws Exception {
        String fileName ="E:\\Gao\\0TiveQP\\DataSet\\Type\\ReDo\\10w_random.txt";

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
        PBTreeConstruction.PBNode root = new PBTreeConstruction_HB().BuildPBTree(dataSet);
        long c_end = System.currentTimeMillis();
        System.out.println("建树 ok : "+(c_end-c_start));
//        System.out.println(RamUsageEstimator.sizeOf(root));

        //  初始化节点

        long time = new PBTreeConstruction_HB().initNode(root,ibf_length,Keylist);
        long init_end = System.currentTimeMillis();
        System.out.println("TiveTree is ok");
        System.out.println("Construction time:");
        System.out.println(init_end - c_end);
        System.out.println("initnode time:");
        System.out.println(time);
        System.out.println("大小:");
        System.out.println(RamUsageEstimator.sizeOf(root));
    }
}
