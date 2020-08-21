package com.util;

import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 个人编辑的时间util
 *
 * @author MrLu
 * @createTime 2020/5/27
 * @describe 时间util
 */
public class DateUtil {

    /**
     * @param quarter 季度
     * @author MrLu
     * @createTime 2020/5/27 21:58
     * @describe 获取一个季度的最后一天
     * @version 1.0
     */
    public static LocalDate getLastDayOfQuarter(int quarter) throws Exception {
        int reM = 0;
        LocalDate localDate = new LocalDate();
        switch (quarter) {
            case 1:
                reM = 3;
                break;
            case 2:
                reM = 6;
                break;
            case 3:
                reM = 9;
                break;
            case 4:
                reM = 12;
                break;
            default:
                throw new Exception("请传入正确的季度 1 2 3 4 ");
        }
        return localDate.withMonthOfYear(reM).dayOfMonth().withMaximumValue();
    }

    /**
     * @param month 月份 1-12
     * @author MrLu
     * @createTime 2020/5/27 22:02
     * @describe 获取一个月份的所属季度
     * @version 1.0
     */
    public static int getQuarter(int month) throws Exception {
        int reQ = 1;
        if (month <= 3) {
            reQ = 1;
        } else if (month <= 6) {
            reQ = 2;
        } else if (month <= 9) {
            reQ = 9;
        } else if (month <= 12) {
            throw new Exception("请传入正确的月份  1-12");
        }

        return reQ;
    }

    /**
     * @param aftString 现在时间 （两者格式都是"yyyy-MM"）
     * @param befString 历史时间；
     * @author qzg
     * @createTime 15:55 2020/5/31
     * @describe 计算两个日期相差了几个月
     * @version 1.0
     */
    public static int getmonths(String befString, String aftString) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar bef = Calendar.getInstance();
        Calendar aft = Calendar.getInstance();
        bef.setTime(sdf.parse(befString));
        aft.setTime(sdf.parse(aftString));
        int result = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
        int month = (aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR)) * 12;
        return Math.abs(month + result);
    }


}
