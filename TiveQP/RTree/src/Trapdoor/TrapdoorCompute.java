package Trapdoor;

import IndexBuilding.User;

public class TrapdoorCompute {

    public static String[] T1(String type) throws Exception {
        String[] type_Prefix = new User().UserType(type);
        return type_Prefix;                     // return trapdoor
    }

    public static String[] T2(String city, double lat, double lng ) throws Exception {
        String[] Location_Prefix = new User().UserLocation(city,lat,lng);

        return Location_Prefix;                     // return trapdoor
    }

    public static String[] T3(int hour_open,int min_open) throws Exception {
        String[] time_Prefix = new User().UserTime(hour_open,min_open);

        return time_Prefix;                     // return trapdoor
    }

}
