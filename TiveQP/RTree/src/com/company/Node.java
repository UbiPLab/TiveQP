package com.company;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private double[][] mbr;

    private boolean leaf;
    private int dim;
    private List<Record> records;
    private List<Node> children;
    private Node parent;

    Node(int dim, Node parent){
        this.parent = parent;
        this.dim = dim;
        this.leaf=true;
        mbr = new double[dim][2];
        records = new ArrayList<>();
        children = new ArrayList<>();
    }

    public void adjustMbr( Record newEntry ){
        for(int i=0;i<dim;i++){

            double entryCoord = newEntry.getInfo().get(i);

            // In every dimension axis check if we need to adjust any of the upper or lower bounds of the mbr.
            if(entryCoord<mbr[i][0] || mbr[i][0]==0){         //Check lower bound of mbr
                mbr[i][0] = entryCoord;
            }
            if(entryCoord>mbr[i][1] || mbr[i][1]==0){         //Check upper bound of mbr
                mbr[i][1] = entryCoord;
            }
        }
    }

    public void adjustMbr( Node newNode ){

        for(int i=0;i<dim;i++){

            // In every dimension axis check if we need to adjust any of the upper or lower bounds of the mbr.
            if(newNode.getMbr()[i][0]<mbr[i][0] || mbr[i][0]==0){         //Check lower bound of mbr
                mbr[i][0] = newNode.getMbr()[i][0];
            }
            if(newNode.getMbr()[i][1]>mbr[i][1] || mbr[i][1]==0){         //Check upper bound of mbr
                mbr[i][1] = newNode.getMbr()[i][1];
            }

        }
    }

    public void clearMbr(){
        mbr = new double[dim][2];
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public List<Node> getChildren() {
        return children;
    }

    public List<Record> getRecords() {
        return records;
    }

    public double[][] getMbr() {
        return mbr;
    }
}
