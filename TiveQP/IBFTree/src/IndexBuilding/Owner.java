package IndexBuilding;

public class Owner {


    public static String[] OwnerType(String type) throws Exception {
        int index = new TypeEncoding().TypeEncoding(type);
        return IndexElementEncoding.prefix(11,index);
    }


    public static String[] OwnerType_Complement(String type) throws Exception {

        return new TypeEncoding().TypeComplment(type);
    }


    public static String[] OwnerLocation(String city, double lat, double lng){
        String city_name = city.toUpperCase().replace(",","-").replace(" ","-");
        String[] encode =  new LocationCoding().City_Encoding(city_name,lat,lng);
        return new LocationCoding().addCityNumber(city_name,encode);
    }


    public static String[] OwnerLoation_Complement(String city, double lat, double lng) throws Exception {
        String city_name = city.toUpperCase().replace(",","-").replace(" ","-");
        String[] encode =  new LocationCoding().City_Encoding_Complement(city_name,lat,lng);
        return new LocationCoding().addCityNumber(city_name,encode);
    }


    public static String[] OwnerTime(int hour_open,int min_open,int hour_close,int min_close){
        return new TimeEncoding().TimeEncoding_owner(hour_open,min_open,hour_close,min_close);
    }

    public static String[] OwnerTime_Complement(int hour_open,int min_open,int hour_close,int min_close){
        return new TimeEncoding().TimeEncoding_owner_Complement(hour_open,min_open,hour_close,min_close);
    }

}
