package AdvanceTest;

import ReadFileData.ReadFiledata;

import java.util.ArrayList;
import java.util.List;

public class TestReadTime {
    public static void main(String[] args) {
        String address = "F:\\TiveQP\\IBFTree\\2w_1km\\leafnode 15.txt";

        List<Long> time = new ArrayList<>();

        for (int i = 0; i < 101; i++) {
            long start_read = System.currentTimeMillis();

            //  read twinlist
            Byte[][] twinlist_left = ReadFiledata.readArray(address);
            Byte[][] twinlist_right = ReadFiledata.readArray(address);

            long end_read = System.currentTimeMillis();

            long t = end_read - start_read;
            time.add(t);

            System.out.println(t);
        }
        long sum = 0 ;
        for (int i = 1; i < time.size(); i++) {
            sum = time.get(i) + sum;
        }
        System.out.println("ave: "+sum/100);


    }
}
