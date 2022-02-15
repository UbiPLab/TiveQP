package com.company;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class for the Serial Range Query action.
 */
public class SerialRangeQueries {

    private final int dim;
    private final List<Double> givenCoor;
    private final String datafile;
    private final List<Records> inRange;

    /**
     * Constructor of the class SerialRangeQueries
     * @param dim
     * @param givenCoor
     */
    SerialRangeQueries(int dim, List<Double> givenCoor){
        this.dim = dim;
        this.givenCoor = new ArrayList<>(givenCoor);
        this.datafile = "datafile";
        inRange = new ArrayList<>();
    }

    public void RangeQuery() throws Exception{
        String string;
        String[] parts;
        List<Double> dimensions = new ArrayList<>();
        Scanner scanner = new Scanner(new File(datafile));

        String lines = scanner.nextLine(); // read first line of file (metadata)
        for (int i = 0; i < Integer.parseInt(lines)-1; i++) { // skip metadata
            scanner.nextLine();
        }

        while(scanner.hasNextLine()){
            string = scanner.nextLine();

            parts = string.split("\\s+");

            for (int i = 0; i < dim; i++) {
                dimensions.add(Double.valueOf(parts[i+1])); //get all the dimensions of a record
            }

            int counter = 0;

            // check if dimensions of a record is in range
            for (int i = 0; i < givenCoor.size()/dim; i++) {

                if (dimensions.get(i)>=Math.min(givenCoor.get(i), givenCoor.get(i+dim))
                        && dimensions.get(i)<=Math.max(givenCoor.get(i), givenCoor.get(i+dim))){
                    counter++;
                }
            }
            if(counter==dim){
                inRange.add(new Records(parts[0], new ArrayList<>(dimensions)));
            }
            dimensions.clear();
        }
        scanner.close();  //closes the scanner

        //print the findings
        for (Records record: inRange) {
            record.showRecord();
        }
    }
}
