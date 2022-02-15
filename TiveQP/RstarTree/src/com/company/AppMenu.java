package com.company;

import com.carrotsearch.sizeof.RamUsageEstimator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The class of the menu of program.
 */
public class AppMenu {

    public void invokeMenu() throws Exception {
        // create a scanner so we can read the command-line input
        Scanner scanner = new Scanner(System.in);

        System.out.println("1) Build R* Tree from csv file");
        System.out.println("2) Exit");

        String option;
        String csvfile = "";
        int dim = 0;
        do {
            System.out.print("Choose one of the above: ");

            option = "1";
//            option = scanner.next();

            switch (option) {
                case "1":
                    System.out.print("How many dimensions: ");
                    dim = 5;
//                    dim = scanner.nextInt();
                    System.out.print("Name of csv file: ");
                    csvfile = "No5.csv";
//                    csvfile = scanner.next();
                    break;
                case "2":
                    System.out.println("Bye Bye!");
                    return;
            }
        } while (!option.equals("1"));

        //Create Datafile from csv
        CSVParser csvParser = new CSVParser(2, csvfile);
        csvParser.CSVParsing();
        csvParser.writeDataFile();

        // Initialise R-Tree and build it
        long start_createTree = System.currentTimeMillis();
        RTree rtree = new RTree(2,csvfile);
        rtree.BuildRTree();
        rtree.WriteIndexFile();
        long end_createTree = System.currentTimeMillis();

        System.out.println();
        System.out.println("R* Tree is ready!");
        System.out.println("R* Tree size : "+RamUsageEstimator.sizeOf(rtree));
        System.out.println("R* Tree time : "+(end_createTree - start_createTree) +"ms");
        System.out.println();

        String innerOption;
        List<Double> givenCoor = new ArrayList<>();

        System.out.println("What do you want to do?");
        System.out.println("1) Insert point");
        System.out.println("2) Find k nearest neighbors in a point");
        System.out.println("3) Find all the points inside an area");
        System.out.println("4) Exit");
        System.out.print("Choose one of the above: ");

        innerOption = "2";
//        innerOption = scanner.next();

        switch (innerOption) {
            case "1":
                System.out.println("---Insert---");
                System.out.print("Give ID: ");
                String id = scanner.next();
                this.typeCoordinates(dim, scanner, givenCoor);
                //Insert Record
                rtree.InsertNewEntry(dim, givenCoor, id);
                System.out.println("Given Point Inserted");
                break;
            case "2":
                System.out.println("---KNN---");
                this.typeCoordinates(dim, scanner, givenCoor);
                System.out.print("Give number of nearest neighbors: ");
                int k_neighbors = 10;
//                int k_neighbors = scanner.nextInt();
                // Call KNN

                System.out.println("R* Tree K Nearest Neighbors:");
                rtree.kNNQuery(dim, k_neighbors, givenCoor);

                System.out.println();

//                System.out.println("Serial K Nearest Neighbors:");
//                SerialActions serialActions1 = new SerialActions(dim, givenCoor, k_neighbors);
//                serialActions1.Knn();
                break;
            case "3":
                System.out.println("---Range Query---");
                System.out.print("Give 1st Coordinates: ");
                for (int i = 0; i < dim; i++) {
                    givenCoor.add(scanner.nextDouble());
                }
                System.out.print("Give 2nd Coordinates: ");
                for (int i = 0; i < dim; i++) {
                    givenCoor.add(scanner.nextDouble());
                }
                //Call RQ
                System.out.println("R* Tree Range Query Results:");
                rtree.RangeQuery(dim, givenCoor);

                System.out.println();

                System.out.println("Serial Range Query Results:");
                SerialActions serialActions2 = new SerialActions(dim, givenCoor);
                serialActions2.RQ();
                break;
            case "4":
                System.out.println("Bye Bye!");
                return;
        }
        System.out.println("_________________________________________________");
    }

    /**
     * Reused code for getting coordinates from user
     * @param dim
     * @param scanner
     * @param givenCoor
     */
    private void typeCoordinates(int dim, Scanner scanner, List<Double> givenCoor){
        System.out.print("Give Coordinates: ");
        double type = 417;
        double lat = 28.5570715;
        double lng = -80.8041368;
        double open= 9;
        double close =17;
        givenCoor.add(type);
        givenCoor.add(lat);
        givenCoor.add(lng);
        givenCoor.add(open);
        givenCoor.add(close);
//        for (int i = 0; i < dim; i++) {
//            givenCoor.add(Double.parseDouble(scanner.next()));
//        }
    }

}
