package Tree;

import IndexBuilding.IndexElementEncoding;
import IndexBuilding.Owner;

import java.math.BigInteger;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/1/16 13:59
 * @Version 1.0
 */
public class RTree {

    public int round = 0;
    public class RNode{

        public String[][] data;
        public String[] location;
        public String[] Time;
        public String[] type;
        public double height;
        public int flag;
        public RNode left;
        public RNode right;

        RNode(String[][] s) {//构造方法
            data = s;
        }
        public void showData(){
            System.out.println(data[0][0]+","+data[0][1]+","+data[0][2]+","+data[0][3]+","+data[0][4]+","+data[0][5]+","+data[0][6]+","+data[0][7]);
        }
    }

    public RNode BuildRTree(String[][] nums){
        //  空树
        if(nums.length==0)
            return null;
        //  叶子节点
        if (nums.length==1){
            RNode root = new RNode(nums);
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
        RNode root = new RNode(nums);
        root.flag = round++;
//        root.flag = new Random().nextInt(2000000);
        root.height = (Math.ceil(Math.log(nums.length) / Math.log(2))) + 1;
        root.left = BuildRTree(tree_left);
        root.right = BuildRTree(tree_right);
        return root;
    }

    public long initNode(RNode root) throws Exception {
        long time = 0;
        if(root==null)
            return time;
        time = time + initNode(root.left);
        time = time + initNode(root.right);

        if (root.height != 1){
            time = time + initMidNode(root);
        }else  if (root.height == 1){
            time = time + initLeafNode(root);
        }
//        System.out.println(root.address + "is ok");
        return time;
    }

    public long initLeafNode(RNode leafnode ) throws Exception {
        long start = System.currentTimeMillis();


        //  准备数据
//        //  type
//        leafnode.type = new Owner().OwnerType(leafnode.data[0][0]);
//        leafnode.YCS = new Owner().OwnerType_Complement(leafnode.data[0][0]);

        leafnode.location = new String[leafnode.data.length];
        leafnode.Time = new String[leafnode.data.length];
        leafnode.type = new String[leafnode.data.length];


        //  location
        leafnode.location = new Owner().OwnerLocation(leafnode.data[0][1],Double.parseDouble(leafnode.data[0][2]),Double.parseDouble(leafnode.data[0][3]));
        //  type
        leafnode.type =  new Owner().OwnerType(leafnode.data[0][0]);
        //  time
        leafnode.Time = new Owner().OwnerTime(Integer.valueOf(leafnode.data[0][4]),Integer.valueOf(leafnode.data[0][5]),Integer.valueOf(leafnode.data[0][6]),Integer.valueOf(leafnode.data[0][7]));



        long end = System.currentTimeMillis();




//        System.out.println("leafnode "+leafnode.flag +" time:"+(end-start));
//        System.out.println("leafnode "+leafnode.flag +" is ok");

        return end - start;
    }


    /**
     * 初始化中间节点
     * @param midnode   中间节点
     * @param
     * @throws
     */
    public long initMidNode(RNode midnode) throws Exception {
        long start = System.currentTimeMillis();

        midnode.location = IndexElementEncoding.UnitPrefix(midnode.left.location,midnode.right.location);
        midnode.Time = IndexElementEncoding.UnitPrefix(midnode.left.Time,midnode.right.Time);
        midnode.type = IndexElementEncoding.UnitPrefix(midnode.left.type,midnode.right.type);


        long end = System.currentTimeMillis();

//        System.out.println("midnode "+midnode.flag +" time:"+(end-start));
//        System.out.println("midnode "+midnode.flag +" is ok");
        return (end-start);


    }

}
