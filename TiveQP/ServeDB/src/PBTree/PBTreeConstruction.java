package PBTree;

import IndexBuilding.Owner;
import Parameter.ServerDB_parameters;
import ReadFileData.ReadFiledata;
import com.carrotsearch.sizeof.RamUsageEstimator;

import java.io.File;
import java.math.BigInteger;

import static QueryProcessing.TrapdoorCompute.toHexString;

public class PBTreeConstruction {

    public String path = ServerDB_parameters.treeStorePath;
//    public String path = "G:\\TiveQP\\ServeDB\\10w_1km\\";
//    public String path = "H:\\TiveQP\\ServeDB\\10w_1km_8\\";
//    public String path_HB = "G:\\TiveQP\\ServeDB\\HB_10w_1km\\";
    public int round = 0;
    public String key = "2bc73dw20ebf4d46";
    public int segment_len = 200;
    public int ibf_length = 200000;
//    public Byte[] twinlist = new Byte[ibf_length];

    public static class PBNode{
        public String address;
        public String address_HB;
        public String[][] data;
        public String ciper;
        public double height;
        public int flag;    //  代替随机数
//        public String[][] location;
//        public String[][] Time;
//        public String[] type;
        public byte[] HL;
        public byte[][] HB;
        public PBNode left;
        public PBNode right;
        public PBNode(String[][] s) {//构造方法
            data = s;
        }
        public void showData(){
            System.out.println(address);
        }
    }

