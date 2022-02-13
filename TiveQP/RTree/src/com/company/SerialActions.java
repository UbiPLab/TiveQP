package com.company;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for the Serial Actions of K Nearest Neighbors and Range Queries
 */
public class SerialActions {

    private final int dim; //number of dimensions given by user
    private final List<Double> givenCoor; //list of coordinates given by user
    private final int k_neighbors; //number of neighbors given by user

    /**
     * Constructor of class SerialActions for K Neighbors
     * @param dim
     * @param givenCoor
     * @param k_neighbors
     */
    SerialActions(int dim, List<Double> givenCoor, int k_neighbors){
        this.dim = dim;
        this.givenCoor = new ArrayList<>(givenCoor);
        this.k_neighbors = k_neighbors;
    }

    /**
     * Constructor of class SerialActions for Range Queries
     * @param dim
     * @param givenCoor
     */
    SerialActions(int dim, List<Double> givenCoor){
        this.dim = dim;
        this.givenCoor = new ArrayList<>(givenCoor);
        this.k_neighbors = 0;
    }

    /**
     * K-nn Neighbors Query doing Serial-Searching on the datafile (For comparing purposes)
     * @throws Exception
     */
    public void Knn() throws Exception {

        SerialKNNeighbors skn = new SerialKNNeighbors(dim, k_neighbors, givenCoor);

        long startTime = System.nanoTime();
        skn.Calculate_KN_Neighbors();
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        System.out.println("SerialKNNeighbors: " + duration/1000000 + "ms");
        System.out.println("SerialKNNeighbors: " + duration + "ns");

    }

    /**
     * Range Query doing Serial-Searching on the datafile. (For comparing purposes)
     * @throws Exception
     */
    public void RQ() throws Exception {

        SerialRangeQueries srq = new SerialRangeQueries(dim, givenCoor);

        long startTime = System.nanoTime();
        srq.RangeQuery();
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        System.out.println("SerialRangeQueries: " + duration/1000000 + "ms");
        System.out.println("SerialRangeQueries: " + duration + "ns");
    }
}
