package IndexBuilding;

import java.util.ArrayList;
import java.util.List;

public class IndexElementEncoding {

    /**
     * 数值 x 的 bitsize位 前缀族
     * @param bitsize
     * @param x
     * @return  字符数组
     * @throws Exception
     */
    public static String[] prefix(int bitsize, int x) throws Exception {//   a number x of bitsize bit  F(6) = { 0110,011*,01**,0***,**** }
        if (Math.pow(2, bitsize) - 1 < x) {
            throw new Exception();
        }
        String result = Integer.toBinaryString(x);
        StringBuffer tempsb = new StringBuffer();
        if (result.length() < bitsize) {
            for (int i = 0; i < bitsize - result.length(); i++) {
                tempsb.append(0);
            }
        }
        tempsb.append(result);
        String list[] = new String[bitsize + 1];
        for (int i = 0; i < bitsize; i++) {
            list[i] = tempsb.toString();
            tempsb.replace(bitsize - i - 1, bitsize - i, "*");
        }
        list[bitsize] = tempsb.toString();
        return list;
    }

    /**
     * 数值区间 [down , up] 的 bitsize 范围
     * @param bitsize
     * @param down
     * @param up
     * @return  字符数组
     */
    public static String[] range(int bitsize, int down, int up) {// range[down,up] of bitsize bit       S[0,8] = { 0***,1000 }

        String[] str = new String[up - down + 1];
        for (int i = 0; i < str.length; i++) {
            String result = Integer.toBinaryString(down + i);
            StringBuffer tempsb = new StringBuffer();
            if (result.length() < bitsize) {
                for (int j = 0; j < bitsize - result.length(); j++) {
                    tempsb.append(0);
                }
            }
            tempsb.append(result);
            str[i] = tempsb.toString();
        }
        double time = Math.floor(Math.log(str.length) / Math.log(2));
        String[] temp = new String[str.length];
        temp = str;
        do {
            int k = temp.length;
            for (int j = 1; j < k; j++) {
                String s = prefix(temp[j - 1], temp[j]);
                if (s.length() > 0) {
                    StringBuffer tempsb = new StringBuffer();
                    tempsb.append(temp[j]);
                    tempsb.replace(s.length(), bitsize, "*");

                    temp[j - 1] = tempsb.toString();
                    temp[j - 1] = add(temp[j - 1], bitsize);
                    temp[j] = temp[j - 1];
                    j++;
                }
            }
            String[] t2 = Repeat(temp);
            temp = t2;
            time--;
        } while (time > 0);
        return temp;
    }


    /**
     * 数值 num 的 bitsize位 反区间
     * @param bitsize
     * @param num
     * @param MinNumber     区间最小元素
     * @param MaxNumber     区间最大元素
     * @return  字符数组
     * @throws Exception
     */
    public static String[] Complement(int bitsize, int num ,int MinNumber, int MaxNumber) throws Exception {   // Complement of num of bitsize bit C(2) in (0,7) = { 00*,011,1** }
        //  边界情况一：恰巧最小元素   min， 【min+1 ，max】
        if (MinNumber == num){
            return range(bitsize,MinNumber+1, MaxNumber);
        }

        //  边界情况二：恰巧比最小元素+1     min+1，  min + 【min+2，max】
        if ( (MinNumber + 1) ==num){
            String[] n = range(bitsize, num + 1, MaxNumber);
            String[] result = new String[n.length + 1];
            result[0] = prefix(bitsize,MinNumber)[0];
            for (int i = 1; i < result.length; i++) {
                result[i] = n[i-1];
            }
            return result;
        }

        //  边界情况三：恰巧为最大元素       max，    【min，max-1】
        if (MaxNumber == num){
            return range(bitsize,MinNumber,MaxNumber-1);
        }

        //  边界情况四：恰巧为最大元素-1     max-1，  【min，max-2】 + max
        if ((MaxNumber-1)== num){
            String[] n = range(bitsize, MinNumber, MaxNumber-2);
            String[] result = new String[n.length + 1];
            result[n.length] = prefix(bitsize,MaxNumber)[0];
            for (int i = 0; i < result.length; i++) {
                result[i] = n[i];
            }
            return result;
        }

        //  正常情况：   n，  【min，n-1】+【n+1，max】
        String[] com_1 = range(bitsize,MinNumber,num-1);
        String[] com_2 = range(bitsize, num+1,MaxNumber);
        String[] result = new String[com_1.length+com_2.length];
        for (int i = 0; i < com_1.length ; i++) {
            result[i] = com_1[i];
        }
        for (int i = 0; i < com_2.length; i++) {
            result[i + com_1.length] = com_2[i];
        }
        return result;
    }

