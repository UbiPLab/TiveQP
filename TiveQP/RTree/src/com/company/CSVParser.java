package com.company;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class for the csv parsing to create the datafile.
 */
public class CSVParser {

    private final int dimensions; //number of dimensions given by user
    private final String filename; //name of the csv file given by user
    private final List<Entry> entries;

    CSVParser(int dimensions, String filename){
        this.dimensions = dimensions;
        this.filename = filename;
        entries = new ArrayList<>();
    }

    /**
     * Function for collecting all the entries from the csv file and inserting them in a list
     * @throws FileNotFoundException
     */

    public void CSVParsing() throws FileNotFoundException {
        List<Double> coordinates = new ArrayList<>();
        List<String> name = new ArrayList<>();
        String string;
        String[] parts;

        Scanner scanner = new Scanner(new File(filename));
        while(scanner.hasNextLine()){
            string = scanner.nextLine();

//            parts = string.split("\\*\\*");
            parts = string.split("\\s+");

            for (int i = 0; i < dimensions; i++) { //loop to collect all the possible coordinates of an entry
                coordinates.add(Double.valueOf(parts[i+1]));
            }

            if (parts.length > dimensions + 1){ //if name exists
                for (int i = dimensions+1; i < parts.length; i++) {
                    name.add(parts[i]+" ");
                }
                entries.add(new Entry(parts[0], coordinates, name));
            } else {
                entries.add(new Entry(parts[0], coordinates));
            }
            name.clear();
            coordinates.clear();

        }
        scanner.close();
    }

    /**
     * Function to write the datafile from the previous created list of entries from the csv file
     */
    public void writeDataFile(){
        //New file object, it will create the file if one doesn't exist.
        File file = new File("datafile");

        BufferedWriter bf = null;

        try{

            //Create new BufferedWriter for the output file.
            bf = new BufferedWriter(new FileWriter(file));
            BufferedWriter finalBf = bf;

            int length;
            int bytesLeft=32768;
            int totalBytes;
            int blocks = 1;
            int lines = 1;
            StringBuilder block1 = new StringBuilder();

            //Iterate map entries.
            //First loop is to find and write all the metadata, thus the block no.1

            for (Entry entry : entries) {
                String tempString = entry.getId() + " " + entry.getCoordinates() + " " + entry.getName();
                length = tempString.getBytes(StandardCharsets.UTF_8).length + 2;
                totalBytes = bytesLeft - length;

                if (totalBytes >= 0) {
                    bytesLeft -= length;
                    lines += 1;
                } else {                   // if we exceed 32kb, we end this block, write its lines and its total bytes
                    blocks += 1;
                    block1.append(blocks);
                    block1.append(" ").append(lines);
                    block1.append(" ").append(32768 - bytesLeft).append("\n");
                    lines = 1;
                    bytesLeft = 32768 - length;
                }
            }

            blocks += 1;                   // this is the last block with the last bytes (not full)
            block1.append(blocks);
            block1.append(" ").append(lines);
            block1.append(" ").append(32768 - bytesLeft).append("\n");


            // Here we calculate the bytes for the first metadata-block and then write the metadata in the file.

            String tempString = 1 + " " + blocks + " ";
            int block1bytes = tempString.getBytes().length + block1.toString().getBytes().length + 2;
            // The " - (blocks)" is to eliminate all "\n" characters from byte counting.
            block1bytes = block1bytes + String.valueOf(block1bytes).getBytes().length - (blocks)
                    + String.valueOf(blocks+1).getBytes().length;
            finalBf.write(String.valueOf(blocks+1));
            finalBf.newLine();
            finalBf.write(tempString + block1bytes);
            finalBf.newLine();
            finalBf.write(block1.toString());

            // Second loop is to write the actual node information in the datafile

            for (Entry entry : entries) {

                try {
                    tempString = entry.getId() + " " + entry.getCoordinates() + " " + entry.getName();
                    finalBf.write(tempString);              // Write the entry info
                    finalBf.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            bf.flush();

        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try{
                //Close the writer.
                assert bf != null;
                bf.close();
            }catch(Exception ignored){}
        }
    }
}
