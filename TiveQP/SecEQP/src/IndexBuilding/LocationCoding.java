package IndexBuilding;
import Parameter.Parameter

public class LocationCoding {
    //  5km  10 7
    //  4km  13 8
    //  3km  17 9
    //  2km  25 11
    //  1km  50 12
    public final int time = Parameter.time;
    public final int bitsize = Parameter.bnitsize;
    public int d = 3;   //  维度


    public double p1_sin_30 = 1 / 2;
    public double p1_cos_30 = Math.sqrt(3) / 2;
    public double p2_sin_45 = 1 / Math.sqrt(2);
    public double p2_cos_45 = 1 / Math.sqrt(2);
    public double p3_sin_60 = Math.sqrt(3) / 2;
    public double p3_cos_60 = 1 / 2;


    //  转换坐标系
    public double ChangeCoordinate_lat(double old_lng , double old_lat,double sin,double cos){
        if (sin < cos){
            if (old_lng < old_lat){
                return old_lng *(1 - cos + sin);
            }else {
                return old_lng *(1 + cos - sin);
            }
        }else {
            if (old_lng < old_lat){
                return old_lng - cos;
            }else {
                return old_lng + cos;
            }
        }

//        double lat = old_lat*cos-old_lng*sin;

    }
    public double ChangeCoordinate_lng(double old_lng , double old_lat,double sin,double cos){
//        return old_lng*cos+old_lat*sin;
        if (sin < cos){
            if (old_lng < old_lat){
                return old_lng *(1 - cos + sin);
            }else {
                return old_lng *(1 + cos - sin);
            }
        }else {
            if (old_lng < old_lat){
                return old_lng - cos;
            }else {
                return old_lng + cos;
            }
        }
    }


    public String[] addCityNumber(String city_name,String[] encode){
        int CityIndex = City.getCityIndex(city_name);
        for (int i = 0; i < encode.length; i++) {
            encode[i] = CityIndex +"|"+ encode[i];
        }
        return encode;
    }

    public String[] City_Encoding(String city_name, double lat, double lng){
        String[][] con = new String[d][];
        int sum = 0;
        for (int i = 0; i < con.length; i++) {
            con[i] = City_Encoding_Part(city_name,lat,lng,i);
            sum = sum + con[i].length;
        }
        String[] result = new String[sum];
        int count = 0;
        for (int i = 0; i < con.length; i++) {
            for (int j = 0; j < con[i].length; j++) {
                result[count] = toBinary(i,2) + "||" + con[i][j];
                count++;
            }
        }
        return result;
    }

    public String[] City_Encoding_Part(String city_name, double lat, double lng, int tag){
        //  取对应城市编码
        int CityIndex = City.getCityIndex(city_name);
        double[] location_format = City.getCity_lat_lng(CityIndex);

        double  min_lat = 0;
        double  max_lat = 0;
        double  min_lng = 0;
        double  max_lng = 0;
        double Min_Lat = location_format[0];
        double Max_Lat = location_format[1];
        double Min_Lng = location_format[2];
        double Max_Lng = location_format[3];

        switch (tag){
            case 0: {
                min_lat = Min_Lat;
                max_lat = Max_Lat;
                min_lng = Min_Lng;
                max_lng = Max_Lng;
                break;
            }
            case 1:{
                min_lat = ChangeCoordinate_lat(Min_Lat,Max_Lat,p1_sin_30,p1_cos_30);
                max_lat = ChangeCoordinate_lat(Max_Lat,Min_Lat,p1_sin_30,p1_cos_30);
                max_lng = ChangeCoordinate_lng(Max_Lng,Min_Lng,p1_sin_30,p1_cos_30);
                min_lng = ChangeCoordinate_lng(Min_Lng,Max_Lng,p1_sin_30,p1_cos_30);
                break;
            }
            case 2:{
//                min_lat = ChangeCoordinate_lat(Min_Lng,Min_Lat,p2_sin_45,p2_cos_45);
//                max_lat = ChangeCoordinate_lat(Max_Lng,Max_Lat,p2_sin_45,p2_cos_45);
//                max_lng = ChangeCoordinate_lng(Max_Lng,Max_Lat,p2_sin_45,p2_cos_45);
//                min_lng = ChangeCoordinate_lng(Min_Lng,Min_Lat,p2_sin_45,p2_cos_45);
                min_lat = ChangeCoordinate_lat(Min_Lat,Max_Lat,p2_sin_45,p2_cos_45);
                max_lat = ChangeCoordinate_lat(Max_Lat,Min_Lat,p2_sin_45,p2_cos_45);
                max_lng = ChangeCoordinate_lng(Max_Lng,Min_Lng,p2_sin_45,p2_cos_45);
                min_lng = ChangeCoordinate_lng(Min_Lng,Max_Lng,p2_sin_45,p2_cos_45);
                break;
            }
            case 3:{
                min_lat = ChangeCoordinate_lat(Min_Lat,Max_Lat,p3_sin_60,p3_cos_60);
                max_lat = ChangeCoordinate_lat(Max_Lat,Min_Lat,p3_sin_60,p3_cos_60);
                max_lng = ChangeCoordinate_lng(Max_Lng,Min_Lng,p3_sin_60,p3_cos_60);
                min_lng = ChangeCoordinate_lng(Min_Lng,Max_Lng,p3_sin_60,p3_cos_60);
                break;
            }
        }
//        System.out.println(min_lat+" , "+max_lat);
//        System.out.println(min_lng+" , "+max_lng);


        //经纬度的坐标编码
        double num_lat = Projection(min_lat, max_lat, lat);
        double num_lng = Projection(min_lng, max_lng, lng);

        int encode = (int) (num_lng * time + num_lat);

//        System.out.println("tag = "+tag+" , num_lng = "+num_lng+"  , num_lat = "+num_lat+" , encode = "+encode);

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
                //System.out.println("down:"+(encode-time-1));
                //System.out.println("up:"+(encode-time+1));
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
        String[][] con = new String[d][];
        int sum = 0;
        for (int i = 0; i < con.length; i++) {
            con[i] = City_Encoding_User_Part(city_name,lat,lng,i);
            sum = sum + con[i].length;
        }
        String[] result = new String[sum];
        int count = 0;
        for (int i = 0; i < con.length; i++) {
            for (int j = 0; j < con[i].length; j++) {
                result[count] = toBinary(i,2) + "||" +con[i][j];
                count++;
            }
        }
        return result;
    }

