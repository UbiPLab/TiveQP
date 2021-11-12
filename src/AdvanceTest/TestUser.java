package AdvanceTest;

import IndexBuilding.User;
import QueryProcessing.TrapdoorCompute;

public class TestUser {
    public static void main(String[] args) throws Exception {
        //  Restaurants**ATLANTA**33.772758**-84.380375**11**0**21**0
        String type = "Restaurants";
        String city = "ATLANTA";
        double lat = 33.772758;
        double lng = -84.380375;
        int open_hour = 12;
        int open_min = 11;

        String[] Type_prefix = new User().UserType(type);
        String[] Location = new User().UserLocation(city,lat,lng);
        String[] Time_prefix = new User().UserTime(open_hour,open_min);

        System.out.println("Type_prefix :");

        MyUtil.Show.showString_list(Type_prefix);

        System.out.println("Location :");

        MyUtil.Show.showString_list(Location);

        System.out.println("Time_prefix :");

        MyUtil.Show.showString_list(Time_prefix);

        System.out.print("KeyList[]:  ");
        String[] Keylist = { "2938879577741549","8729598049525437","8418086888563864","0128636306393258","2942091695121238","6518873307787549"};
        MyUtil.Show.showString_list(Keylist);

        String[][] T1 = new TrapdoorCompute().T1(type,Keylist,12306);
        System.out.println("T1 :");
        MyUtil.Show.ShowMatrix(T1);

        String[][] T2 = new TrapdoorCompute().T2(city,lat,lng,Keylist,12306);
        System.out.println("T2 :");
        MyUtil.Show.ShowMatrix(T2);

        String[][] T3 = new TrapdoorCompute().T3(open_hour,open_min,Keylist,12306);
        System.out.println("T3 :");
        MyUtil.Show.ShowMatrix(T3);
    }
}
