package AdvanceTest;

import ReadFileData.ReadFiledata;
import com.carrotsearch.sizeof.RamUsageEstimator;

import static ReadFileData.ReadFiledata.readArray_String;

public class TestdataSet {
    public static void main(String[] args) {

        Byte[] a = {0,1,0,1,1,1,1,0,1,1,1,1,0,0,0,0};
        String address = "E:\\Gao\\0TiveQP\\DataSet\\Type\\byte_p.txt";
        ReadFiledata.saveArray(a,address);

        Byte[] b = ReadFiledata.readArray(address);
        for (int i = 0; i < b.length; i++) {
            System.out.print(b[i]+" ");
        }

    }
}
