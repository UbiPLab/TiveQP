package IndexBuilding;

public class TestLocation {
    public static void main(String[] args) {
        String city = "ATLANTA";
        double lat = 33.7660237;
        double lng = -84.5301237;

        int city_index = City.getCityIndex(city);
        double[] city_lat_lng = new City().getCity_lat_lng(city_index);
        for (int i = 0; i < city_lat_lng.length; i++) {
            System.out.print(city_lat_lng[i] + ",");
        }
        System.out.println();
        double x = (lat-city_lat_lng[0]) / (city_lat_lng[1] - city_lat_lng[0]) *10 ;
        double y = (lng-city_lat_lng[2]) / (city_lat_lng[3] - city_lat_lng[2]) *10 ;

        System.out.println(x+","+y);

        String[] encode = new LocationCoding_211117().City_Encoding(city,lat,lng);
        MyUtil.Show.showString_list(encode);
    }
}
