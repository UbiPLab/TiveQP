package QueryProcessing;

import IndexBuilding.Owner;
import PBTree.PBTreeConstruction;
import ReadFileData.ReadFiledata;

import java.io.File;

import static PBTree.PBTreeConstruction.insert;
import static QueryProcessing.Cloud_Query.toByteArray;
import static QueryProcessing.TrapdoorCompute.addBytes;

public class ReLoadTree {
    public String path = "F:\\TiveQP\\ServeDB\\6w_1km\\";
    public int round = 0;
    public String key = "2bc73dw20ebf4d46";
    public int segment_len = 200;
    public int op = 1;


    public PBTreeConstruction.PBNode BuildPBTree(String[][] nums){
        //  空树
        if(nums.length==0)
            return null;
        //  叶子节点
        if (nums.length==1){
            PBTreeConstruction.PBNode root = new PBTreeConstruction.PBNode(nums);
            root.flag = round++;
            root.height = 1;
            return root;
        }
        //  正常节点

        //  计算左子树元素个数
        int num_left = 0;
        if (nums.length%2 == 0){
            num_left = nums.length/2;
        }else {
            num_left = nums.length/2 + 1;
        }

        //  删除原树元素集合中右子树元素，剩余为左子树元素
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

        time = time + initNode(root.left,ibf_length,Keylist);
        time = time + initNode(root.right,ibf_length,Keylist);

        if (root.height != 1){
            time = time + initMidNode(root,ibf_length,Keylist);
        }else {
            time = time + initLeafNode(root,ibf_length,Keylist);
        }
//        System.out.println(root.address + "is ok");


        return time;
    }


    public long initLeafNode(PBTreeConstruction.PBNode leafnode , int ibf_length , String[] Keylist ) throws Exception {
        long start = System.currentTimeMillis();
        leafnode.address =path+ "leafnode "+leafnode.flag+".txt";
        File file = new File(leafnode.address);
        if (file.exists()){
            long end = System.currentTimeMillis();
//            System.out.print(leafnode.flag+", ");
            return end - start;
        }else {
            //  创建 ibf
            Byte[] twinlist = new Byte[ibf_length];
            for (int i = 0; i < ibf_length; i++) {
                twinlist[i] = 0;

            }
            String[][] location = new String[leafnode.data.length][];

            String[][] Time = new String[leafnode.data.length][];

            String[] type =  new Owner().OwnerType(leafnode.data[0][0]);

            for (int i = 0; i < leafnode.data.length; i++) {
                //  location
                location[i] = new Owner().OwnerLocation(leafnode.data[i][1],Double.parseDouble(leafnode.data[i][2]),Double.parseDouble(leafnode.data[i][3]));
                //  time
                Time[i] = new Owner().OwnerTime(Integer.valueOf(leafnode.data[i][4]),Integer.valueOf(leafnode.data[i][5]),Integer.valueOf(leafnode.data[i][6]),Integer.valueOf(leafnode.data[i][7]));
            }

            //  插入 type 数据
            for (int i = 0; i < type.length; i++) {
                insert(twinlist,type[i],Keylist,leafnode.flag);
            }

            //  插入 location 数据
            for (int i = 0; i < location.length; i++) {
                for (int j = 0; j < location[i].length; j++) {
                    insert(twinlist,location[i][j],Keylist,leafnode.flag);
                }
            }

            //  插入 time 数据
            for (int i = 0; i < Time.length; i++) {
                for (int j = 0; j < Time[i].length; j++) {
                    insert(twinlist,Time[i][j],Keylist,leafnode.flag);
                }
            }

//            leafnode.address_HB =path_HB+ "leafnode "+leafnode.flag+".txt";

//        leafnode.HB = MyUtil.HashFounction.mdinstance.digest(addBytes(twinlist[0],twinlist[1]));
            String plaintext = leafnode.data[0][0] + "**" + leafnode.data[0][1] + "**" + leafnode.data[0][2] + "**" + leafnode.data[0][3] + "**" + leafnode.data[0][4] + "**" + leafnode.data[0][5] + "**" + leafnode.data[0][6] + "**" + leafnode.data[0][7] ;
            String encode = MyUtil.AESUtil.encryptIntoHexString(plaintext, key);
            leafnode.ciper = encode;
            leafnode.HL = MyUtil.HashFounction.mdinstance.digest(toByteArray(encode));

//        leafnode.HB = MyUtil.HashFounction.mdinstance.digest(toPrimitives(twinlist));
//            int group = ibf_length/ segment_len;
//            byte[][] HB = new byte[group][];
//            for (int i = 0; i < group; i++) {
//                Byte[] segment = new Byte[segment_len];
//                for (int j = 0; j < segment.length; j++) {
//                    segment[j] = twinlist[i* segment_len +j];
//                }
//                HB[i] = MyUtil.HashFounction.mdinstance.digest(toPrimitives(segment));
//            }

            long end = System.currentTimeMillis();

            ReadFiledata.saveArray(twinlist,leafnode.address);
//            ReadFiledata.saveArray(HB,leafnode.address_HB);
            return end - start;
        }
    }

