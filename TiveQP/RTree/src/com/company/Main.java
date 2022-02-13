package com.company;

/**
 * Main class of the project.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        //  471	28.5570715	-80.8041368	9	17
        AppMenu appMenu = new AppMenu();
        appMenu.invokeMenu();

        /** Queries for R-tree  **/         // TODO: WE NEED TO RUN THOSE SEPERATELY DUE TO STREAM PROBLEMS
        //rtree.InsertNewEntry(2);       // (DONE!) Inserts new entry into the tree    //CSV AND DATAFILE SHOULD HAVE NO BLANK LINE AT THE END

        //rtree.RangeQuery(2);              // (DONE!) Prints the old_datafile line for every entry that belongs to the query

        //rtree.kNNQuery(2,5);              // (DONE!) Prints the info of the knn query

	}

}