    public PBNode BuildPBTree(String[][] nums){
        //  空树
        if(nums.length==0)
            return null;
        //  叶子节点
        if (nums.length==1){
            PBNode root = new PBNode(nums);
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
        PBNode root = new PBNode(nums);
        root.flag = round++;
//        root.flag = new Random().nextInt(2000000);
        root.height = (Math.ceil(Math.log(nums.length) / Math.log(2))) + 1;
        root.left = BuildPBTree(tree_left);
        root.right = BuildPBTree(tree_right);
        return root;
    }

    public long initNode(PBNode root , int ibf_length , String[] Keylist ) throws Exception {
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


    public long initLeafNode(PBNode leafnode , int ibf_length , String[] Keylist ) throws Exception {
        System.out.print(leafnode.flag+",");
        long start = System.currentTimeMillis();
        //  创建 ibf
        Byte[] twinlist = new Byte[ibf_length];
        for (int i = 0; i < ibf_length; i++) {
            twinlist[i] = 0;
        }

        //  准备数据
//        //  type
//        leafnode.type = new Owner().OwnerType(leafnode.data[0][0]);
//        leafnode.YCS = new Owner().OwnerType_Complement(leafnode.data[0][0]);

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

        leafnode.address =path+ "leafnode "+leafnode.flag+".txt";
//        leafnode.address_HB =path_HB+ "leafnode "+leafnode.flag+".txt";

//        leafnode.HB = MyUtil.HashFounction.mdinstance.digest(addBytes(twinlist[0],twinlist[1]));
        String plaintext = leafnode.data[0][0] + "**" + leafnode.data[0][1] + "**" + leafnode.data[0][2] + "**" + leafnode.data[0][3] + "**" + leafnode.data[0][4] + "**" + leafnode.data[0][5] + "**" + leafnode.data[0][6] + "**" + leafnode.data[0][7] ;
        String encode = MyUtil.AESUtil.encryptIntoHexString(plaintext, key);
        leafnode.ciper = encode;
        leafnode.HL = MyUtil.HashFounction.mdinstance.digest(toByteArray(encode));

//        leafnode.HB = MyUtil.HashFounction.mdinstance.digest(toPrimitives(twinlist));
        int group = ibf_length/ segment_len;
        byte[][] HB = new byte[group][];
        for (int i = 0; i < group; i++) {
            Byte[] segment = new Byte[segment_len];
            for (int j = 0; j < segment.length; j++) {
                segment[j] = twinlist[i* segment_len +j];
            }
            HB[i] = MyUtil.HashFounction.mdinstance.digest(toPrimitives(segment));
        }

        long end = System.currentTimeMillis();

//        ReadFiledata.saveArray(twinlist,leafnode.address);
//        ReadFiledata.saveArray(HB,leafnode.address_HB);


//        System.out.println("leafnode "+leafnode.flag +" time:"+(end-start));
//        System.out.println("leafnode "+leafnode.flag +" is ok");

        return end - start;
    }

    public long initMidNode(PBNode midnode , int ibf_length , String[] Keylist ) throws Exception {
        long start = System.currentTimeMillis();
        //  创建 ibf
        Byte[] twinlist = new Byte[ibf_length];
        for (int i = 0; i < ibf_length; i++) {
            twinlist[i] = 0;
        }

        //  准备数据
//        //  type
//        leafnode.type = new Owner().OwnerType(leafnode.data[0][0]);
//        leafnode.YCS = new Owner().OwnerType_Complement(leafnode.data[0][0]);

       String[][] location = new String[midnode.data.length][];

        String[][] Time = new String[midnode.data.length][];

        String[] type =  new Owner().OwnerType(midnode.data[0][0]);

        for (int i = 0; i < midnode.data.length; i++) {
            //  location
//            System.out.println("第 "+i+" 行");
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

        midnode.address =path+ "midnode "+midnode.flag+".txt";
//        midnode.address_HB =path_HB+ "midnode "+midnode.flag+".txt";

        midnode.HL = MyUtil.HashFounction.mdinstance.digest(addBytes(midnode.left.HL,midnode.right.HL));

//        midnode.HB = MyUtil.HashFounction.mdinstance.digest(toPrimitives(twinlist));

        int group = ibf_length/ segment_len;
        byte[][] HB = new byte[group][];
        for (int i = 0; i < group; i++) {
            Byte[] segment = new Byte[segment_len];
            for (int j = 0; j < segment.length; j++) {
                segment[j] = twinlist[i* segment_len +j];
            }
            HB[i] = MyUtil.HashFounction.mdinstance.digest(toPrimitives(segment));
        }

        long end = System.currentTimeMillis();

//        ReadFiledata.saveArray(twinlist,midnode.address);
//        ReadFiledata.saveArray(HB,midnode.address_HB);



//        System.out.println("leafnode "+leafnode.flag +" time:"+(end-start));
//        System.out.println("leafnode "+leafnode.flag +" is ok");

        return end - start;
    }

    /**
     * 将数据插入twinlist
     * @param twinlist
     * @param data
     * @param Keylist
     * @param rb
     * @throws Exception
     */
    public static void insert(Byte[] twinlist, String data ,String[] Keylist , int rb) throws Exception {
        for (int i = 0; i < Keylist.length - 1; i++) {
            byte[] outbytes = MyUtil.HashFounction.HmacSHA256Encrypt(data,Keylist[i]);    //  HMAC(w,k_i)
            byte[] hkp1 = MyUtil.HashFounction.HmacSHA256Encrypt(toHexString(outbytes),String.valueOf(rb));

            BigInteger bi = new BigInteger(1, hkp1);
            int twinindex = bi.mod(BigInteger.valueOf(twinlist.length)).intValue();      //  twins_id
            //now we get k twins

            //for each twin, compute the chosen location
            twinlist[twinindex] = 1;
        }
    }

    public static byte[] toPrimitives(Byte[] oBytes)
    {
        byte[] bytes = new byte[oBytes.length];

        for(int i = 0; i < oBytes.length; i++) {
            bytes[i] = oBytes[i];
        }

        return bytes;
    }

    public static Byte[][] toPrimitives_B(byte[][] oBytes)
    {
        Byte[][] bytes = new Byte[oBytes.length][];

        for(int i = 0; i < oBytes.length; i++) {
            for (int j = 0; j < oBytes[i].length; j++) {
                bytes[i][j] = oBytes[i][j];
            }
        }

        return bytes;
    }

    public static byte[] toByteArray(String hexString) {
        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() >> 1];
        int index = 0;
        for (int i = 0; i < hexString.length(); i++) {
            if (index  > hexString.length() - 1)
                return byteArray;
            byte highDit = (byte) (Character.digit(hexString.charAt(index), 16) & 0xFF);
            byte lowDit = (byte) (Character.digit(hexString.charAt(index + 1), 16) & 0xFF);
            byteArray[i] = (byte) (highDit << 4 | lowDit);
            index += 2;
        }
        return byteArray;
    }

    public static byte[] addBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;

    }
}