    public long initMidNode(PBTreeConstruction.PBNode midnode , int ibf_length , String[] Keylist ) throws Exception {
        long start = System.currentTimeMillis();

        midnode.address =path+ "midnode "+midnode.flag+".txt";

        File file = new File(midnode.address);
        if (file.exists()){
            long end = System.currentTimeMillis();
            return end - start;
        }else {
            //  创建 ibf
            Byte[] twinlist = new Byte[ibf_length];
            for (int i = 0; i < ibf_length; i++) {
                twinlist[i] = 0;

            }
            String[][] location = new String[midnode.data.length][];

            String[][] Time = new String[midnode.data.length][];

            String[] type =  new Owner().OwnerType(midnode.data[0][0]);

            for (int i = 0; i < midnode.data.length; i++) {
                //  location
                location[i] = new Owner().OwnerLocation(midnode.data[i][1],Double.parseDouble(midnode.data[i][2]),Double.parseDouble(midnode.data[i][3]));
                //  time
                Time[i] = new Owner().OwnerTime(Integer.valueOf(midnode.data[i][4]),Integer.valueOf(midnode.data[i][5]),Integer.valueOf(midnode.data[i][6]),Integer.valueOf(midnode.data[i][7]));
            }

            //  插入 type 数据
            for (int i = 0; i < type.length; i++) {
                insert(twinlist,type[i],Keylist,midnode.flag);
            }

            //  插入 location 数据
            for (int i = 0; i < location.length; i++) {
                for (int j = 0; j < location[i].length; j++) {
                    insert(twinlist,location[i][j],Keylist,midnode.flag);
                }
            }

            //  插入 time 数据
            for (int i = 0; i < Time.length; i++) {
                for (int j = 0; j < Time[i].length; j++) {
                    insert(twinlist,Time[i][j],Keylist,midnode.flag);
                }
            }

//            midnode.address_HB =path_HB+ "midnode "+midnode.flag+".txt";

//            midnode.HL = MyUtil.HashFounction.mdinstance.digest(addBytes(midnode.left.HL,midnode.right.HL));

//        midnode.HB = MyUtil.HashFounction.mdinstance.digest(toPrimitives(twinlist));

//            int group = ibf_length/ segment_len;
//            byte[][] HB = new byte[group][];
//            for (int i = 0; i < group; i++) {
//                Byte[] segment = new Byte[segment_len];
//                for (int j = 0; j < segment.length; j++) {
//                    segment[j] = twinlist[i* segment_len +j];
//                }
//                HB[i] = MyUtil.HashFounction.mdinstance.digest(toPrimitives(segment));
//            }

            long end = System.currentTimeMillis();

            ReadFiledata.saveArray(twinlist,midnode.address);
//            ReadFiledata.saveArray(HB,midnode.address_HB);
            return end - start;
        }
    }
}
