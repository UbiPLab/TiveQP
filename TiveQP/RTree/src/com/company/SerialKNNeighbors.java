package com.company;

import java.io.*;
import java.util.*;

/**
 * Class for the Serial K Nearest Neighbors action.
 */
public class SerialKNNeighbors {

    private final int dim;
    private final int k_neighbors;
    private final List<Double> givenCoor;
    private final String datafile;
    private PriorityQueue<Records> pq;

    /**
     * Constructor of the class SerialKNeighbors
     * @param dim
     * @param k_neighbors
     * @param givenCoor
     */
    SerialKNNeighbors(int dim, int k_neighbors, List<Double> givenCoor){
        this.dim = dim;
        this.k_neighbors = k_neighbors;
        this.givenCoor = new ArrayList<>(givenCoor);
        this.datafile = "datafile";
    }

    public void Calculate_KN_Neighbors() throws Exception{
        pq = new PriorityQueue<>(k_neighbors, Comparator.comparing(Records::getDistance).reversed());
        String string;
        String[] parts;
        double dist = 0;
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
                dimensions.add(Double.valueOf(parts[i+1]));
            }
            //finding the distance between a record from datafile and the given point
            for (int i = 0; i < dim; i++) {
                dist += Math.pow(dimensions.get(i)-givenCoor.get(i),2);
            }
            dist = Math.sqrt(dist);

            if (pq.size() >= k_neighbors){
                pq.add(new Records(parts[0], new ArrayList<>(dimensions), dist));
                pq.poll();
            } else {
                pq.add(new Records(parts[0], new ArrayList<>(dimensions), dist));
            }
            dist = 0;
            dimensions.clear();
        }

        scanner.close();  //closes the scanner

        //print the findings
        for (int i = 0; i < k_neighbors; i++) {
            Objects.requireNonNull(pq.poll()).showRecord();
        }
    }
}
