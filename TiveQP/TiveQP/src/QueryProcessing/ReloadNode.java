package QueryProcessing;

import IndexBuilding.IndexElementEncoding;
import IndexBuilding.Owner;
import ReadFileData.ReadFiledata;
import TiveTree.Construction;

import java.math.BigInteger;
import java.sql.Time;

import static QueryProcessing.Cloud_Query.toByteArray;
import static QueryProcessing.ReLoadTree.addBytes;

public class ReloadNode {
    public String path = TiveQP_parameters.treeStorePath;
//    public String path = "F:\\TiveQP\\Node_10w_1km_Query\\";
    //  Node_2w_1km_Query
    public int round = 0;

    public Construction.TiveTreeNode BuildTiveSubTree(String[][] nums){
        //  间隔
        int big = 1000;
        //  空树
        if(nums.length==0)
            return null;
        //  叶子节点
        if (nums.length / big == 1){
            Construction.TiveTreeNode root = BuildTiveTree(nums);
            root.flag = round++;
            root.subroot = true;
            root.subrootleaf = true;
            return root;
        }
        //  正常节点

        //  计算左子树元素个数
        int num_left = 0;
        if ((nums.length / big) % 2 == 0){
            num_left = nums.length / big / 2;
        }else {
            num_left = nums.length /big /2 + 1;
        }

        //  删除原树元素集合中右子树元素，剩余为左子树元素
        String[][] tree_left = new String[num_left * big][];
        String[][] tree_right = new String[nums.length-num_left*big][];

        for (int i = 0; i < num_left*big; i++) {
            tree_left[i] = nums[i];
        }
        for (int i = 0; i < tree_right.length; i++) {
            tree_right[i] = nums[i+num_left];
        }
        Construction.TiveTreeNode root = new Construction.TiveTreeNode(nums);
        root.flag = round++;
        root.subroot = true;
        root.subrootleaf = false;
        root.height = (Math.ceil(Math.log(nums.length / big) / Math.log(2))) + 1 + 9 ;
        root.left = BuildTiveSubTree(tree_left);
        root.right = BuildTiveSubTree(tree_right);
        return root;
    }


