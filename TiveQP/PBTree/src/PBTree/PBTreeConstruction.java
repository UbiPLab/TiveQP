package PBTree;

import IndexBuilding.Owner;
import Parameter.PBTree_paremeters;
import ReadFileData.ReadFiledata;

import java.math.BigInteger;

public class PBTreeConstruction {

    public String path = PBTree_paremeters.treeStorePath;
    public int round = 0;

    public static class PBNode{
        public String address;
        public String[][] data;
        public double height;
        public int flag;
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

        if(nums.length==0)
            return null;

        if (nums.length==1){
            PBNode root = new PBNode(nums);
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


    public long initLeafNode(PBNode leafnode , int ibf_length , String[] Keylist ) throws Exception {
        long start = System.currentTimeMillis();

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


        for (int i = 0; i < type.length; i++) {
            insert(twinlist,type[i],Keylist,leafnode.flag);
        }

        for (int i = 0; i < location.length; i++) {
            for (int j = 0; j < location[i].length; j++) {
                insert(twinlist,location[i][j],Keylist,leafnode.flag);
            }
        }


        for (int i = 0; i < Time.length; i++) {
            for (int j = 0; j < Time[i].length; j++) {
                insert(twinlist,Time[i][j],Keylist,leafnode.flag);
            }
        }

        leafnode.address =path+ "leafnode "+leafnode.flag+".txt";
        long end = System.currentTimeMillis();
        ReadFiledata.saveArray(twinlist,leafnode.address);



//        System.out.println("leafnode "+leafnode.flag +" time:"+(end-start));
//        System.out.println("leafnode "+leafnode.flag +" is ok");

        return end - start;
    }

    public long initMidNode(PBNode midnode , int ibf_length , String[] Keylist ) throws Exception {
        long start = System.currentTimeMillis();

        Byte[] twinlist = new Byte[ibf_length];
        for (int i = 0; i < ibf_length; i++) {
            twinlist[i] = 0;
        }


//        //  type
//        leafnode.type = new Owner().OwnerType(leafnode.data[0][0]);
//        leafnode.YCS = new Owner().OwnerType_Complement(leafnode.data[0][0]);

        String[][] location = new String[midnode.data.length][];

        String[][] Time = new String[midnode.data.length][];

        String[] type =  new Owner().OwnerType(midnode.data[0][0]);

        for (int i = 0; i < midnode.data.length; i++) {
            //  location

            location[i] = new Owner().OwnerLocation(midnode.data[i][1],Double.parseDouble(midnode.data[i][2]),Double.parseDouble(midnode.data[i][3]));
            //  time
            Time[i] = new Owner().OwnerTime(Integer.valueOf(midnode.data[i][4]),Integer.valueOf(midnode.data[i][5]),Integer.valueOf(midnode.data[i][6]),Integer.valueOf(midnode.data[i][7]));
        }


        for (int i = 0; i < type.length; i++) {
            insert(twinlist,type[i],Keylist,midnode.flag);
        }

        for (int i = 0; i < location.length; i++) {
            for (int j = 0; j < location[i].length; j++) {
                insert(twinlist,location[i][j],Keylist,midnode.flag);
            }
        }

        for (int i = 0; i < Time.length; i++) {
            for (int j = 0; j < Time[i].length; j++) {
                insert(twinlist,Time[i][j],Keylist,midnode.flag);
            }
        }

        midnode.address =path+ "midnode "+midnode.flag+".txt";
        long end = System.currentTimeMillis();
        ReadFiledata.saveArray(twinlist,midnode.address);


//        System.out.println("leafnode "+leafnode.flag +" time:"+(end-start));
//        System.out.println("leafnode "+leafnode.flag +" is ok");

        return end - start;
    }


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

    public static byte[] addBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;

    }

    public static byte[] addBytes(Byte[] data1, Byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        for (int i = 0; i < data1.length; i++) {
            data3[i] = data1[i];
        }
        for (int i = 0; i < data2.length; i++) {
            data3[i+data1.length] = data2[i];
        }
        return data3;
    }
    public static String toHexString(byte[] byteArray) {
        final StringBuilder hexString = new StringBuilder("");
        if (byteArray == null || byteArray.length <= 0)
            return null;
        for (int i = 0; i < byteArray.length; i++) {
            int v = byteArray[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                hexString.append(0);
            }
            hexString.append(hv);
        }
        return hexString.toString().toLowerCase();
    }
}
