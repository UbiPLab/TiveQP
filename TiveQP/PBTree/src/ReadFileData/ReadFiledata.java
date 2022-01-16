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

    public static Byte[] readArray(String address) {

        FileReader reader = null;
        BufferedReader readerBuf = null;
        Byte[] array = null;
        try {
            reader = new FileReader(address);
            readerBuf = new BufferedReader(reader);

            String lineStr;
            while((lineStr = readerBuf.readLine()) != null) {

                String[] strList = lineStr.split("\\s+");
                int columnNum = strList.length;

                array = new Byte[columnNum];


                for (int i = 0; i < columnNum; i++) {
                    if (strList[i].equals("0"))
                        array[i] = 0;
                    else if (strList[i].equals("1")) {
                        array[i] = 1;
                    }
                }

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


    public static void saveArray(Byte[] array,String address){
        FileWriter writeFile = null;
        try {
            File file = new File(address);
            if(!file.exists()) {
                file.createNewFile();
            }
            writeFile = new FileWriter(file);
                for(int j = 0; j < array.length - 1; j++) {
                    writeFile.write(array[j] + " ");
                }
                writeFile.write(array[array.length - 1] + "");
                writeFile.write("\n");

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


}


