package com.company;

import java.util.List;

public class Record {
    private final String id;
    private final List<Double> info;
    private final int line;
    private final int blockID;
    Record(String id, List<Double> info, int line, int blockID){
        this.id = id;
        this.info = info;
        this.line = line;
        this.blockID = blockID;
    }
    public List<Double> getInfo() {
        return info;
    }
    public String getId() {
        return id;
    }
    public int getLine() {
        return line;
    }
    public int getBlockID() {
        return blockID;
    }
}