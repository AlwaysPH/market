package com.market.thread.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 00074964
 * @version 1.0
 * @date 2023-06-04 13:22
 */
@Data
public class GrantCouponInfo implements Serializable {
    private static final long serialVersionUID = -5237084856116421886L;

    /**
     * 优惠券ID
     */
    private String couponId;

    /**
     * 活动ID
     */
    private String activityId;

    /**
     * 领取方式  1 自动发放  2 手动领取
     */
    private String sendType;

    /**
     *优惠券渠道  1 潇湘支付优惠券
     */
    private String channelType;

    /**
     * 发放数量
     */
    private Integer grantNum;

    /**
     * 切分次数
     */
    private Integer splitNum;

    /**
     * 用户列表
     */
    private List<UserInfo> userInfoList;


    public static class UserInfo {

        /**
         * 用户编号
         */
        private String userNo;

        /**
         * 用户手机号
         */
        private String phoneNumber;

        /**
         * APP账号
         */
        private String appAccount;

        /**
         * 卡号
         */
        private String cardNumber;

        /**
         * 用户标识 0全部 1 新用户 2 首单
         */
        private String userType;

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getUserNo() {
            return userNo;
        }

        public void setUserNo(String userNo) {
            this.userNo = userNo;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getAppAccount() {
            return appAccount;
        }

        public void setAppAccount(String appAccount) {
            this.appAccount = appAccount;
        }

        public String getCardNumber() {
            return cardNumber;
        }

        public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
        }
    }


}
