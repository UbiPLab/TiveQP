package ReadFileData;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadFiledata {

    public static String[][] readArray_String(String address) {


        FileReader reader = null;

        BufferedReader readerBuf = null;

        String[][] array = null;
        try {

            reader = new FileReader(address);

            readerBuf = new BufferedReader(reader);

            List<String> strList = new ArrayList<>();

            String lineStr;

            while((lineStr = readerBuf.readLine()) != null) {

                strList.add(lineStr);
            }

            int lineNum = strList.size();

            String s =  strList.get(0);
            int columnNum = s.split("\\*\\*").length;

            array = new String[strList.size()][columnNum];

            int count = 0;

            for(String str : strList) {

                String[] strs = str.split("\\*\\*");
                for(int i = 0; i < columnNum; i++) {
                    array[count][i] = strs[i];;
                }

                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                if(reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if(readerBuf != null)
                    readerBuf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return array;
    }

    public static Byte[][] readArray(String address) {


        FileReader reader = null;

        BufferedReader readerBuf = null;

        Byte[][] array = null;
        try {

            reader = new FileReader(address);

            readerBuf = new BufferedReader(reader);

            List<String> strList = new ArrayList<>();

            String lineStr;

            while((lineStr = readerBuf.readLine()) != null) {

                strList.add(lineStr);
            }

            int lineNum = strList.size();

            String s =  strList.get(0);
            int columnNum = s.split("\\s+").length;

            array = new Byte[strList.size()][columnNum];

            int count = 0;

            for(String str : strList) {

                String[] strs = str.split("\\s+");
                for(int i = 0; i < columnNum; i++) {
                    if (strs[i].equals("0"))
                        array[count][i] =0;
                    else if (strs[i].equals("1")){
                        array[count][i] =1;
                    }
                }

                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                if(reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if(readerBuf != null)
                    readerBuf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return array;
    }


    public static void saveArray(Byte[][] array,String address){

        FileWriter writeFile = null;
        try {

            File file = new File(address);

            if(!file.exists()) {
                file.createNewFile();
            }

            writeFile = new FileWriter(file);

            for(int i = 0; i < array.length; i++) {
                for(int j = 0; j < array[0].length - 1; j++) {
                    writeFile.write(array[i][j] + " ");
                }

                writeFile.write(array[i][array[0].length - 1] + "");

                writeFile.write("\n");
            }

            writeFile.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                if(writeFile != null)
                    writeFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveArray_bits_TCSLCS(String[][][] bits,String address){
        String[][] array = changeThreeToTwo(bits);

        FileWriter writeFile = null;
        try {

            File file = new File(address);

            if(!file.exists()) {
                file.createNewFile();
            }

            writeFile = new FileWriter(file);

            for(int i = 0; i < array.length; i++) {

                for(int j = 0; j < array[0].length - 1; j++) {
                    writeFile.write(array[i][j] + " ");
                }

                writeFile.write(array[i][array[0].length - 1] + "");

                writeFile.write("\n");
            }

            writeFile.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                if(writeFile != null)
                    writeFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String[][] changeThreeToTwo(String[][][] bits){
        String[][] result = new String[bits.length][];
        for (int i = 0; i < bits.length; i++) {
            for (int j = 0; j < bits[i].length; j++) {
                result[i][j] = bits[i][j][0] + "**" +bits[i][j][1] + "**" +bits[i][j][2] + "**" +bits[i][j][3] + "**" +bits[i][j][4];
            }
        }
        return result;
    }

    public static String[][][] changeTwoToThree(String[][] result){
        String[][][] bits = new String[result.length][][];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                bits[i][j] = result[i][j].split("\\*\\*");
            }
        }
        return bits;
    }


}


