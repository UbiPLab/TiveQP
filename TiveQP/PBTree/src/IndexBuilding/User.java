package IndexBuilding;

public class User {

    public static String[] UserType(String type) throws Exception {
        int index = new TypeEncoding().TypeEncoding(type);
        return IndexElementEncoding.prefix(11,index);
    }




    public static String[] UserLocation(String city, double lat, double lng) throws Exception {
        String city_name = city.toUpperCase().replace(",","-").replace(" ","-");
        String[] encode =  new LocationCoding().City_Encoding_User(city_name,lat,lng);
        return new LocationCoding().addCityNumber(city_name,encode);
    }


    public static String[] UserTime(int hour_open,int min_open) throws Exception {
        return new TimeEncoding().TimeEncoding_user(hour_open,min_open);
    }


}
