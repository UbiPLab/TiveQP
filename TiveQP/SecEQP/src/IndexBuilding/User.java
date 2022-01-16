package IndexBuilding;

public class User {

    /**
     * User输入 type 返回 type对应编号的前缀族
     * @param type
     * @return
     * @throws Exception
     */
    public static String[] UserType(String type) throws Exception {
        int index = new TypeEncoding().TypeEncoding(type);
//        String[] prefix = IndexElementEncoding.prefix(11,index);
//        String[] result = new String[1];
//        result[0] = prefix[0];
        return IndexElementEncoding.prefix(11,index);
    }



    /**
     * 格式化输入后 对User 位置编码
     * @param city
     * @param lat
     * @param lng
     * @return
     */
    public static String[] UserLocation(String city, double lat, double lng) throws Exception {
        String city_name = city.toUpperCase().replace(",","-").replace(" ","-");
        String[] encode =  new LocationCoding().City_Encoding_User(city_name,lat,lng);
        return new LocationCoding().addCityNumber(city_name,encode);
    }



    /**
     * 对输入时间 半点处理
     * @param hour_open
     * @param min_open
     * @return  前缀族
     */
    public static String[] UserTime(int hour_open,int min_open) throws Exception {
        return new TimeEncoding().TimeEncoding_user(hour_open,min_open);
    }


}
