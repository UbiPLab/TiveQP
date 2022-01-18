package IBFTree;

import IndexBuilding.Owner;
import Parameter.IBFTree_parameters;
import ReadFileData.ReadFiledata;

import java.math.BigInteger;

public class IBFTreeConstruction {

    public String path = IBFTree_parameters.treeStorePath;
    public int round = 0;
    
    public static class IBFNode{
        public String address;
        public String[][] data;
        public double height;
        public int flag;
        public IBFNode left;
        public IBFNode right;
        public IBFNode(String[][] s) {//构造方法
            data = s;
        }
        public void showData(){
            System.out.println(address);
        }
    }

    public IBFNode BuildPBTree(String[][] nums){

        if(nums.length==0)
            return null;

        if (nums.length==1){
            IBFNode root = new IBFNode(nums);
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
        IBFNode root = new IBFNode(nums);
        root.flag = round++;
//        root.flag = new Random().nextInt(2000000);
        root.height = (Math.ceil(Math.log(nums.length) / Math.log(2))) + 1;
        root.left = BuildPBTree(tree_left);
        root.right = BuildPBTree(tree_right);
        return root;
    }

    public long initNode(IBFNode root , int ibf_length , String[] Keylist ,int rb) throws Exception {
        long time = 0;
        if(root==null)
            return time;

        time = time + initNode(root.left,ibf_length,Keylist,rb);
        time = time + initNode(root.right,ibf_length,Keylist,rb);

        if (root.height != 1){
            time = time + initMidNode(root,ibf_length,Keylist,rb);
        }else {
            time = time + initLeafNode(root,ibf_length,Keylist,rb);
        }
//        System.out.println(root.address + "is ok");


        return time;
    }


    public long initLeafNode(IBFNode leafnode , int ibf_length , String[] Keylist ,int rb ) throws Exception {
        long start = System.currentTimeMillis();
        Byte[][] twinlist = new Byte[2][ibf_length];
        for (int i = 0; i < ibf_length; i++) {
            twinlist[0][i] = 0;
            twinlist[1][i] = 0;
        }

//        //  type
//        leafnode.type = new Owner().OwnerType(leafnode.data[0][0]);
//        leafnode.YCS = new Owner().OwnerType_Complement(leafnode.data[0][0]);

        String[][] location = new String[leafnode.data.length][];

        String[][] Time = new String[leafnode.data.length][];

        String[] type =  new Owner().OwnerType(leafnode.data[0][0]);

        for (int i = 0; i < leafnode.data.length; i++) {
            //  location
            System.out.println(leafnode.data[i][1]+","+leafnode.data[i][2]);
            location[i] = new Owner().OwnerLocation(leafnode.data[i][1],Double.parseDouble(leafnode.data[i][2]),Double.parseDouble(leafnode.data[i][3]));
            //  time
            Time[i] = new Owner().OwnerTime(Integer.valueOf(leafnode.data[i][4]),Integer.valueOf(leafnode.data[i][5]),Integer.valueOf(leafnode.data[i][6]),Integer.valueOf(leafnode.data[i][7]));
        }


        for (int i = 0; i < type.length; i++) {
            insert(twinlist,type[i],Keylist,rb);
        }


        for (int i = 0; i < location.length; i++) {
            for (int j = 0; j < location[i].length; j++) {
                insert(twinlist,location[i][j],Keylist,rb);
            }
        }


        for (int i = 0; i < Time.length; i++) {
            for (int j = 0; j < Time[i].length; j++) {
                insert(twinlist,Time[i][j],Keylist,rb);
            }
        }

        leafnode.address =path+ "leafnode "+leafnode.flag+".txt";
        long end = System.currentTimeMillis();
//        ReadFiledata.saveArray(twinlist,leafnode.address);



//        System.out.println("leafnode "+leafnode.flag +" time:"+(end-start));
//        System.out.println("leafnode "+leafnode.flag +" is ok");

        return end - start;
    }

    public long initMidNode(IBFNode midnode , int ibf_length , String[] Keylist ,int rb) throws Exception {

        Byte[][] twinlist_left = new Byte[2][ibf_length];
        for (int i = 0; i < ibf_length; i++) {
            twinlist_left[0][i] = 0;
            twinlist_left[1][i] = 0;
        }
        Byte[][] twinlist_right = new Byte[2][ibf_length];
        for (int i = 0; i < ibf_length; i++) {
            twinlist_right[0][i] = 0;
            twinlist_right[1][i] = 0;
        }
        long start = System.currentTimeMillis();
        //  创建 ibf
        Byte[][] twinlist = new Byte[2][ibf_length];
        for (int i = 0; i < ibf_length; i++) {
            twinlist[0][i] = 0;
            twinlist[1][i] = 0;
        }

//        long start_read = System.currentTimeMillis();
//
//        Byte[][] twinlist_left = ReadFiledata.readArray(midnode.left.address);
//        Byte[][] twinlist_right = ReadFiledata.readArray(midnode.right.address);
//
//        long end_read = System.currentTimeMillis();

//        System.out.println(end_read-start_read);

        for (int i = 0; i < ibf_length; i++) {
            twinlist[0][i] = together(twinlist_left[0][i],twinlist_right[0][i]);
            twinlist[1][i] = together(twinlist_right[1][i],twinlist_right[1][i]);
        }

        midnode.address =path+ "midnode "+midnode.flag+".txt";
        long end = System.currentTimeMillis();
//        ReadFiledata.saveArray(twinlist,midnode.address);


//        System.out.println("leafnode "+leafnode.flag +" time:"+(end-start));
//        System.out.println("leafnode "+leafnode.flag +" is ok");

//        return (end - start) - (end_read - start_read);
        return (end - start) ;
    }

    /**
     * Insert into twinlist
     * @param twinlist
     * @param data
     * @param Keylist
     * @param rb
     * @throws Exception
     */
    public static void insert(Byte[][] twinlist, String data ,String[] Keylist , int rb) throws Exception {
        for (int i = 0; i < Keylist.length - 1; i++) {
            byte[] outbytes = MyUtil.HashFounction.HmacSHA256Encrypt(data,Keylist[i]);    //  HMAC(w,k_i)
            BigInteger bi = new BigInteger(1, outbytes);
            int twinindex = bi.mod(BigInteger.valueOf(twinlist[0].length)).intValue();      //  twins_id
            //now we get k twins

            //for each twin, compute the chosen location
            byte[] hkp1 = MyUtil.HashFounction.mdinstance.digest(addBytes(outbytes, Keylist[Keylist.length - 1].getBytes()));//h_k+1
            BigInteger hkp1bi = new BigInteger(1, hkp1);
            byte[] sha1bytes = MyUtil.HashFounction.H.digest(hkp1bi.xor(BigInteger.valueOf(rb)).toByteArray());  //sha1_xor rb
            int location = new BigInteger(1, sha1bytes).mod(BigInteger.valueOf(2)).intValue();//mod2

            if (location == 0) {
                twinlist[0][twinindex] = 1;
                twinlist[1][twinindex] = 0;
            } else {
                twinlist[1][twinindex] = 1;
                twinlist[0][twinindex] = 0;
            }
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

    public static byte together(byte a, byte b){
        byte m = 1;
        if( (a==1) || (b==1)){
            return m;
        }else {
            return a;
        }
    }
}
