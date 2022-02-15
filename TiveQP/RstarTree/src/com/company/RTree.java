package com.company;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;

/**
 * The class for building the R* Tree
 */
public class RTree {

    final int M = 4;  // MAX bound of entries in a node
    final int m = 2;  // min bound of entries in a node

    private final int dim;
    private final String csvfile;
    private final Node root;

    private final List<Integer> block_bytes = new ArrayList<>();
    private final List<Integer> block_lines = new ArrayList<>();
    private List<String> lines = new ArrayList<>();

    RTree(int dim, String csvfile) {
        this.dim = dim;
        this.csvfile = csvfile;

        // Initialise root
        this.root = new Node(dim,null);
    }

    /**
     * Function to get the metadata from datafile created after building the R* Tree
     * @throws IOException
     */
    private void Get_Metadata() throws IOException {
        FileInputStream fis = new FileInputStream(new File("datafile"));

        //Construct BufferedReader from InputStreamReader
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        String number_of_blocks = br.readLine();

        block_bytes.add(Integer.parseInt(number_of_blocks));
        block_lines.add(1);
        String string;
        String[] parts;

        for (int i = 0; i < Integer.parseInt(number_of_blocks)-1; i++) {
            string = br.readLine();

            parts = string.split("\\s+");

            block_lines.add(Integer.parseInt(parts[1]));
            block_bytes.add(Integer.parseInt(parts[2]));
        }

        lines = Files.readAllLines(Paths.get("datafile"));

        br.close();
    }

    /**
     * Function to get the records from the datafile.
     * @param line
     * @param block
     */
    public void Results_Datafile(int line, int block) {

        int skip_lines = 1;
        for (int i = 1; i < block; i++) {
            skip_lines += block_lines.get(i);
        }

        System.out.println(lines.get(skip_lines + line-1));
    }

    /**
     * Function to insert new entry in R* Tree.
     * @param dim
     * @param givenCoor
     * @param id
     * @throws IOException
     */
    public void InsertNewEntry(int dim, List<Double> givenCoor, String id) throws IOException {

        FileWriter csvWriter = new FileWriter(csvfile,true);
        csvWriter.append('\n');
        csvWriter.append(id);
        csvWriter.append(" ");
        for(int i=0;i<dim;i++){
            csvWriter.append(givenCoor.get(i).toString());
            csvWriter.append(' ');
        }
        csvWriter.close();

        CSVParser csvParser = new CSVParser(dim, csvfile);
        csvParser.CSVParsing();
        csvParser.writeDataFile();
        this.BuildRTree();
    }