    public Construction.TiveTreeNode BuildTiveTree(String[][] nums){
        //  空树
        if(nums.length==0)
            return null;
        //  叶子节点
        if (nums.length==1){
            Construction.TiveTreeNode root = new Construction.TiveTreeNode(nums);
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
        Construction.TiveTreeNode root = new Construction.TiveTreeNode(nums);
        root.flag = round++;
//        root.flag = new Random().nextInt(2000000);
        root.height = (Math.ceil(Math.log(nums.length) / Math.log(2))) + 1;
        root.left = BuildTiveTree(tree_left);
        root.right = BuildTiveTree(tree_right);
        return root;
    }


    public long initNode(Construction.TiveTreeNode root , int ibf_length , String[] Keylist , int rb ) throws Exception {
        long time = 0;
        if(root==null)
            return time;
        time = time + initNode(root.left,ibf_length,Keylist,rb);
        time = time + initNode(root.right,ibf_length,Keylist,rb);

        if (root.subrootleaf == true){
            time = time + initSubrootLeafNode(root,ibf_length,Keylist,rb);
        }else if (root.subroot == true){
            time = time + initSubrootMidNode(root,ibf_length,Keylist,rb);
        }else if (root.height != 1){
            time = time + initMidNode(root,ibf_length,Keylist,rb);
        }else  if (root.height == 1){
            time = time + initLeafNode(root,ibf_length,Keylist,rb);
//            System.out.println(root.address + "is ok");
        }
//        System.out.println(root.address + "is ok");
        return time;
    }


    /**
     * 初始化叶子节点
     * @param leafnode  叶子节点
     * @param ibf_length    ibf 长度
     * @param Keylist   密钥
     * @param rb    随机数
     * @throws Exception
     */
    public long initLeafNode(Construction.TiveTreeNode leafnode , int ibf_length , String[] Keylist , int rb ) throws Exception {
        long start = System.currentTimeMillis();
        leafnode.address =path+ "leafnode "+leafnode.flag+".txt";
//        String[][] Location = new String[leafnode.data.length][];
        leafnode.LCS = new String[leafnode.data.length][];
//        String[][] Time = new String[leafnode.data.length][];
        leafnode.TCS = new String[leafnode.data.length][];

        for (int i = 0; i < leafnode.data.length; i++) {
            //  location
//            Location[i] = new Owner().OwnerLocation(leafnode.data[i][1],Double.parseDouble(leafnode.data[i][2]),Double.parseDouble(leafnode.data[i][3]));
            leafnode.LCS[i] = new Owner().OwnerLoation_Complement (leafnode.data[i][1],Double.parseDouble(leafnode.data[i][2]),Double.parseDouble(leafnode.data[i][3]));

            //  time
//            Time[i] = new Owner().OwnerTime(Integer.valueOf(leafnode.data[i][4]),Integer.valueOf(leafnode.data[i][5]),Integer.valueOf(leafnode.data[i][6]),Integer.valueOf(leafnode.data[i][7]));
            leafnode.TCS[i] = new Owner().OwnerTime_Complement(Integer.valueOf(leafnode.data[i][4]),Integer.valueOf(leafnode.data[i][5]),Integer.valueOf(leafnode.data[i][6]),Integer.valueOf(leafnode.data[i][7]));
        }



        leafnode.bits_LCS = new String[leafnode.LCS.length][][];
        leafnode.HV_LCS = new byte[leafnode.LCS.length][][][];


        //   lcs 插入 ibf ， 计算 bits——lcs ，计算 Hv——lcs
        for (int i = 0; i < leafnode.LCS.length; i++) {

            String[][] temp = new String[leafnode.LCS[i].length][Keylist.length - 1];
            byte[][][] b = new byte[leafnode.LCS[i].length][Keylist.length - 1][];

            for (int j = 0; j < leafnode.LCS[i].length; j++) {
                for (int k = 0; k < Keylist.length - 1; k++) {
                    byte[] outbytes = MyUtil.HashFounction.HmacSHA256Encrypt(leafnode.LCS[i][j],Keylist[k]);    //  HMAC(w,k_i)
                    BigInteger bi = new BigInteger(1, outbytes);
                    int twinindex = bi.mod(BigInteger.valueOf(ibf_length)).intValue();      //  twins_id
                    //now we get k twins

                    //for each twin, compute the chosen location
                    byte[] hkp1 = MyUtil.HashFounction.mdinstance.digest(addBytes(outbytes, Keylist[Keylist.length - 1].getBytes()));//h_k+1
                    BigInteger hkp1bi = new BigInteger(1, hkp1);
                    byte[] sha1bytes = MyUtil.HashFounction.H.digest(hkp1bi.xor(BigInteger.valueOf(rb)).toByteArray());  //sha1_xor rb
                    int location = new BigInteger(1, sha1bytes).mod(BigInteger.valueOf(2)).intValue();//mod2

                    if (location == 0) {
//                        twinlist[0][twinindex] = 1;
//                        twinlist[1][twinindex] = 0;
                        temp[j][k] = twinindex+"|"+1;     //  bits
                        b[j][k] = MyUtil.HashFounction.mdinstance.digest(addBytes(temp[j][k].getBytes(), Keylist[Keylist.length - 1].getBytes()));  //  HV
                    } else {
//                        twinlist[1][twinindex] = 1;
//                        twinlist[0][twinindex] = 0;
                        temp[j][k] = twinindex+"|"+0;
                        b[j][k] = MyUtil.HashFounction.mdinstance.digest(addBytes(temp[j][k].getBytes(), Keylist[Keylist.length - 1].getBytes()));  //  HV
                    }
                }
            }

            leafnode.bits_LCS[i] = temp;
            leafnode.HV_LCS[i] = b;
        }


        leafnode.bits_TCS = new String[leafnode.TCS.length][][];
        leafnode.HV_TCS = new byte[leafnode.TCS.length][][][];

//        //  插入 time 数据
//        for (int i = 0; i < Time.length; i++) {
//            for (int j = 0; j < Time[i].length; j++) {
//                insert(twinlist,Time[i][j],Keylist,rb);
//            }
//        }

        //   tcs 插入 ibf ， 计算 bits——tcs ，计算 Hv——tcs
        for (int i = 0; i < leafnode.TCS.length; i++) {

            String[][] temp = new String[leafnode.TCS[i].length][Keylist.length - 1];
            byte[][][] b = new byte[leafnode.TCS[i].length][Keylist.length - 1][];

            for (int j = 0; j < leafnode.TCS[i].length; j++) {
                for (int k = 0; k < Keylist.length - 1; k++) {
                    byte[] outbytes = MyUtil.HashFounction.HmacSHA256Encrypt(leafnode.TCS[i][j],Keylist[k]);    //  HMAC(w,k_i)
                    BigInteger bi = new BigInteger(1, outbytes);
                    int twinindex = bi.mod(BigInteger.valueOf(ibf_length)).intValue();      //  twins_id
                    //now we get k twins

                    //for each twin, compute the chosen location
                    byte[] hkp1 = MyUtil.HashFounction.mdinstance.digest(addBytes(outbytes, Keylist[Keylist.length - 1].getBytes()));//h_k+1
                    BigInteger hkp1bi = new BigInteger(1, hkp1);
                    byte[] sha1bytes = MyUtil.HashFounction.H.digest(hkp1bi.xor(BigInteger.valueOf(rb)).toByteArray());  //sha1_xor rb
                    int location = new BigInteger(1, sha1bytes).mod(BigInteger.valueOf(2)).intValue();//mod2

                    if (location == 0) {
//                        twinlist[0][twinindex] = 1;
//                        twinlist[1][twinindex] = 0;
                        temp[j][k] = twinindex+"|"+1;     //  bits
                        b[j][k] = MyUtil.HashFounction.mdinstance.digest(addBytes(temp[j][k].getBytes(), Keylist[Keylist.length - 1].getBytes()));  //  HV
                    } else {
//                        twinlist[1][twinindex] = 1;
//                        twinlist[0][twinindex] = 0;
                        temp[j][k] = twinindex+"|"+0;
                        b[j][k] = MyUtil.HashFounction.mdinstance.digest(addBytes(temp[j][k].getBytes(), Keylist[Keylist.length - 1].getBytes()));  //  HV
                    }
                }
            }

            leafnode.bits_TCS[i] = temp;
            leafnode.HV_TCS[i] = b;
        }

        String plaintext = leafnode.data[0][0] + "**" + leafnode.data[0][1] + "**" + leafnode.data[0][2] + "**" + leafnode.data[0][3] + "**" + leafnode.data[0][4] + "**" + leafnode.data[0][5] + "**" + leafnode.data[0][6] + "**" + leafnode.data[0][7] ;
        String key = "2bc73dw20ebf4d46";
        String encode = MyUtil.AESUtil.encryptIntoHexString(plaintext, key);
        leafnode.HV = MyUtil.HashFounction.mdinstance.digest(toByteArray(encode));
        long end = System.currentTimeMillis();
//        ReadFiledata.saveArray(twinlist,leafnode.address);


//        System.out.println("leafnode "+leafnode.flag +" time:"+(end-start));
//        System.out.println("leafnode "+leafnode.flag +" is ok");

        return (end-start);
    }




    /**
     * 初始化中间节点
     * @param midnode   中间节点
     * @param ibf_length    ibf 长度
     * @throws
     */
    public long initMidNode(Construction.TiveTreeNode midnode, int ibf_length , String[] Keylist , int rb) throws Exception {

        long start = System.currentTimeMillis();

        //  取LCS、TCS 的并集
        midnode.TCS = new String[midnode.left.TCS.length][];
        midnode.LCS = new String[midnode.left.LCS.length][];


        for (int i = 0; i < midnode.left.TCS.length; i++) {
            midnode.LCS[i] = IndexElementEncoding.UnitPrefix(midnode.left.LCS[i],midnode.right.LCS[i]);
            midnode.TCS[i] = IndexElementEncoding.UnitPrefix(midnode.left.TCS[i],midnode.right.TCS[i]);
        }

        midnode.bits_TCS = new String[midnode.TCS.length][][];
        midnode.HV_TCS = new byte[midnode.TCS.length][][][];


        //   tcs 插入 ibf ， 计算 bits——tcs ，计算 Hv——tcs
        for (int i = 0; i < midnode.TCS.length; i++) {

            String[][] temp = new String[midnode.TCS[i].length][Keylist.length - 1];
            byte[][][] b = new byte[midnode.TCS[i].length][Keylist.length - 1][];

            for (int j = 0; j < midnode.TCS[i].length; j++) {
                for (int k = 0; k < Keylist.length - 1; k++) {
                    byte[] outbytes = MyUtil.HashFounction.HmacSHA256Encrypt(midnode.TCS[i][j],Keylist[k]);    //  HMAC(w,k_i)
                    BigInteger bi = new BigInteger(1, outbytes);
                    int twinindex = bi.mod(BigInteger.valueOf(ibf_length)).intValue();      //  twins_id
                    //now we get k twins

                    //for each twin, compute the chosen location
                    byte[] hkp1 = MyUtil.HashFounction.mdinstance.digest(addBytes(outbytes, Keylist[Keylist.length - 1].getBytes()));//h_k+1
                    BigInteger hkp1bi = new BigInteger(1, hkp1);
                    byte[] sha1bytes = MyUtil.HashFounction.H.digest(hkp1bi.xor(BigInteger.valueOf(rb)).toByteArray());  //sha1_xor rb
                    int location = new BigInteger(1, sha1bytes).mod(BigInteger.valueOf(2)).intValue();//mod2

                    if (location == 0) {
//                        twinlist[0][twinindex] = 1;
//                        twinlist[1][twinindex] = 0;
                        temp[j][k] = twinindex+"|"+1;     //  bits
                        b[j][k] = MyUtil.HashFounction.mdinstance.digest(addBytes(temp[j][k].getBytes(), Keylist[Keylist.length - 1].getBytes()));  //  HV
                    } else {
//                        twinlist[1][twinindex] = 1;
//                        twinlist[0][twinindex] = 0;
                        temp[j][k] = twinindex+"|"+0;
                        b[j][k] = MyUtil.HashFounction.mdinstance.digest(addBytes(temp[j][k].getBytes(), Keylist[Keylist.length - 1].getBytes()));  //  HV
                    }
                }
            }

            midnode.bits_TCS[i] = temp;
            midnode.HV_TCS[i] = b;
        }

        midnode.bits_LCS = new String[midnode.LCS.length][][];
        midnode.HV_LCS = new byte[midnode.LCS.length][][][];


        //   tcs 插入 ibf ， 计算 bits——tcs ，计算 Hv——tcs
        for (int i = 0; i < midnode.LCS.length; i++) {

            String[][] temp = new String[midnode.LCS[i].length][Keylist.length - 1];
            byte[][][] b = new byte[midnode.LCS[i].length][Keylist.length - 1][];

            for (int j = 0; j < midnode.LCS[i].length; j++) {
                for (int k = 0; k < Keylist.length - 1; k++) {
                    byte[] outbytes = MyUtil.HashFounction.HmacSHA256Encrypt(midnode.LCS[i][j],Keylist[k]);    //  HMAC(w,k_i)
                    BigInteger bi = new BigInteger(1, outbytes);
                    int twinindex = bi.mod(BigInteger.valueOf(ibf_length)).intValue();      //  twins_id
                    //now we get k twins

                    //for each twin, compute the chosen location
                    byte[] hkp1 = MyUtil.HashFounction.mdinstance.digest(addBytes(outbytes, Keylist[Keylist.length - 1].getBytes()));//h_k+1
                    BigInteger hkp1bi = new BigInteger(1, hkp1);
                    byte[] sha1bytes = MyUtil.HashFounction.H.digest(hkp1bi.xor(BigInteger.valueOf(rb)).toByteArray());  //sha1_xor rb
                    int location = new BigInteger(1, sha1bytes).mod(BigInteger.valueOf(2)).intValue();//mod2

                    if (location == 0) {
//                        twinlist[0][twinindex] = 1;
//                        twinlist[1][twinindex] = 0;
                        temp[j][k] = twinindex+"|"+1;     //  bits
                        b[j][k] = MyUtil.HashFounction.mdinstance.digest(addBytes(temp[j][k].getBytes(), Keylist[Keylist.length - 1].getBytes()));  //  HV
                    } else {
//                        twinlist[1][twinindex] = 1;
//                        twinlist[0][twinindex] = 0;
                        temp[j][k] = twinindex+"|"+0;
                        b[j][k] = MyUtil.HashFounction.mdinstance.digest(addBytes(temp[j][k].getBytes(), Keylist[Keylist.length - 1].getBytes()));  //  HV
                    }
                }
            }

            midnode.bits_LCS[i] = temp;
            midnode.HV_LCS[i] = b;
        }

        midnode.HV = MyUtil.HashFounction.mdinstance.digest(addBytes(midnode.left.HV,midnode.right.HV));

        midnode.address =path+ "midnode "+midnode.flag+".txt";


        long end = System.currentTimeMillis();
//        ReadFiledata.saveArray(twinlist,midnode.address);

//        System.out.println("midnode "+midnode.flag +" time:"+(end-start));
//        System.out.println("midnode "+midnode.flag +" is ok");
        return end-start;


    }

    public long initSubrootLeafNode(Construction.TiveTreeNode SubRootLeafnode, int ibf_length , String[] Keylist , int rb) throws Exception {
        long start = System.currentTimeMillis();

        SubRootLeafnode.YCS = new Owner().OwnerType_Complement(SubRootLeafnode.data[0][0]);


        //  计算 bits——ycs ，计算 Hv——ycs
        SubRootLeafnode.bits_YCS = new String[SubRootLeafnode.YCS.length][];
        SubRootLeafnode.HV_YCS = new byte[SubRootLeafnode.YCS.length][][];


        for (int i = 0; i < SubRootLeafnode.YCS.length; i++) {

            String[] temp = new String[Keylist.length - 1];
            byte[][] b = new byte[Keylist.length - 1][];


            for (int k = 0; k < Keylist.length - 1; k++) {
                byte[] outbytes = MyUtil.HashFounction.HmacSHA256Encrypt(SubRootLeafnode.YCS[i],Keylist[k]);    //  HMAC(w,k_i)
                BigInteger bi = new BigInteger(1, outbytes);
                int twinindex = bi.mod(BigInteger.valueOf(ibf_length)).intValue();      //  twins_id
                //now we get k twins

                //for each twin, compute the chosen location
                byte[] hkp1 = MyUtil.HashFounction.mdinstance.digest(addBytes(outbytes, Keylist[Keylist.length - 1].getBytes()));//h_k+1
                BigInteger hkp1bi = new BigInteger(1, hkp1);
                byte[] sha1bytes = MyUtil.HashFounction.H.digest(hkp1bi.xor(BigInteger.valueOf(rb)).toByteArray());  //sha1_xor rb
                int location = new BigInteger(1, sha1bytes).mod(BigInteger.valueOf(2)).intValue();//mod2

                if (location == 0) {
//                    twinlist[0][twinindex] = 1;
//                    twinlist[1][twinindex] = 0;
                    temp[k] = twinindex+"|"+1;     //  bits
                    b[k] = MyUtil.HashFounction.mdinstance.digest(addBytes(temp[k].getBytes(), Keylist[Keylist.length - 1].getBytes()));  //  HV
                } else {
//                    twinlist[1][twinindex] = 1;
//                    twinlist[0][twinindex] = 0;
                    temp[k] = twinindex+"|"+0;
                    b[k] = MyUtil.HashFounction.mdinstance.digest(addBytes(temp[k].getBytes(), Keylist[Keylist.length - 1].getBytes()));  //  HV
                }
            }


            SubRootLeafnode.bits_YCS[i] = temp;
            SubRootLeafnode.HV_YCS[i] = b;
        }

        //  取LCS、TCS 的并集
        SubRootLeafnode.TCS = new String[SubRootLeafnode.left.TCS.length][];
        SubRootLeafnode.LCS = new String[SubRootLeafnode.left.LCS.length][];


        for (int i = 0; i < SubRootLeafnode.left.TCS.length; i++) {
            SubRootLeafnode.LCS[i] = IndexElementEncoding.UnitPrefix(SubRootLeafnode.left.LCS[i],SubRootLeafnode.right.LCS[i]);
            SubRootLeafnode.TCS[i] = IndexElementEncoding.UnitPrefix(SubRootLeafnode.left.TCS[i],SubRootLeafnode.right.TCS[i]);
        }

        SubRootLeafnode.bits_TCS = new String[SubRootLeafnode.TCS.length][][];
        SubRootLeafnode.HV_TCS = new byte[SubRootLeafnode.TCS.length][][][];


        //   tcs 插入 ibf ， 计算 bits——tcs ，计算 Hv——tcs
        for (int i = 0; i < SubRootLeafnode.TCS.length; i++) {

            String[][] temp = new String[SubRootLeafnode.TCS[i].length][Keylist.length - 1];
            byte[][][] b = new byte[SubRootLeafnode.TCS[i].length][Keylist.length - 1][];

            for (int j = 0; j < SubRootLeafnode.TCS[i].length; j++) {
                for (int k = 0; k < Keylist.length - 1; k++) {
                    byte[] outbytes = MyUtil.HashFounction.HmacSHA256Encrypt(SubRootLeafnode.TCS[i][j],Keylist[k]);    //  HMAC(w,k_i)
                    BigInteger bi = new BigInteger(1, outbytes);
                    int twinindex = bi.mod(BigInteger.valueOf(ibf_length)).intValue();      //  twins_id
                    //now we get k twins

                    //for each twin, compute the chosen location
                    byte[] hkp1 = MyUtil.HashFounction.mdinstance.digest(addBytes(outbytes, Keylist[Keylist.length - 1].getBytes()));//h_k+1
                    BigInteger hkp1bi = new BigInteger(1, hkp1);
                    byte[] sha1bytes = MyUtil.HashFounction.H.digest(hkp1bi.xor(BigInteger.valueOf(rb)).toByteArray());  //sha1_xor rb
                    int location = new BigInteger(1, sha1bytes).mod(BigInteger.valueOf(2)).intValue();//mod2

                    if (location == 0) {
//                        twinlist[0][twinindex] = 1;
//                        twinlist[1][twinindex] = 0;
                        temp[j][k] = twinindex+"|"+1;     //  bits
                        b[j][k] = MyUtil.HashFounction.mdinstance.digest(addBytes(temp[j][k].getBytes(), Keylist[Keylist.length - 1].getBytes()));  //  HV
                    } else {
//                        twinlist[1][twinindex] = 1;
//                        twinlist[0][twinindex] = 0;
                        temp[j][k] = twinindex+"|"+0;
                        b[j][k] = MyUtil.HashFounction.mdinstance.digest(addBytes(temp[j][k].getBytes(), Keylist[Keylist.length - 1].getBytes()));  //  HV
                    }
                }
            }

            SubRootLeafnode.bits_TCS[i] = temp;
            SubRootLeafnode.HV_TCS[i] = b;
        }

        SubRootLeafnode.bits_LCS = new String[SubRootLeafnode.LCS.length][][];
        SubRootLeafnode.HV_LCS = new byte[SubRootLeafnode.LCS.length][][][];


        //   tcs 插入 ibf ， 计算 bits——tcs ，计算 Hv——tcs
        for (int i = 0; i < SubRootLeafnode.LCS.length; i++) {

            String[][] temp = new String[SubRootLeafnode.LCS[i].length][Keylist.length - 1];
            byte[][][] b = new byte[SubRootLeafnode.LCS[i].length][Keylist.length - 1][];

            for (int j = 0; j < SubRootLeafnode.LCS[i].length; j++) {
                for (int k = 0; k < Keylist.length - 1; k++) {
                    byte[] outbytes = MyUtil.HashFounction.HmacSHA256Encrypt(SubRootLeafnode.LCS[i][j],Keylist[k]);    //  HMAC(w,k_i)
                    BigInteger bi = new BigInteger(1, outbytes);
                    int twinindex = bi.mod(BigInteger.valueOf(ibf_length)).intValue();      //  twins_id
                    //now we get k twins

                    //for each twin, compute the chosen location
                    byte[] hkp1 = MyUtil.HashFounction.mdinstance.digest(addBytes(outbytes, Keylist[Keylist.length - 1].getBytes()));//h_k+1
                    BigInteger hkp1bi = new BigInteger(1, hkp1);
                    byte[] sha1bytes = MyUtil.HashFounction.H.digest(hkp1bi.xor(BigInteger.valueOf(rb)).toByteArray());  //sha1_xor rb
                    int location = new BigInteger(1, sha1bytes).mod(BigInteger.valueOf(2)).intValue();//mod2

                    if (location == 0) {
//                        twinlist[0][twinindex] = 1;
//                        twinlist[1][twinindex] = 0;
                        temp[j][k] = twinindex+"|"+1;     //  bits
                        b[j][k] = MyUtil.HashFounction.mdinstance.digest(addBytes(temp[j][k].getBytes(), Keylist[Keylist.length - 1].getBytes()));  //  HV
                    } else {
//                        twinlist[1][twinindex] = 1;
//                        twinlist[0][twinindex] = 0;
                        temp[j][k] = twinindex+"|"+0;
                        b[j][k] = MyUtil.HashFounction.mdinstance.digest(addBytes(temp[j][k].getBytes(), Keylist[Keylist.length - 1].getBytes()));  //  HV
                    }
                }
            }

            SubRootLeafnode.bits_LCS[i] = temp;
            SubRootLeafnode.HV_LCS[i] = b;
        }


        //  计算Hv
        SubRootLeafnode.HV = MyUtil.HashFounction.mdinstance.digest(addBytes(SubRootLeafnode.left.HV,SubRootLeafnode.right.HV));


        SubRootLeafnode.address =path+ "SubRootLeafnode "+SubRootLeafnode.flag+".txt";
//        //  创建 ibf
//        Byte[][] twinlist = new Byte[2][ibf_length];
//        for (int i = 0; i < twinlist[0].length; i++) {
//            twinlist[0][i] = 0;
//            twinlist[1][i] = 0;
//        }

//        long start_read = System.currentTimeMillis();

        //  读取子节点twinlist
//        Byte[][] twinlist = ReadFiledata.readArray(SubRootLeafnode.address);
//        Byte[][] twinlist_right = ReadFiledata.readArray(SubRootLeafnode.right.address);
//        long end_read = System.currentTimeMillis();

//        System.out.println(end_read-start_read);

//        //  取子节点的并集
//        for (int i = 0; i < twinlist[0].length; i++) {
//            twinlist[0][i] = together(twinlist_left[0][i],twinlist_right[0][i]);
//            twinlist[1][i] = together(twinlist_left[1][i],twinlist_right[1][i]);
//        }

        long end = System.currentTimeMillis();
//        ReadFiledata.saveArray(twinlist,SubRootLeafnode.address);

//        System.out.println("SubRootLeafnode "+SubRootLeafnode.flag +" time:"+(end-start));
//        System.out.println("SubRootLeafnode "+SubRootLeafnode.flag +" is ok");
        return end-start;
    }

    public long initSubrootMidNode(Construction.TiveTreeNode SubRootMidnode, int ibf_length , String[] Keylist , int rb) throws Exception {
        long start = System.currentTimeMillis();

        //  取YCS 的并集
        SubRootMidnode.YCS = IndexElementEncoding.UnitPrefix(SubRootMidnode.left.YCS,SubRootMidnode.right.YCS);


        //  计算 bits——ycs ，计算 Hv——ycs
        SubRootMidnode.bits_YCS = new String[SubRootMidnode.YCS.length][];
        SubRootMidnode.HV_YCS = new byte[SubRootMidnode.YCS.length][][];


        for (int i = 0; i < SubRootMidnode.YCS.length; i++) {

            String[] temp = new String[Keylist.length - 1];
            byte[][] b = new byte[Keylist.length - 1][];


            for (int k = 0; k < Keylist.length - 1; k++) {
                byte[] outbytes = MyUtil.HashFounction.HmacSHA256Encrypt(SubRootMidnode.YCS[i],Keylist[k]);    //  HMAC(w,k_i)
                BigInteger bi = new BigInteger(1, outbytes);
                int twinindex = bi.mod(BigInteger.valueOf(ibf_length)).intValue();      //  twins_id
                //now we get k twins

                //for each twin, compute the chosen location
                byte[] hkp1 = MyUtil.HashFounction.mdinstance.digest(addBytes(outbytes, Keylist[Keylist.length - 1].getBytes()));//h_k+1
                BigInteger hkp1bi = new BigInteger(1, hkp1);
                byte[] sha1bytes = MyUtil.HashFounction.H.digest(hkp1bi.xor(BigInteger.valueOf(rb)).toByteArray());  //sha1_xor rb
                int location = new BigInteger(1, sha1bytes).mod(BigInteger.valueOf(2)).intValue();//mod2

                if (location == 0) {
//                    twinlist[0][twinindex] = 1;
//                    twinlist[1][twinindex] = 0;
                    temp[k] = twinindex+"|"+1;     //  bits
                    b[k] = MyUtil.HashFounction.mdinstance.digest(addBytes(temp[k].getBytes(), Keylist[Keylist.length - 1].getBytes()));  //  HV
                } else {
//                    twinlist[1][twinindex] = 1;
//                    twinlist[0][twinindex] = 0;
                    temp[k] = twinindex+"|"+0;
                    b[k] = MyUtil.HashFounction.mdinstance.digest(addBytes(temp[k].getBytes(), Keylist[Keylist.length - 1].getBytes()));  //  HV
                }
            }


            SubRootMidnode.bits_YCS[i] = temp;
            SubRootMidnode.HV_YCS[i] = b;
        }

        //  计算Hv
        SubRootMidnode.HV = MyUtil.HashFounction.mdinstance.digest(addBytes(SubRootMidnode.left.HV,SubRootMidnode.right.HV));
        SubRootMidnode.address =path+ "SubRootMidnode "+SubRootMidnode.flag+".txt";
//        //  创建 ibf
//        Byte[][] twinlist = new Byte[2][ibf_length];
//        for (int i = 0; i < twinlist[0].length; i++) {
//            twinlist[0][i] = 0;
//            twinlist[1][i] = 0;
//        }

//        long start_read = System.currentTimeMillis();

        //  读取子节点twinlist
//        Byte[][] twinlist = ReadFiledata.readArray(SubRootMidnode.address);
//        Byte[][] twinlist_right = ReadFiledata.readArray(SubRootMidnode.right.address);
//        long end_read = System.currentTimeMillis();

//        System.out.println(end_read-start_read);

//        //  取子节点的并集
//        for (int i = 0; i < twinlist[0].length; i++) {
//            twinlist[0][i] = together(twinlist_left[0][i],twinlist_right[0][i]);
//            twinlist[1][i] = together(twinlist_left[1][i],twinlist_right[1][i]);
//        }




        long end = System.currentTimeMillis();
//        ReadFiledata.saveArray(twinlist,SubRootMidnode.address);

//        System.out.println("SubRootMidnode "+SubRootMidnode.flag +" time:"+(end-start));
//        System.out.println("SubRootMidnode "+SubRootMidnode.flag +" is ok");
        return end-start;
    }
}
