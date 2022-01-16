package IndexBuilding;

public class LocationCoding_Old {
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

    public String[] addCityNumber(String city_name,String[] encode){
        int CityIndex = City.getCityIndex(city_name);
        for (int i = 0; i < encode.length; i++) {
            encode[i] = aim+"|"+CityIndex +"|"+ encode[i];
        }
        return encode;
    }
    public String[] City_Encoding(String city_name, double lat, double lng) throws Exception {
        String[][] code = new  String[level][];
        int count = 0;
        for (int i = 0; i < level; i++) {
            code[i] = City_Encoding_Part(city_name,lat,lng,i);
//            System.out.println(i);
            count = count + code[i].length;
        }
        String[] result = new String[count];
        int temp = 0;
        for (int i = 0; i < code.length; i++) {
            for (int j = 0; j < code[i].length; j++) {
                result[temp] = i+ "|" + code[i][j];
                temp++;
            }
        }
        return result;

    }

    public String[] City_Encoding_Part(String city_name, double lat, double lng, int current_level) throws Exception {
        int time = (int) Math.pow(2,current_level);
        //  取对应城市编码
        int CityIndex = City.getCityIndex(city_name);
        double[] location_format = City.getCity_lat_lng(CityIndex);

        //经纬度的坐标编码
        double num_lat = Projection(location_format[0], location_format[1], lat,time);
        double num_lng = Projection(location_format[2], location_format[3], lng,time);

        int encode = (int) (num_lng * time + num_lat);
//        System.out.println(num_lng +","+num_lat);
//        System.out.println(encode);

        if (num_lat == 0){
            if (num_lng == 0){
                //  左下角
                String[] n1 = IndexElementEncoding.range(bitsize,encode,encode+1);
                String[] n2 = IndexElementEncoding.range(bitsize,encode+time,encode+time+1);
                String[] result = new String[n1.length+n2.length];
                for (int i = 0; i < n1.length; i++) {
                    result[i] = n1[i];
                }
                for (int i = 0; i < n2.length; i++) {
                    result[i+ n1.length] = n2[i];
                }
                return result;
            }else if (num_lng == time-1){
                //  左上角
                String[] n1 = IndexElementEncoding.range(bitsize,encode,encode+1);
                String[] n2 = IndexElementEncoding.range(bitsize,encode-time,encode-time+1);
                String[] result = new String[n1.length+n2.length];
                for (int i = 0; i < n2.length; i++) {
                    result[i] = n2[i];
                }
                for (int i = 0; i < n1.length; i++) {
                    result[i+ n2.length] = n1[i];
                }
                return result;

            }else {
                //  左边
                String[] n1 = IndexElementEncoding.range(bitsize,encode-time,encode-time+1);
                String[] n2 = IndexElementEncoding.range(bitsize,encode,encode+1);
                String[] n3 = IndexElementEncoding.range(bitsize,encode+time,encode+time+1);
                String[] result = new String[n1.length+n2.length+n3.length];
                for (int i = 0; i < n1.length; i++) {
                    result[i] = n1[i];
                }
                for (int i = 0; i < n2.length; i++) {
                    result[i+ n1.length] = n2[i];
                }
                for (int i = 0; i < n3.length; i++) {
                    result[i + n1.length + n2.length] = n3[i];
                }
                return result;
            }

        }else if (num_lat == time-1){
            if (num_lng == 0){
                //  右下角
                String[] n1 = IndexElementEncoding.range(bitsize,encode-1,encode);

                String[] n2 = IndexElementEncoding.range(bitsize,encode+time-1,encode+time);

                String[] result = new String[n1.length+n2.length];
                for (int i = 0; i < n1.length; i++) {
                    result[i] = n1[i];
                }
                for (int i = 0; i < n2.length; i++) {
                    result[i+ n1.length] = n2[i];
                }
                return result;

            }else if (num_lng == time-1){
                //  右上角
                String[] n1 = IndexElementEncoding.range(bitsize,encode-time-1,encode-time);
                String[] n2 = IndexElementEncoding.range(bitsize,encode-1,encode);

                String[] result = new String[n1.length+n2.length];
                for (int i = 0; i < n1.length; i++) {
                    result[i] = n1[i];
                }
                for (int i = 0; i < n2.length; i++) {
                    result[i+ n1.length] = n2[i];
                }
                return result;

            }else {
                //  右边
                String[] n1 = IndexElementEncoding.range(bitsize,encode-time-1,encode-time);
                String[] n2 = IndexElementEncoding.range(bitsize,encode-1,encode);
                String[] n3 = IndexElementEncoding.range(bitsize,encode+time-1,encode+time);
                String[] result = new String[n1.length+n2.length+n3.length];
                for (int i = 0; i < n1.length; i++) {
                    result[i] = n1[i];
                }
                for (int i = 0; i < n2.length; i++) {
                    result[i+ n1.length] = n2[i];
                }
                for (int i = 0; i < n3.length; i++) {
                    result[i + n1.length + n2.length] = n3[i];
                }
                return result;
            }

        }else {
            if (num_lng == 0){
                //  下边
                String[] n1 = IndexElementEncoding.range(bitsize,encode-1,encode+1);
                String[] n2 = IndexElementEncoding.range(bitsize,encode+time-1,encode+time+1);

                String[] result = new String[n1.length+n2.length];
                for (int i = 0; i < n1.length; i++) {
                    result[i] = n1[i];
                }
                for (int i = 0; i < n2.length; i++) {
                    result[i+ n1.length] = n2[i];
                }

                return result;
            }else if (num_lng == time-1){
                //  上边
                String[] n1 = IndexElementEncoding.range(bitsize,encode-time-1,encode-time+1);
                String[] n2 = IndexElementEncoding.range(bitsize,encode-1,encode+1);

                String[] result = new String[n1.length+n2.length];
                for (int i = 0; i < n1.length; i++) {
                    result[i] = n1[i];
                }
                for (int i = 0; i < n2.length; i++) {
                    result[i+ n1.length] = n2[i];
                }

                return result;
            }else {
                //  正常
                String[] n1 = IndexElementEncoding.range(bitsize,encode-time-1,encode-time+1);
                String[] n2 = IndexElementEncoding.range(bitsize,encode-1,encode+1);
                String[] n3 = IndexElementEncoding.range(bitsize,encode+time-1,encode+time+1);
                String[] result = new String[n1.length+n2.length+n3.length];
                for (int i = 0; i < n1.length; i++) {
                    result[i] = n1[i];
                }
                for (int i = 0; i < n2.length; i++) {
                    result[i+ n1.length] = n2[i];
                }
                for (int i = 0; i < n3.length; i++) {
                    result[i + n1.length + n2.length] = n3[i];
                }
                return result;
            }
        }

    }


