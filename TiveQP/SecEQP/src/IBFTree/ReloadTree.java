package IBFTree;
import Parameter.Parameterp;
import java.math.BigInteger;

public class ReloadTree {


        public String path = Parameter.treeStorePath;
        public int round = 0;



        public IBFTree.IBFTreeConstruction.IBFNode BuildPBTree(String[][] nums){
            //  空树
            if(nums.length==0)
                return null;
            //  叶子节点
            if (nums.length==1){
                IBFTree.IBFTreeConstruction.IBFNode root = new IBFTree.IBFTreeConstruction.IBFNode(nums);
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
            IBFTree.IBFTreeConstruction.IBFNode root = new IBFTree.IBFTreeConstruction.IBFNode(nums);
            root.flag = round++;
//        root.flag = new Random().nextInt(2000000);
            root.height = (Math.ceil(Math.log(nums.length) / Math.log(2))) + 1;
            root.left = BuildPBTree(tree_left);
            root.right = BuildPBTree(tree_right);
            return root;
        }

        public long initNode(IBFTree.IBFTreeConstruction.IBFNode root , int ibf_length , String[] Keylist , int rb) throws Exception {
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


        public long initLeafNode(IBFTree.IBFTreeConstruction.IBFNode leafnode , int ibf_length , String[] Keylist , int rb ) throws Exception {
            long start = System.currentTimeMillis();
            //  创建 ibf

            leafnode.address =path+ "leafnode "+leafnode.flag+".txt";
            long end = System.currentTimeMillis();
//        ReadFiledata.saveArray(twinlist,leafnode.address);



//        System.out.println("leafnode "+leafnode.flag +" time:"+(end-start));
//        System.out.println("leafnode "+leafnode.flag +" is ok");

            return end - start;
        }

        public long initMidNode(IBFTree.IBFTreeConstruction.IBFNode midnode , int ibf_length , String[] Keylist , int rb) throws Exception {
            long start = System.currentTimeMillis();
            //  创建 ibf


            midnode.address =path+ "midnode "+midnode.flag+".txt";
            long end = System.currentTimeMillis();
//        ReadFiledata.saveArray(twinlist,midnode.address);


//        System.out.println("leafnode "+leafnode.flag +" time:"+(end-start));
//        System.out.println("leafnode "+leafnode.flag +" is ok");

            return (end - start);
        }

        /**
         * 将数据插入twinlist
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

        //取并集
        public static byte together(byte a, byte b){
            byte m = 1;
            if( (a==1) || (b==1)){
                return m;
            }else {
                return a;
            }
        }


}
