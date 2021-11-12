package AdvanceTest;

import com.carrotsearch.sizeof.RamUsageEstimator;

public class TestOutSize {
    public static void main(String[] args) {
        String[] strs = {"sadasd","sadsa"};
        int  a =12;
        System.out.println(RamUsageEstimator.sizeOf(strs));
        System.out.println(RamUsageEstimator.sizeOf(a));
    }
}
