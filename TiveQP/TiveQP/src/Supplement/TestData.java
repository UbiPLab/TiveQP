package Supplement;

import IndexBuilding.Owner;
import com.carrotsearch.sizeof.RamUsageEstimator;

import static ReadFileData.ReadFiledata.readArray_String;

public class TestData {
    public static void main(String[] args) throws Exception {
        String fileName ="E:\\Gao\\0TiveQP\\DataSet\\Type\\2w_random.txt";

        String[][] dataSet = readArray_String(fileName);

        System.out.println("Read File ok");
        System.out.println(RamUsageEstimator.sizeOf(dataSet));

        String[][] Location = new String[dataSet.length][];
        String[][] LCS = new String[dataSet.length][];
        String[][] Time = new String[dataSet.length][];
        String[][] TCS = new String[dataSet.length][];
        
        for (int i = 0; i < dataSet.length; i++) {
//            System.out.println(i+","+dataSet[i][0]+"**"+dataSet[i][1]+"**"+dataSet[i][2]+"**"+dataSet[i][3]+"**"+dataSet[i][4]);
            Location[i] = new Owner().OwnerLocation(dataSet[i][1],Double.parseDouble(dataSet[i][2]),Double.parseDouble(dataSet[i][3]));
            LCS[i] = new Owner().OwnerLoation_Complement (dataSet[i][1],Double.parseDouble(dataSet[i][2]),Double.parseDouble(dataSet[i][3]));

            //  time
            Time[i] = new Owner().OwnerTime(Integer.valueOf(dataSet[i][4]),Integer.valueOf(dataSet[i][5]),Integer.valueOf(dataSet[i][6]),Integer.valueOf(dataSet[i][7]));
            TCS[i] = new Owner().OwnerTime_Complement(Integer.valueOf(dataSet[i][4]),Integer.valueOf(dataSet[i][5]),Integer.valueOf(dataSet[i][6]),Integer.valueOf(dataSet[i][7]));

        }
    }
}
