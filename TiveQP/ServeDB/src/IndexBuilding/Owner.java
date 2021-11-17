package IndexBuilding;

public class Owner {

    /**
     * Owner输入 type 返回 type对应编号的前缀族
     * @param type
     * @return
     * @throws Exception
     */
    public static String[] OwnerType(String type) throws Exception {
        int index = new TypeEncoding().TypeEncoding(type);
        return IndexElementEncoding.prefix(11,index);
    }

    /**
     * Owner输入 type 返回 type编号对应补集
     * @param type
     * @return
     * @throws Exception
     */
    public static String[] OwnerType_Complement(String type) throws Exception {

        return new TypeEncoding().TypeComplment(type);
    }

    /**
     * 格式化输入后 对Owner 位置编码
     * @param city
     * @param lat
     * @param lng
     * @return
     */
    public static String[] OwnerLocation(String city, double lat, double lng) throws Exception {
        String city_name = city.toUpperCase().replace(",","-").replace(" ","-");
        String[] encode =  new LocationCoding().City_Encoding(city_name,lat,lng);
        return new LocationCoding().addCityNumber(city_name,encode);
    }



    /**
     * 对输入时间 半点处理
     * @param hour_open
     * @param min_open
     * @param hour_close
     * @param min_close
     * @return
     */
    public static String[] OwnerTime(int hour_open,int min_open,int hour_close,int min_close) throws Exception {
        return new TimeEncoding().TimeEncoding_owner(hour_open,min_open,hour_close,min_close);
    }

    /**
     * Owner时间 补集
     * @param hour_open
     * @param min_open
     * @param hour_close
     * @param min_close
     * @return
     */
    public static String[] OwnerTime_Complement(int hour_open,int min_open,int hour_close,int min_close) throws Exception {
        return new TimeEncoding().TimeEncoding_owner_Complement(hour_open,min_open,hour_close,min_close);
    }

}
