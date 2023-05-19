package com.market.common.utils;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * 正则
 * @author ph
 * @version 1.0
 * @date 2023-04-20 17:15
 */
public class RegexCommon {

    /**
     * 日期时间格式
     */
    public static final String TIME_REGEX = "((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))\\\\s+([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])";

    /**
     * 电话号码
     */
    public static final String PHONE_COM_REGEX = "0\\d{2,3}-\\d{7,8}|\\(?0\\d{2,3}[)-]?\\d{7,8}|\\(?0\\d{2,3}[)-]*\\d{7,8}";

    /**
     * 手机号
     */
    public static final String PHONE_REGEX = "^1[3|4|5|6|7|8|9][0-9]\\d{8}$";


    /**
     * 身份证号码
     */
    public static final String ID_CARD_REGEX = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";

    /**
     * 银行卡号
     */
    public static final String BANK_NUM_REGEX = "^[0-9]{9,18}$";

    /**
     * 正数字
     */
    public static final String NUM_REGEX = "^[1-9]\\d{0,10}";

    /**
     * 保留2位小数正整数
     */
    public static final String TWO_REGEX = "^([0-9]|[1-9]\\d+)(\\.\\d{1,2})?$";

    /**
     * 判断数字是否是符合规范的数据
     * @param amount
     * @return
     */
    public static boolean judgeNum(BigDecimal amount, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(amount.toString()).matches();
    }

    /**
     * 判断输入是否是时间格式
     * @param date
     * @return
     */
    public static boolean judgeTime(String date) {
        Pattern pattern = Pattern.compile(TIME_REGEX);
        return pattern.matcher(date).matches();
    }

    /**
     * 判断输入是否是身份证
     * @param card
     * @return
     */
    public static boolean judgeIdCard(String card) {
        Pattern pattern = Pattern.compile(ID_CARD_REGEX);
        return pattern.matcher(card).matches();
    }

    /**
     * 判断输入是否是符合要求
     * @param data
     * @return
     */
    public static boolean judgeData(String data, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(data).matches();
    }

    public static void main(String[] args) {
        System.out.println(judgeData("18888888888", PHONE_REGEX));
    }
}
