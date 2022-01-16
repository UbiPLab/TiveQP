package IndexBuilding;

import java.util.ArrayList;
import java.util.List;

public class IndexElementEncoding {


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



    public static String[] Complement(int bitsize, int num ,int MinNumber, int MaxNumber) throws Exception {   // Complement of num of bitsize bit C(2) in (0,7) = { 00*,011,1** }

        if (MinNumber == num){
            return range(bitsize,MinNumber+1, MaxNumber);
        }


        if ( (MinNumber + 1) ==num){
            String[] n = range(bitsize, num + 1, MaxNumber);
            String[] result = new String[n.length + 1];
            result[0] = prefix(bitsize,MinNumber)[0];
            for (int i = 1; i < result.length; i++) {
                result[i] = n[i-1];
            }
            return result;
        }


        if (MaxNumber == num){
            return range(bitsize,MinNumber,MaxNumber-1);
        }


        if ((MaxNumber-1)== num){
            String[] n = range(bitsize, MinNumber, MaxNumber-2);
            String[] result = new String[n.length + 1];
            result[n.length] = prefix(bitsize,MaxNumber)[0];
            for (int i = 0; i < result.length; i++) {
                result[i] = n[i];
            }
            return result;
        }


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

    public static String[] UnitPrefix(String[] n1,String[] n2){
        String[] unit_1 = IndexElementEncoding.Merge(n1,n2);
        return unit(unit_1);
    }

    public static String[] unit(String[] num){

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


    public static String add(String str, int strLength) {
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

    public static String prefix(String s1, String s2) {
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

    public static String[] Repeat(String[] arr) {

        List list = new ArrayList();

        for (int i = 0; i < arr.length; i++) {

            if (!list.contains(arr[i])) {
                list.add(arr[i]);
            }
        }

        Object[] newArr = list.toArray();
        String[] dest = new String[newArr.length];
        for (int i = 0; i < dest.length; i++)
            dest[i] = newArr[i].toString();
        return dest;
    }

    public static String[] RepeatS(String[] s1, String[] s2) {
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