    /**
     * The function to perform the range queries.
     * @param dim
     * @param givenCoor
     */
    public void RangeQuery(int dim, List<Double> givenCoor) {

        long startTime = System.nanoTime();

        Scanner scanner = new Scanner(System.in);
        double[][] range = new double[dim][2];
        List<Record> results = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < dim; j++) {
                range[j][i] = givenCoor.get(i*dim+j);
            }
        }

        for (int i = 0; i < dim; i++) {
            if(range[i][0]>range[i][1]){
                double temp = range[i][0];
                range[i][0] = range[i][1];
                range[i][1] = temp;
            }
        }

        for (int i = 0; i< dim; i++){
            range[i][0] -= 0.00000001;
            range[i][1] += 0.00000001;
        }

        LinkedList<Node> queue = new LinkedList<>();
        Node currNode;
        queue.add(root);

        while (queue.size() != 0) {
            currNode = queue.poll();
            boolean flag=true;
            for(int i=0;i<dim;i++){
                if(Calculate_OverlapValue(range, currNode.getMbr())==0 ){
                    flag=false;
                }
            }
            if(flag){
                if (currNode.getChildren().size() != 0) {
                    queue.addAll(currNode.getChildren());
                } else {
                    for(int i=0;i<currNode.getRecords().size();i++){
                        flag=true;
                        for(int j=0;j<dim;j++){
                            if (!(currNode.getRecords().get(i).getInfo().get(j) >= range[j][0] &&
                                    currNode.getRecords().get(i).getInfo().get(j) <= range[j][1])) {
                                flag = false;
                                break;
                            }
                        }
                        if(flag){
                            results.add(currNode.getRecords().get(i));
                        }
                    }
                }
            }
        }
        scanner.close();  // Closes the scanner
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.

        for (Record result : results) {
            //System.out.println(results.get(i).getLine() + " " + results.get(i).getBlockID());
            Results_Datafile(result.getLine(), result.getBlockID());
        }
        System.out.println("R* Tree RangeQueries: " + duration/1000000 + "ms");
        System.out.println("R* Tree RangeQueries: " + duration + "ns");
    }

    /**
     * Function to get the k nearest neighbors of a point.
     * @param dim
     * @param knn
     * @param givenCoor
     */
    public void kNNQuery(int dim, int knn, List<Double> givenCoor) {
        long startTime = System.nanoTime();

        Scanner scanner = new Scanner(System.in);
        double[] point = new double[dim];
        Record[] results = new Record[knn];
        double[] distances = new double[knn];
        double maxdist=0;
        int inserted=0;

        for(int i=0;i<dim;i++){
            point[i] = givenCoor.get(i);
        }

        LinkedList<Node> queue = new LinkedList<>();
        Node currNode;
        int childSelected;
        queue.add(root);

        while (queue.size() != 0) {
            currNode = queue.poll();
            childSelected=0;
            double mindist = Double.MAX_VALUE;

            if (currNode.getChildren().size() == 0) {
                for (int i=0; i<currNode.getRecords().size();i++){
                    if(inserted<knn){
                        results[inserted]=currNode.getRecords().get(i);
                        distances[inserted]= euclideanDist(point,currNode.getRecords().get(i).getInfo());
                        inserted++;
                        if(distances[inserted-1]<maxdist){
                            maxdist=distances[inserted-1];
                        }
                        if(inserted==knn){
                            parallelBubbleSort(distances,results);
                        }
                    }else{
                        boolean flag=true;
                        int pos=knn;
                        for(int j=knn-1;j>=0 && flag;j--){
                            if(euclideanDist(point,currNode.getRecords().get(i).getInfo()) < distances[j]){
                                pos=j;
                            }else{
                                flag=false;
                            }
                        }
                        if(pos!=knn){
                            distances[knn-1] = euclideanDist(point,currNode.getRecords().get(i).getInfo());
                            results[knn-1] = currNode.getRecords().get(i);
                            if(distances[knn-1]<maxdist){
                                maxdist=distances[knn-1];
                            }
                            parallelBubbleSort(distances,results);
                        }
                    }
                }
            }else {
                for (int i = 0; i < currNode.getChildren().size();i++) {
                    double nodeDist = MINDIST(point, currNode.getChildren().get(i).getMbr());
                    if (nodeDist < mindist) {
                        mindist = nodeDist;
                        childSelected = i;
                    }
                }
                for (int i =0; i < currNode.getChildren().size();i++) {
                    if(i!=childSelected){
                        queue.add(currNode.getChildren().get(i));
                    }
                }
                queue.add(currNode.getChildren().get(childSelected));
            }
        }

        long endTime = System.nanoTime();

        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        for (int i = 0; i < knn; i++) {
            Results_Datafile(results[i].getLine(), results[i].getBlockID());
        }
        System.out.println("R* Tree KNN: " + duration/1000000 + "ms");
        System.out.println("R* Tree KNN: " + duration + "ns");
        scanner.close();  // Closes the scanner
    }

    static void parallelBubbleSort(double[] arr, Record[] arr2) {
        int n = arr.length;
        double temp;
        Record temp2;
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {
                if (arr[j - 1] > arr[j]) {
                    //swap elements
                    temp = arr[j - 1];
                    arr[j - 1] = arr[j];
                    arr[j] = temp;
                    temp2 = arr2[j - 1];
                    arr2[j - 1] = arr2[j];
                    arr2[j] = temp2;
                }
            }
        }
    }

        public double euclideanDist(double[] point, List<Double> record){
        double dist = 0 ;
        for(int i=0;i<dim;i++){
            dist += Math.pow(point[i]-record.get(i),2);
        }
        dist = Math.sqrt(dist);
        return dist;
    }

    public double MINDIST(double[] point, double[][] mbr){
        double rj, pj;
        double sum = 0;

        for(int i=0;i<dim;i++){
            pj = point[i];
            if(pj<mbr[i][0]) {
                rj = mbr[i][0];
            }else rj = Math.min(pj, mbr[i][1]);

            sum+=Math.pow(Math.abs(pj-rj),2);
        }
        sum = Math.sqrt(sum);
        return sum;
    }

    // Construct the IndexFile based on the R-Tree made from function BuildRTree.

    public void WriteIndexFile() throws FileNotFoundException {
        Bfs(root);
    }

    private void Bfs(Node root) throws FileNotFoundException {

        PrintWriter out = new PrintWriter("indexfile");
        int id=0;
        int fatherId;
        int childrenId=1;
        LinkedList<Node> queue = new LinkedList<>();
        LinkedList<Integer> queueF = new LinkedList<>();
        queue.add(root);
        queueF.add(id);
        Node currNode;

        while (queue.size() != 0) {
            currNode = queue.poll();
            fatherId = queueF.poll();

            //START OF PRINTING A NODE'S DETAILS

            id++;
            out.println(id);
            for(int i=0;i<dim;i++){
                out.print(currNode.getMbr()[i][0] + " ");
            }
            for(int i=0;i<dim;i++){
                out.print(currNode.getMbr()[i][1] + " ");
            }
            if(currNode.getChildren().size()!=0){
                out.print("\n");
            }
            for(int i=0;i<currNode.getChildren().size();i++){
                childrenId++;
                out.print(childrenId + " ");
            }
            if(currNode.getChildren().size()==0){                 // THIS IS TO LOOK FOR FIRST CHILD FOUND, SO THAT WE

                out.println();
                for(int i=0;i<currNode.getRecords().size();i++){
                    for(int j=0; j<dim;j++){
                        out.print(currNode.getRecords().get(i).getInfo().get(j) + " ");
                    }
                    out.println(currNode.getRecords().get(i).getLine());
                }
                for(int i=0;i<M-currNode.getRecords().size();i++){
                    out.println(-1);
                }
            }
            else{
                out.print("\n");
            }
            out.println(fatherId+"\n");

            //END OF PRINTING A NODE'S DETAILS

            for(int i=0;i<currNode.getChildren().size();i++){
                queue.add(currNode.getChildren().get(i));
                queueF.add(id);
            }
        }
        out.close();
    }

    /**
     * The main function of the class for building the tree.
     * @throws IOException
     */
    public void BuildRTree() throws IOException {
        // Parsing a CSV file into Scanner class constructor
        Scanner scanner = new Scanner(new File("datafile"));
        String string;
        String[] parts;
        List<Double> coordinates;
        List<String> metadata = new ArrayList<>();
        int last_line_hack = 0;
        // Create a list with the number of lines in each block from the metadata
        metadata.add(scanner.nextLine()); // first element of the list the lines of the metadata
        for (int i = 0; i < Integer.parseInt(metadata.get(0))-1; i++) {
            string = scanner.nextLine();
            parts = string.split("\\s+");
            metadata.add(parts[1]);
        }

        // Loop through the CSVFile lines to get id and coordinates based on dimension parameter
        // We need the following csv format:
        // 1st element = id
        // Rest dimension elements (e.g. 2) = coordinates
        for (int i = 2; i < Integer.parseInt(metadata.get(0)); i++) {
            if (i==Integer.parseInt(metadata.get(0))-1){
                last_line_hack = 1;
            }
            for (int j = 0; j < Integer.parseInt(metadata.get(i))-last_line_hack; j++) {
                string = scanner.nextLine();
                parts = string.split("\\s+");
                coordinates = new ArrayList<>();
                for (int q = 0; q < dim; q++) {
                    coordinates.add(Double.valueOf(parts[q+1]));
                }
                Insert(new Record(parts[0],coordinates,j+1, i), root); // j is line inside block, i is blockID
            }
        }
        scanner.close();  // Closes the scanner
        Get_Metadata();
    }

    /**
     * Function to insert a new entry into the R*Tree.
     * @param entry
     * @param currNode
     */
    private void Insert(Record entry, Node currNode){

        // Choose in which node we will try to insert the new entry
        Node selected = ChooseSubtree(currNode, entry);

        // If node has less than M entries, then insert the entry into this node.
        if (selected.getRecords().size()<M){
            selected.getRecords().add(entry);
            selected.adjustMbr(entry);
        }

        // Else if node has M entries, then invoke OverflowTreatment
        else{
            OverflowTreatment( selected, entry);
        }

    }

    /**
     * According to the papers about the R*tree, the OverflowTreatment function should try to first Remove p elements
     * from the overflown node, and then ReInsert them to the tree again to sometimes prevent splits.
     * However, since we were asked to not implement a Remove/Delete function, we are also not able to implement the
     * ReInsert function as well.
     *
     * As a result, the OverflowTreatment function will only invoke the Split function.
     * @param overNode
     * @param extraEntry
     */
    private void OverflowTreatment( Node overNode, Record extraEntry ){
        Split_Leaf(overNode,extraEntry);
    }

    /**
     * Function to choose the appropriate insertion path and reach the node where we will insert a new entry.
     * @param currNode
     * @param entry
     * @return
     */
    private Node ChooseSubtree(Node currNode, Record entry){

        currNode.adjustMbr(entry);
        // If N is leaf, return N
        if (currNode.getChildren().size()==0) {
            return currNode;
        }

        // Else if the childpointers of N point to leaves, choose the child whose rectangle needs least overlap enlargement
        else if(currNode.getChildren().get(0).isLeaf()){
            int numOfChildren = currNode.getChildren().size();
            double [] Overlap = new double[numOfChildren];
            for (int i=0;i<numOfChildren;i++){
                for (int j=0;j<numOfChildren;j++){
                    if(i!=j){
                        Overlap[i]-=Calculate_OverlapValue(currNode.getChildren().get(i).getMbr(),currNode.getChildren().get(j).getMbr());
                    }
                }
                Node temp = new Node(dim, null);
                temp.adjustMbr(entry);
                temp.adjustMbr(currNode.getChildren().get(i));
                for (int j=0;j<numOfChildren;j++){
                    if(i!=j){
                        Overlap[i]+=Calculate_OverlapValue(temp.getMbr(),currNode.getChildren().get(j).getMbr());
                    }
                }
            }
            int minNode=0;
            double minOverlap = Overlap[0];
            for (int i=1;i<numOfChildren;i++){
                if (Overlap[i]<minOverlap){
                    minOverlap=Overlap[i];
                    minNode=i;
                }
            }

            return ChooseSubtree(currNode.getChildren().get(minNode),entry);
        }

        // Else if the childpointers of N point to non-leaves, choose child whose rectangle needs least area enlargement
        else{
            int numOfChildren = currNode.getChildren().size();
            double [] Area = new double[numOfChildren];
            for (int i=0;i<numOfChildren;i++){
                Area[i]-=Calculate_Area(currNode.getChildren().get(i).getMbr());
                Node temp = new Node(dim, null);
                temp.adjustMbr(entry);
                temp.adjustMbr(currNode.getChildren().get(i));
                Area[i]+=Calculate_Area(temp.getMbr());
            }
            int minNode=0;
            double minArea = Area[0];
            for (int i=1;i<numOfChildren;i++){
                if (Area[i]<minArea){
                    minArea=Area[i];
                    minNode=i;
                }
            }
            return ChooseSubtree(currNode.getChildren().get(minNode),entry);
        }
    }


    /**
     * Function to perform a node split ( NON-LEAF ).
     * @param parent
     * @param extraNode
     */
    private void Split_NonLeaf(Node parent, Node extraNode){
        int axis;
        List<List<Node>> bestDistribution;

        //Invoke ChooseSplitAxis_NonLeaf to determine the axis of the performed split
        axis = ChooseSplitAxis_NonLeaf(parent, extraNode);

        //Invoke ChooseSplitIndex_NonLeaf to determine the best distribution into 2 groups along the axis selected
        bestDistribution = ChooseSplitIndex_NonLeaf(parent, extraNode, axis);

        //If we are at root node, then create 2 new nodes
        if (parent.getParent()==null) {
            parent.adjustMbr(extraNode);

            Node child1 = new Node(dim, parent);
            Node child2 = new Node(dim, parent);

            for (int i = 0; i < bestDistribution.get(0).size(); i++) {          //Node of group no.1
                bestDistribution.get(0).get(i).setParent(child1);
                child1.getChildren().add(bestDistribution.get(0).get(i));
                child1.adjustMbr(bestDistribution.get(0).get(i));
            }
            for (int i=0;i<bestDistribution.get(1).size();i++){                 //Node of group no.2
                bestDistribution.get(1).get(i).setParent(child2);
                child2.getChildren().add(bestDistribution.get(1).get(i));
                child2.adjustMbr(bestDistribution.get(1).get(i));
            }
            parent.getChildren().clear();
            parent.getChildren().add(child1);
            parent.getChildren().add(child2);
        }
        //Else if parent node has M children, then split him too and go upwards
        else if(parent.getParent().getChildren().size()==M){
            Node child = new Node(dim,null);
            for (int i=0;i<bestDistribution.get(0).size();i++){                 //Node of group no.1
                bestDistribution.get(0).get(i).setParent(child);
                child.getChildren().add(bestDistribution.get(0).get(i));
                child.adjustMbr(bestDistribution.get(0).get(i));
            }
            parent.getChildren().clear();
            parent.clearMbr();
            for (int i=0;i<bestDistribution.get(1).size();i++){                 //Node of group no.2
                bestDistribution.get(1).get(i).setParent(parent);
                parent.getChildren().add(bestDistribution.get(1).get(i));
                parent.adjustMbr(bestDistribution.get(1).get(i));
            }

            // RECURSION!
            // Now the parent node has more than M children so we have to split them.
            Split_NonLeaf(parent.getParent(),child);
        }
        //Else if parent node has less than M children, then create only 1 new node;
        else if(parent.getParent().getChildren().size()<M){
            parent.getParent().adjustMbr(extraNode);
            Node child = new Node(dim,parent.getParent());
            for (int i=0;i<bestDistribution.get(0).size();i++){                 //Node of group no.1
                bestDistribution.get(0).get(i).setParent(child);
                child.getChildren().add(bestDistribution.get(0).get(i));
                child.adjustMbr(bestDistribution.get(0).get(i));
            }

            parent.getChildren().clear();
            parent.clearMbr();
            for (int i=0;i<bestDistribution.get(1).size();i++){                 //Node of group no.2
                bestDistribution.get(1).get(i).setParent(parent);
                parent.getChildren().add(bestDistribution.get(1).get(i));
                parent.adjustMbr(bestDistribution.get(1).get(i));
            }
            parent.getParent().getChildren().add(child);
        }
    }

    /**
     * Function to provide the new distributions so that we can create new nodes during a split ( NON-LEAF ).
     * @param parent
     * @param extraNode
     * @param axis
     * @return
     */
    private List<List<Node>> ChooseSplitIndex_NonLeaf(Node parent, Node extraNode, int axis) {
        List<Node> sortedNodes1 = new ArrayList<>();
        List<Node> sortedNodes2 = new ArrayList<>();
        List<List<Node>> bestDistribution;

        sortedNodes1.add(extraNode);
        sortedNodes2.add(extraNode);
        for (int j = 0; j < M; j++) {
            sortedNodes1.add(parent.getChildren().get(j));
            sortedNodes2.add(parent.getChildren().get(j));
        }

        // Sort based on the lower value of the rectangles
        sortedNodes1.sort(Comparator.comparingDouble(o -> o.getMbr()[axis][0]));

        // Sort based on the upper value of the rectangles
        sortedNodes2.sort(Comparator.comparingDouble(o -> o.getMbr()[axis][1]));

        bestDistribution = Find_Best_Distribution_NonLeaf(sortedNodes1,sortedNodes2);
        return bestDistribution;
    }


    /**
     * Function to find the best possible distribution during a split ( NON-LEAF )
     * @param sortedNodes1
     * @param sortedNodes2
     * @return
     */
    private List<List<Node>> Find_Best_Distribution_NonLeaf(List<Node> sortedNodes1, List<Node> sortedNodes2){
        double overlap;
        double area;

        List<Node> group1 = new ArrayList<>();
        List<Node> group2 = new ArrayList<>();

        List<List<Node>> bestDistributionOverlap1 = new ArrayList<>();
        List<List<Node>> bestDistributionArea1 = new ArrayList<>();

        double[][] mbr1;
        double[][] mbr2;
        double minOverlap1 = Double.MAX_VALUE;
        double minArea1 = Double.MAX_VALUE;

        for (int k=1;k<=M-2*m+2;k++){
            for (int i=0;i<m-1+k;i++){
                group1.add(sortedNodes1.get(i));
            }
            for (int i=m-1+k;i<sortedNodes1.size();i++){
                group2.add(sortedNodes1.get(i));
            }

            mbr1 = Calculate_Mbr_NonLeaf(group1);
            mbr2 = Calculate_Mbr_NonLeaf(group2);

            overlap = Calculate_OverlapValue(mbr1,mbr2);
            area = Calculate_Area(mbr1) + Calculate_Area(mbr2);

            if(area < minArea1){
                minArea1 = area;
                bestDistributionArea1.clear();
                bestDistributionArea1.add(new ArrayList<>(group1));
                bestDistributionArea1.add(new ArrayList<>(group2));
            }

            if (overlap < minOverlap1){
                minOverlap1 = overlap;
                bestDistributionOverlap1.clear();
                bestDistributionOverlap1.add(new ArrayList<>(group1));
                bestDistributionOverlap1.add(new ArrayList<>(group2));
            }
            group1.clear();
            group2.clear();
        }

        List<List<Node>> bestDistributionOverlap2 = new ArrayList<>();
        List<List<Node>> bestDistributionArea2 = new ArrayList<>();

        double minOverlap2 = Double.MAX_VALUE;
        double minArea2 = Double.MAX_VALUE;

        for (int k=1;k<=M-2*m+2;k++){
            for (int i=0;i<m-1+k;i++){
                group1.add(sortedNodes2.get(i));
            }
            for (int i=m-1+k;i<sortedNodes2.size();i++){
                group2.add(sortedNodes2.get(i));
            }

            mbr1 = Calculate_Mbr_NonLeaf(group1);
            mbr2 = Calculate_Mbr_NonLeaf(group2);

            overlap = Calculate_OverlapValue(mbr1,mbr2);
            area = Calculate_Area(mbr1) + Calculate_Area(mbr2);

            if(area < minArea2){
                minArea2 = area;
                bestDistributionArea2.clear();
                bestDistributionArea2.add(new ArrayList<>(group1));
                bestDistributionArea2.add(new ArrayList<>(group2));
            }

            if (overlap < minOverlap2){
                minOverlap2 = overlap;
                bestDistributionOverlap2.clear();
                bestDistributionOverlap2.add(new ArrayList<>(group1));
                bestDistributionOverlap2.add(new ArrayList<>(group2));
            }
            group1.clear();
            group2.clear();
        }

        if(minOverlap1==0){
            if(minOverlap2!=0){
                return bestDistributionArea1;
            }else{
                if (minArea1<=minArea2){
                    return bestDistributionArea1;
                }else{
                    return bestDistributionArea2;
                }
            }
        }else{
            if(minOverlap2==0){
                return bestDistributionArea2;
            }else{
                if (minOverlap1<=minOverlap2){
                    return bestDistributionOverlap1;
                }else{
                    return bestDistributionOverlap2;
                }
            }
        }
    }

    /**
     * Function to determine the axis, perpendicular to which the split is performed ( NON-LEAF ).
     * @param parent
     * @param extraNode
     * @return
     */
    private int ChooseSplitAxis_NonLeaf(Node parent, Node extraNode){
        double [] S = new double[dim];

        for (int axis=0;axis<dim;axis++){
            List<Node> sortedNodes = new ArrayList<>();

            sortedNodes.add(extraNode);
            for (int j=0;j<M;j++){
                sortedNodes.add(parent.getChildren().get(j));
            }

            int axisUsed = axis;
            for (int i=0;i<2;i++){      // Calculate S according to the upper and lower values of the rectangles
                int iUsed = i;
                sortedNodes.sort(Comparator.comparingDouble(o -> o.getMbr()[axisUsed][iUsed]));
                S[axis] += Calculate_S_NonLeaf(sortedNodes);
            }
        }

        double min = S[0];
        int minAxis = 0;
        for (int i=0; i<dim; i++){
            //System.out.println(S[i]);
            if (S[i]<min){
                min=S[i];
                minAxis=i;
            }
        }
        return minAxis;
    }

    /**
     * Function to calculate the sum of all the margin-values of the different distributions ( NON-LEAF ).
     * @param sortedNodes
     * @return
     */
    private double Calculate_S_NonLeaf(List<Node> sortedNodes){
        double sum = 0;
        List<Node> group1 = new ArrayList<>();
        List<Node> group2 = new ArrayList<>();
        double[][] mbr1;
        double[][] mbr2;

        for (int k=1;k<=M-2*m+2;k++) {
            for (int i = 0; i < m - 1 + k; i++) {
                group1.add(sortedNodes.get(i));
            }
            for (int i = m - 1 + k; i < sortedNodes.size(); i++) {
                group2.add(sortedNodes.get(i));
            }
            mbr1 = Calculate_Mbr_NonLeaf(group1);
            mbr2 = Calculate_Mbr_NonLeaf(group2);

            sum+=Calculate_MarginValue(mbr1);
            sum+=Calculate_MarginValue(mbr2);
            group1.clear();
            group2.clear();
        }
        return sum;
    }

    /**
     * Function to calculate the mbr of each of the two groups during a node-split ( NON-LEAF ).
     * @param group
     * @return
     */
    private double[][] Calculate_Mbr_NonLeaf(List<Node> group){
        double[][] mbr = new double[dim][2];

        for(int i=0;i<dim;i++){
            for (Node node : group) {
                double upper = node.getMbr()[i][1];
                double lower = node.getMbr()[i][0];

                // In every dimension axis check if we need to adjust any of the upper or lower bounds of the mbr.
                if (lower < mbr[i][0] || mbr[i][0] == 0) {         //Check lower bound of mbr
                    mbr[i][0] = lower;
                }
                if (upper > mbr[i][1] || mbr[i][1] == 0) {         //Check upper bound of mbr
                    mbr[i][1] = upper;
                }
            }
        }
        return mbr;
    }

    /**
     * Function to perform a node split ( LEAF ).
     * @param overNode
     * @param extraEntry
     */
    private void Split_Leaf(Node overNode, Record extraEntry){

        int axis;
        List<List<Record>> bestDistribution;

        //Invoke ChooseSplitAxis_Leaf to determine the axis of the performed split
        axis = ChooseSplitAxis_Leaf(overNode, extraEntry);

        //Invoke ChooseSplitIndex_Leaf to determine the best distribution into 2 groups along the axis selected
        bestDistribution = ChooseSplitIndex_Leaf(overNode, extraEntry, axis);

        //If we are at root node, then create 2 new nodes
        if (overNode.getParent()==null){
            overNode.adjustMbr(extraEntry);
            Node child1 = new Node(dim,overNode);
            Node child2 = new Node(dim,overNode);
            for (int i=0;i<bestDistribution.get(0).size();i++){                 //Node of group no.1
                child1.getRecords().add(bestDistribution.get(0).get(i));
                child1.adjustMbr(bestDistribution.get(0).get(i));
            }
            for (int i=0;i<bestDistribution.get(1).size();i++){                 //Node of group no.2
                child2.getRecords().add(bestDistribution.get(1).get(i));
                child2.adjustMbr(bestDistribution.get(1).get(i));
            }
            overNode.getChildren().add(child1);
            overNode.getChildren().add(child2);
            overNode.setLeaf(false);
        }
        //Else if parent node has M children, then split him too and go upwards
        else if(overNode.getParent().getChildren().size()==M){
            Node child = new Node(dim,null);
            for (int i=0;i<bestDistribution.get(0).size();i++){                 //Node of group no.1
                child.getRecords().add(bestDistribution.get(0).get(i));
                child.adjustMbr(bestDistribution.get(0).get(i));
            }
            overNode.getRecords().clear();
            overNode.clearMbr();
            for (int i=0;i<bestDistribution.get(1).size();i++){                 //Node of group no.2
                overNode.getRecords().add(bestDistribution.get(1).get(i));
                overNode.adjustMbr(bestDistribution.get(1).get(i));
            }

            // Now the parent node has more than M children so we have to split them.
            Split_NonLeaf(overNode.getParent(),child);
        }
        //Else if parent node has less than M children, then create only 1 new node;
        else if(overNode.getParent().getChildren().size()<M){
            overNode.getParent().adjustMbr(extraEntry);
            Node child = new Node(dim,overNode.getParent());
            for (int i=0;i<bestDistribution.get(0).size();i++){                 //Node of group no.1
                child.getRecords().add(bestDistribution.get(0).get(i));
                child.adjustMbr(bestDistribution.get(0).get(i));
            }

            overNode.getRecords().clear();
            overNode.clearMbr();
            for (int i=0;i<bestDistribution.get(1).size();i++){                 //Node of group no.2
                overNode.getRecords().add(bestDistribution.get(1).get(i));
                overNode.adjustMbr(bestDistribution.get(1).get(i));
            }
            overNode.getParent().getChildren().add(child);
            overNode.getParent().setLeaf(false);                    //Might be unnecessary
        }
    }

    /**
     * Function to determine the axis, perpendicular to which the split is performed ( LEAF ).
     * @param overNode
     * @param extraEntry
     * @return
     */
    private int ChooseSplitAxis_Leaf(Node overNode, Record extraEntry){
        double [] S = new double[dim];

        for (int axis=0;axis<dim;axis++){
            List<Record> sortedEntries = new ArrayList<>();

            sortedEntries.add(extraEntry);
            for (int j=0;j<M;j++){
                sortedEntries.add(overNode.getRecords().get(j));
            }

            int axisUsed = axis;

            sortedEntries.sort(Comparator.comparingDouble(o -> o.getInfo().get(axisUsed)));

            S[axis] = Calculate_S_Leaf(sortedEntries);
        }

        double min = S[0];
        int minAxis = 0;
        for (int i=0; i<dim; i++){
            //System.out.println(S[i]);
            if (S[i]<min){
                min=S[i];
                minAxis=i;
            }
        }
        return minAxis;
    }


    /**
     * Function to provide the new distributions so that we can create new nodes during a split ( LEAF ).
     * @param overNode
     * @param extraEntry
     * @param axis
     * @return
     */
    private List<List<Record>> ChooseSplitIndex_Leaf(Node overNode, Record extraEntry, int axis) {

        List<Record> sortedEntries = new ArrayList<>();
        List<List<Record>> bestDistribution;

        sortedEntries.add(extraEntry);
        for (int j = 0; j < M; j++) {
            sortedEntries.add(overNode.getRecords().get(j));
        }

        sortedEntries.sort(Comparator.comparingDouble(o -> o.getInfo().get(axis)));

        bestDistribution = Find_Best_Distribution_Leaf(sortedEntries);
        return bestDistribution;
    }


    /**
     * Function to find the best possible distribution during a split ( LEAF ).
     * @param sortedEntries
     * @return
     */
    private List<List<Record>> Find_Best_Distribution_Leaf(List<Record> sortedEntries){
        double [] overlap = new double[M-2*m+2];
        double [] area = new double[M-2*m+2];

        List<Record> group1 = new ArrayList<>();
        List<Record> group2 = new ArrayList<>();

        List<List<Record>> bestDistributionOverlap = new ArrayList<>();
        List<List<Record>> bestDistributionArea = new ArrayList<>();

        double[][] mbr1;
        double[][] mbr2;
        double minOverlap = Double.MAX_VALUE ;
        double minArea = Double.MAX_VALUE ;

        for (int k=1;k<=M-2*m+2;k++){
            for (int i=0;i<m-1+k;i++){
                group1.add(sortedEntries.get(i));
            }
            for (int i=m-1+k;i<sortedEntries.size();i++){
                group2.add(sortedEntries.get(i));
            }

            mbr1 = Calculate_Mbr_Leaf(group1);
            mbr2 = Calculate_Mbr_Leaf(group2);

            overlap[k-1] = Calculate_OverlapValue(mbr1,mbr2);
            area[k-1] = Calculate_Area(mbr1) + Calculate_Area(mbr2);

            if(area[k-1] < minArea){
                minArea = area[k-1];
                bestDistributionArea.clear();
                bestDistributionArea.add(new ArrayList<>(group1));
                bestDistributionArea.add(new ArrayList<>(group2));
            }

            if (overlap[k-1] < minOverlap){
                minOverlap = overlap[k-1];
                bestDistributionOverlap.clear();
                bestDistributionOverlap.add(new ArrayList<>(group1));
                bestDistributionOverlap.add(new ArrayList<>(group2));
            }
            group1.clear();
            group2.clear();
        }

        if(minOverlap==0){
            return bestDistributionArea;
        }else{
            return bestDistributionOverlap;
        }
    }

    // Function to calculate the area-value of a given mbr.
    private double Calculate_Area(double[][] mbr){
        double area = 1;
        for(int i=0;i<dim;i++){
            area = area * Math.abs(mbr[i][0]-mbr[i][1]);
        }
        return area;
    }

    // Function to calculate the overlap-value of 2 given mbrs.
    private double Calculate_OverlapValue(double[][] mbr1, double[][] mbr2){
        double overlap = 1;
        for(int i=0;i<dim;i++){
            overlap = overlap * Math.max(0,Math.min(mbr1[i][1],mbr2[i][1])-Math.max(mbr1[i][0],mbr2[i][0]));
        }
        return overlap;
    }

    /**
     * Function to calculate the sum of all the margin-values of the different distributions ( LEAF ).
     * @param sortedEntries
     * @return
     */
    private double Calculate_S_Leaf(List<Record> sortedEntries){
        double sum = 0;
        List<Record> group1 = new ArrayList<>();
        List<Record> group2 = new ArrayList<>();
        double[][] mbr1;
        double[][] mbr2;

        for (int k=1;k<=M-2*m+2;k++){
            for (int i=0;i<m-1+k;i++){
                group1.add(sortedEntries.get(i));
            }
            for (int i=m-1+k;i<sortedEntries.size();i++){
                group2.add(sortedEntries.get(i));
            }

            mbr1 = Calculate_Mbr_Leaf(group1);
            mbr2 = Calculate_Mbr_Leaf(group2);

            sum+=Calculate_MarginValue(mbr1);
            sum+=Calculate_MarginValue(mbr2);
            group1.clear();
            group2.clear();
        }
        return sum;
    }

    /**
     * Function to calculate the mbr of each of the two groups during a node-split ( LEAF ).
     * @param group
     * @return
     */
    private double[][] Calculate_Mbr_Leaf(List<Record> group){
        double[][] mbr = new double[dim][2];

        for(int i=0;i<dim;i++){
            for (Record record : group) {
                double entryCoord = record.getInfo().get(i);

                // In every dimension axis check if we need to adjust any of the upper or lower bounds of the mbr.
                if (entryCoord < mbr[i][0] || mbr[i][0] == 0) {         //Check lower bound of mbr
                    mbr[i][0] = entryCoord;
                }
                if (entryCoord > mbr[i][1] || mbr[i][1] == 0) {         //Check upper bound of mbr
                    mbr[i][1] = entryCoord;
                }
            }
        }
        return mbr;
    }

    /**
     * Function to calculate the margin-value of a given mbr.
     * @param mbr
     * @return
     */
    private double Calculate_MarginValue(double[][] mbr){
        double sum=0;
        for(int i=0;i<dim;i++){
            sum+=Math.abs(mbr[i][0]-mbr[i][1]);
        }
        sum = sum * Math.pow(2,(dim-1));    // formula to calculate a bounding rectangle's margin-value in k-dimensions
        //System.out.println(sum);
        return sum;
    }
}