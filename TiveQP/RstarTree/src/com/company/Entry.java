package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Objects of this class are used to store entries from the csv file before inserted in the datafile
 */
public class Entry {

    private final String id;
    private final List<Double> coordinates;
    private List<String> name;

    /**
     * Constructor of the class Entry
     * @param id
     * @param coordinates
     * @param name
     */
    Entry(String id, List<Double> coordinates, List<String> name){
        this.id = id;
        this.coordinates = new ArrayList<>(coordinates);
        this.name = new ArrayList<>(name);
    }

    /**
     * Second constructor of the class Entry
     * @param id
     * @param coordinates
     */
    Entry(String id, List<Double> coordinates){
        this.id = id;
        this.coordinates = new ArrayList<>(coordinates);
    }

    /**
     * Getters of the parameters of the class
     */
    public String getId() {
        return id;
    }

    public String getCoordinates() {
        return Arrays.toString(coordinates.toArray()).
                replace("[", "").
                replace(",", "").
                replace("]", "");
    }

    public String getName() {
        if(name==null){
            return String.valueOf(name);
        } else {
            return name.toString().replace("[", "").
                    replace(",", "").
                    replace("]", "").
                    replace("  ", " ");
        }
    }
}
