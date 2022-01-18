package Parameter;

public interface ServerDB_parameters {
    String fileName ="./2w_random.txt";
    String treeStorePath="./Cache/";

    //  5km  8  3   4
    //  4km  16 4   6
    //  3km  32 5   8
    //  2km  32 6   10
    //  1km  64 7   12
//    public final int time = 64;
//    public final String aim = "1 km";
    public final String aim = "1 km";
    public final int level = 7;
    public final int bitsize = 12;
}
