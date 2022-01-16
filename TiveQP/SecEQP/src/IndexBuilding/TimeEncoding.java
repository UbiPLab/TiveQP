package IndexBuilding;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeEncoding {
    /**
     *             "星期一: 休息",
     *             "星期二: 11:30–14:30, 17:00–21:00",
     *             "星期三: 11:30–14:30, 17:00–21:00",
     *             "星期四: 11:30–14:30, 17:00–21:00",
     *             "星期五: 11:30–14:30, 17:00–21:00",
     *             "星期六: 11:30–14:30, 17:00–21:00",
     *             "星期日: 休息"
     *
     *              open 0 close 1  1 bit
     *             星期一 到 星期日 0，1，2，3，4，5，6   3 bit
     *             时间 0000 - 2350  8 bit 半小时为最小单位
     *             日期 转化为 星期
     */


    /**
     * 对 User 查询时间处理，采取 每半小时为单位 0：00 - 24：00 ———————— 0 - 48
     * @param hour  hour * 2
     * @param min   if（0-30） hour + 0 ； if（30-60） hour + 1
     * @return  F（hour）前缀族
     * @throws Exception
     */
    public String[] TimeEncoding_user(int hour,int min) throws Exception {
        int time = hour*2;

        if (min>=30){
            time++;
        }

        String[] timePrefix = TimeTimePrefix(time);

        return timePrefix;
    }

    /**
     * 对 Owner 查询时间处理，采取 每半小时为单位 0：00 - 24：00 ———————— 0 - 48
     * @param hour_open hour_open * 2
     * @param min_open   if（0-30） hour_open + 0 ； if（30-60） hour_open + 1
     * @param hour_close    hour_close * 2
     * @param min_close  if（0-30） hour_close + 0 ； if（30-60） hour_close + 1
     * @return  range(hour_open,hour_close)
     */
    public String[] TimeEncoding_owner(int hour_open,int min_open,int hour_close,int min_close){


        int time_open = hour_open*2;
        if (min_open>=30){
            time_open++;
        }
        int time_close = hour_close*2;
        if (min_close>=30){
            time_close++;
        }
        String[] timeRange = TimeTimeRange(time_open,time_close);

        return timeRange;
    }

    public String[] TimeEncoding_owner_Complement(int hour_open,int min_open,int hour_close,int min_close){
        int time_open = hour_open*2;
        if (min_open>=30){
            time_open++;
        }
        int time_close = hour_close*2;
        if (min_close>=30){
            time_close++;
        }

        String[] n1 = TimeTimeRange(0,time_open-1);
        if (time_open+1 ==time_close){
            String[] n2 = TimeTimeRange(time_close+1,47);

            String[] timePrefix = new String[n1.length+n2.length];

            for (int i = 0; i < n1.length; i++) {
                timePrefix[i] = n1[i];
            }
            for (int i = 0; i < n2.length; i++) {
                timePrefix[i+n1.length] = n2[i];
            }
            return timePrefix;
        }else {
            String[] n2 = TimeTimeRange(time_open + 1, time_close - 1);
            String[] n3 = TimeTimeRange(time_close + 1, 47);

            String[] timePrefix = new String[n1.length + n2.length + n3.length];

            for (int i = 0; i < n1.length; i++) {
                timePrefix[i] = n1[i];
            }
            for (int i = 0; i < n2.length; i++) {
                timePrefix[i + n1.length] = n2[i];
            }
            for (int i = 0; i < n3.length; i++) {
                timePrefix[i + n1.length + n2.length] = n3[i];
            }
            return timePrefix;
        }

    }




    //  2019-05-06 星期一 08:00
//    public String[] TimeEncoding_user(String status,String year,String month,String day,int hour,int min) throws Exception {
//        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
//        String date = year+"-"+month+"-"+day;
////        Date d2 = f.parse(date);
//        String weekday_change = TimeDay(date);
//        String weekday_encode = TimeWeekday(weekday_change);
//        int time = hour*10+min/10;
//        String[] timePrefix = TimeTimePrefix(time);
//        String status_encode = TimeStatus(status);
//
//        for (int i = 0; i < timePrefix.length; i++) {
//            timePrefix[i] = status_encode+weekday_encode+timePrefix[i];
//        }
//
//        return timePrefix;
//    }

//    public String[] TimeEncoding_owner(String status,String year,String month,String day,int hour_open,int min_open,int hour_close,int min_close){
//        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
//        String date = year+"-"+month+"-"+day;
////        Date d2 = f.parse(date);
//        String weekday_change = TimeDay(date);
//        String weekday_encode = TimeWeekday(weekday_change);
//        int time_open = hour_open*10+min_open/10;
//        int time_close = hour_close*10+min_close/10;
//        String[] timeRange = TimeTimeRange(time_open,time_close);
//        String status_encode = TimeStatus(status);
//
//        for (int i = 0; i < timeRange.length; i++) {
//            timeRange[i] = status_encode+weekday_encode+timeRange[i];
//        }
//
//        return timeRange;
//    }

    public String TimeStatus(String status){
        //  open or close
        if ("open".equals(status)){
            return "1";
        }else if ("close".equals(status)){
            return "0";
        }else {
            return "status error";
        }
    }

    public String TimeWeekday(String weekday){
        //  Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday
        switch (weekday){
            case "星期一" : return "1";
            case "星期二": return "2";
            case "星期三": return "3";
            case "星期四": return "4";
            case "星期五": return "5";
            case "星期六": return "6";
            case "星期日": return "7";
            default: return "Weekday error";
        }
    }

    public String[] TimeTimeRange(int down,int up){
        return IndexElementEncoding.range(6,down,up);
    }
    public String[] TimeTimePrefix(int time) throws Exception {
        return IndexElementEncoding.prefix(6,time);
    }




    public String TimeDay(String datetime){
        //  2019-05-06 ——> 星期一
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        Date date;
        try {
            date = f.parse(datetime);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //一周的第几天
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }



}