    public static String[] UnitPrefix(String[] n1,String[] n2){     //  合并两个集合
        String[] unit_1 = IndexElementEncoding.Merge(n1,n2);
        return unit(unit_1);
    }

    public static String[] unit(String[] num){  //  合并集合中的前缀元素

        for (int i = 0; i < num.length; i++) {
            for (int j = i+1; j < num.length; j++) {
                String same_prefix = prefix(num[i],num[j]);
                if ("*".equals(num[i].substring(same_prefix.length(),same_prefix.length()+1))){
                    num[j] = num[i];
                }else if ("*".equals(num[j].substring(same_prefix.length(),same_prefix.length()+1)) ){
                    num[i] = num[j];
                }
            }
        }

        return Repeat(num);
    }

    /**
     * 字符串未满strLength位前 右补*
     * @param str
     * @param strLength
     * @return
     */
    public static String add(String str, int strLength) {       //补全
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                //sb.append("0").append(str);// 左补0
                sb.append(str).append("*");//右补*
                str = sb.toString();
                strLen = str.length();
            }
        }

        return str;
    }

    public static String prefix(String s1, String s2) {          //相同前缀
        String str = "";
        int samebit = 0;
        int min = (s1.length() < s2.length()) ? s1.length() : s2.length();
        char[] ch1 = s1.toCharArray();
        char[] ch2 = s2.toCharArray();
        for (int i = 0; i < min; i++) {
            if (ch1[i] == ch2[i]) {
                str = str + ch1[i];
            } else {
                break;
            }
        }
        for (int i = 0; i < min; i++) {
            if (ch1[i] == ch2[i]) {
                samebit++;
            }
        }
        if (samebit == s1.length() - 1) {
            return str;
        } else {
            str = "";
            return str;
        }
    }

    public static String[] Repeat(String[] arr) {       //一个数组中去除相同元素
        //创建一个集合
        List list = new ArrayList();
        //遍历数组往集合里存元素
        for (int i = 0; i < arr.length; i++) {
            //如果集合里面没有相同的元素才往里存
            if (!list.contains(arr[i])) {
                list.add(arr[i]);
            }
        }
        //toArray()方法会返回一个包含集合所有元素的Object类型数组
        Object[] newArr = list.toArray();
        String[] dest = new String[newArr.length];
        for (int i = 0; i < dest.length; i++)
            dest[i] = newArr[i].toString();
        return dest;
    }

    public static String[] RepeatS(String[] s1, String[] s2) {//两个数组中取相同元素
        List list = new ArrayList();
        for (int i = 0; i < s1.length; i++) {
            for (int j = 0; j < s2.length; j++) {
                if (s1[i].equals(s2[j]))
                    list.add(s1[i]);
            }
        }
        Object[] newArr = list.toArray();
        String[] dest = new String[newArr.length];
        for (int i = 0; i < dest.length; i++)
            dest[i] = newArr[i].toString();
        String[] result = Repeat(dest);
        return result;
    }

    /**
     * 两个字符数组合并，相同元素只取一次
     * @param num1
     * @param num2
     * @return
     */
    public static String[] Merge(String [] num1,String [] num2){
        String[] strs1 = new String[num1.length+num2.length];
        for (int i = 0; i < num1.length; i++) {
            strs1[i] = num1[i];
        }
        for (int i = 0; i < num2.length; i++) {
            strs1[i + num1.length] = num2[i];
        }
        return Repeat(strs1);

    }

}