    public String[] City_Encoding_User_Part(String city_name, double lat, double lng, int tag) throws Exception {
        //  取对应城市编码
        int CityIndex = City.getCityIndex(city_name);
        double[] location_format = City.getCity_lat_lng(CityIndex);

        double  min_lat = 0;
        double  max_lat = 0;
        double  min_lng = 0;
        double  max_lng = 0;
        double Min_Lat = location_format[0];
        double Max_Lat = location_format[1];
        double Min_Lng = location_format[2];
        double Max_Lng = location_format[3];

        switch (tag){
            case 0: {
                min_lat = Min_Lat;
                max_lat = Max_Lat;
                min_lng = Min_Lng;
                max_lng = Max_Lng;
                break;
            }
            case 1:{
                min_lat = ChangeCoordinate_lat(Min_Lat,Max_Lat,p1_sin_30,p1_cos_30);
                max_lat = ChangeCoordinate_lat(Max_Lat,Min_Lat,p1_sin_30,p1_cos_30);
                max_lng = ChangeCoordinate_lng(Max_Lng,Min_Lng,p1_sin_30,p1_cos_30);
                min_lng = ChangeCoordinate_lng(Min_Lng,Max_Lng,p1_sin_30,p1_cos_30);
                break;
            }
            case 2:{
//                min_lat = ChangeCoordinate_lat(Min_Lng,Min_Lat,p2_sin_45,p2_cos_45);
//                max_lat = ChangeCoordinate_lat(Max_Lng,Max_Lat,p2_sin_45,p2_cos_45);
//                max_lng = ChangeCoordinate_lng(Max_Lng,Max_Lat,p2_sin_45,p2_cos_45);
//                min_lng = ChangeCoordinate_lng(Min_Lng,Min_Lat,p2_sin_45,p2_cos_45);
                min_lat = ChangeCoordinate_lat(Min_Lat,Max_Lat,p2_sin_45,p2_cos_45);
                max_lat = ChangeCoordinate_lat(Max_Lat,Min_Lat,p2_sin_45,p2_cos_45);
                max_lng = ChangeCoordinate_lng(Max_Lng,Min_Lng,p2_sin_45,p2_cos_45);
                min_lng = ChangeCoordinate_lng(Min_Lng,Max_Lng,p2_sin_45,p2_cos_45);
                break;
            }
            case 3:{
                min_lat = ChangeCoordinate_lat(Min_Lat,Max_Lat,p3_sin_60,p3_cos_60);
                max_lat = ChangeCoordinate_lat(Max_Lat,Min_Lat,p3_sin_60,p3_cos_60);
                max_lng = ChangeCoordinate_lng(Max_Lng,Min_Lng,p3_sin_60,p3_cos_60);
                min_lng = ChangeCoordinate_lng(Min_Lng,Max_Lng,p3_sin_60,p3_cos_60);
                break;
            }
        }

        //经纬度的坐标编码
        double num_lat = Projection(min_lat, max_lat, lat);
        double num_lng = Projection(min_lng, max_lng, lng);

        int encode = (int) (num_lng * time + num_lat);

        return new IndexElementEncoding().prefix(bitsize,encode);



    }

//    public double Projection(double min, double max, double value){
//        double result = (value - Math.abs(min)) / ((Math.abs(max) - Math.abs(min)) * time);
//        return Math.floor(result);
//    }

    public double Projection(double min, double max, double value){
        double result = (value - min) / (max - min) * time;
        return Math.floor(result);
    }

    /**
     * 将一个int数字转换为二进制的字符串形式。
     * @param num 需要转换的int类型数据
     * @param digits 要转换的二进制位数，位数不足则在前面补0
     * @return 二进制的字符串形式
     */
    public static String toBinary(int num, int digits) {
        String cover = Integer.toBinaryString(1 << digits).substring(1);
        String s = Integer.toBinaryString(num);
        return s.length() < digits ? cover.substring(s.length()) + s : s;
    }
}

