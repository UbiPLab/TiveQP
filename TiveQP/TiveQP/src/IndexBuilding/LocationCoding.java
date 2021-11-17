package IndexBuilding;


public class LocationCoding{
    //  5km  10 7
    //  4km  13 8
    //  3km  17 9
    //  2km  25 11
    //  1km  50 12
    public final int time = 50;
    public final int bitsize = 12;

    public String[] addCityNumber(String city_name,String[] encode){
        int CityIndex = City.getCityIndex(city_name);
        for (int i = 0; i < encode.length; i++) {
            encode[i] = CityIndex +"|"+ encode[i];
        }
        return encode;
    }

    public String[] City_Encoding(String city_name, double lat, double lng){
        //  取对应城市编码
        int CityIndex = City.getCityIndex(city_name);
        double[] location_format = City.getCity_lat_lng(CityIndex);

        //经纬度的坐标编码
        double num_lat = Projection(location_format[0], location_format[1], lat);
        double num_lng = Projection(location_format[2], location_format[3], lng);

        int encode = (int) (num_lng * time + num_lat);

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


    public String[] City_Encoding_Complement(String city_name, double lat, double lng){
        //  取对应城市编码
        int CityIndex = City.getCityIndex(city_name);
        double[] location_format = City.getCity_lat_lng(CityIndex);

        //经纬度的坐标编码
        double num_lat = Projection(location_format[0], location_format[1], lat);
        double num_lng = Projection(location_format[2], location_format[3], lng);

        int encode = (int) (num_lng * time + num_lat);

        if (num_lat == 0){
            if (num_lng == 0){
                //  左下角
                String[] n1 = IndexElementEncoding.range(bitsize,encode+2,encode+time-1);
                String[] n2 = IndexElementEncoding.range(bitsize,encode+time+2,time*time);
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
                String[] n1 = IndexElementEncoding.range(bitsize,0,encode-time-1);
                String[] n2 = IndexElementEncoding.range(bitsize,encode-time+2,encode-1);
                String[] n3 = IndexElementEncoding.range(bitsize,encode+2,time*time);
                String[] result = new String[n1.length+n2.length+n3.length];
                for (int i = 0; i < n2.length; i++) {
                    result[i] = n2[i];
                }
                for (int i = 0; i < n1.length; i++) {
                    result[i+ n2.length] = n1[i];
                }
                for (int i = 0; i < n3.length; i++) {
                    result[i + n1.length + n2.length] = n3[i];
                }
                return result;

            }else {
                //  左边
                String[] n1 = IndexElementEncoding.range(bitsize,0,encode-1);
                String[] n2 = IndexElementEncoding.range(bitsize,encode+2,encode+time-1);
                String[] n3 = IndexElementEncoding.range(bitsize,encode+time+2,time*time);
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
                String[] n1 = IndexElementEncoding.range(bitsize,0,encode-2);
                String[] n2 = IndexElementEncoding.range(bitsize,encode+1,encode+time-2);
                String[] n3 = IndexElementEncoding.range(bitsize,encode+time+1,time*time);

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

            }else if (num_lng == time-1){
                //  右上角
                String[] n1 = IndexElementEncoding.range(bitsize,0,encode-time-2);
                String[] n2 = IndexElementEncoding.range(bitsize,encode-time+1,encode-2);

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
                String[] n1 = IndexElementEncoding.range(bitsize,0,encode-time-2);
                String[] n2 = IndexElementEncoding.range(bitsize,encode-time+1,encode-2);
                String[] n3 = IndexElementEncoding.range(bitsize,encode+1,encode+time-2);
                String[] n4 = IndexElementEncoding.range(bitsize,encode+time+1,time*time);
                String[] result = new String[n1.length+n2.length+n3.length+n4.length];
                for (int i = 0; i < n1.length; i++) {
                    result[i] = n1[i];
                }
                for (int i = 0; i < n2.length; i++) {
                    result[i+ n1.length] = n2[i];
                }
                for (int i = 0; i < n3.length; i++) {
                    result[i + n1.length + n2.length] = n3[i];
                }
                for (int i = 0; i < n4.length; i++) {
                    result[i + n1.length + n2.length+n3.length] = n4[i];
                }
                return result;
            }

        }else {
            if (num_lng == 0){
                //  下边
                String[] n1 = IndexElementEncoding.range(bitsize,0,encode-2);
                String[] n2 = IndexElementEncoding.range(bitsize,encode+2,time*time);

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
                String[] n1 = IndexElementEncoding.range(bitsize,0,encode-2);
                String[] n2 = IndexElementEncoding.range(bitsize,encode+2,time*time);

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
                String[] n1 = IndexElementEncoding.range(bitsize,0,encode-2);
                String[] n2 = IndexElementEncoding.range(bitsize,encode+2,encode+time-2);
                String[] n3 = IndexElementEncoding.range(bitsize,encode+time+2,time*time);
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
        //  取对应城市编码
        int CityIndex = City.getCityIndex(city_name);
        double[] location_format = City.getCity_lat_lng(CityIndex);

        //经纬度的坐标编码
        double num_lat = Projection(location_format[0], location_format[1], lat);
        double num_lng = Projection(location_format[2], location_format[3], lng);

        int encode = (int) (num_lng * time + num_lat);

        return new IndexElementEncoding().prefix(bitsize,encode);



    }


    public double Projection(double min, double max, double value){
        double result = (value - min) / (max - min) * time;
        return Math.floor(result);
    }
}

