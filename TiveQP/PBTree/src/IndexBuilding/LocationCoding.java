package IndexBuilding;


import Parameter.PBTree_paremeters;

public class LocationCoding {
    //  5km  10 7
    //  4km  13 8
    //  3km  17 9
    //  2km  25 11
    //  1km  50 12
    public final int time = PBTree_paremeters.time;
    public final int bitsize = PBTree_paremeters.bitsize;

    public String[] addCityNumber(String city_name,String[] encode){
        int CityIndex = City.getCityIndex(city_name);
        for (int i = 0; i < encode.length; i++) {
            encode[i] = CityIndex +"|"+ encode[i];
        }
        return encode;
    }

    public String[] City_Encoding(String city_name, double lat, double lng){

        int CityIndex = City.getCityIndex(city_name);
        double[] location_format = City.getCity_lat_lng(CityIndex);


        double num_lat = Projection(location_format[0], location_format[1], lat);
        double num_lng = Projection(location_format[2], location_format[3], lng);

        int encode = (int) (num_lng * time + num_lat);

        if (num_lat == 0){
            if (num_lng == 0){

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

        int CityIndex = City.getCityIndex(city_name);
        double[] location_format = City.getCity_lat_lng(CityIndex);


        double num_lat = Projection(location_format[0], location_format[1], lat);
        double num_lng = Projection(location_format[2], location_format[3], lng);

        int encode = (int) (num_lng * time + num_lat);

        if (num_lat == 0){
            if (num_lng == 0){

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

        int CityIndex = City.getCityIndex(city_name);
        double[] location_format = City.getCity_lat_lng(CityIndex);

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