    public String[] City_Encoding_User(String city_name, double lat, double lng) throws Exception {
        String[][] code = new  String[level][];
        int count = 0;
        for (int i = 0; i < level; i++) {
            code[i] = City_Encoding_User_Part(city_name,lat,lng,i);
            count = count + code[i].length;
        }
        String[] result = new String[count];
        int temp = 0;
        for (int i = 0; i < code.length; i++) {
            for (int j = 0; j < code[i].length; j++) {
                result[temp] = i+ "|" + code[i][j];
                temp++;
            }
        }
        return result;
    }

    public String[] City_Encoding_User_Part(String city_name, double lat, double lng, int current_level) throws Exception {
        int time = (int) Math.pow(2,current_level);
        //  取对应城市编码
        int CityIndex = City.getCityIndex(city_name);
        double[] location_format = City.getCity_lat_lng(CityIndex);

        //经纬度的坐标编码
        double num_lat = Projection(location_format[0], location_format[1], lat, time);
        double num_lng = Projection(location_format[2], location_format[3], lng, time);

        int encode = (int) (num_lng * time + num_lat);

        return new IndexElementEncoding().prefix(bitsize,encode);



    }


    public double Projection(double min, double max, double value, int time){
        double result = (value - min) / (max - min) * time;
        return Math.floor(result);
    }
}
