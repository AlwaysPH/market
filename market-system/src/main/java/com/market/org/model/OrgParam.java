package com.market.org.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.market.common.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ph
 * @version 1.0
 * @date 2023-04-20 16:24
 */
@Data
public class OrgParam implements Serializable {

    /** 机构名 */
    @Excel(name = "机构名称")
    @NotNull(message = "机构名不能为空")
    private String orgName;

    /** 机构编号 */
    private String orgCode;

    /** 角色类型  0  营销方  **/
    @NotNull(message = "角色类型不能为空")
    private String roleType;

    /** 机构类型 0 商户  1 政府  2 服务商**/
    @NotNull(message = "机构类型不能为空")
    @Excel(name = "机构类型", readConverterExp = "0=商户,1=政府,2=服务商")
    private String orgType;

    /** 营业执照号 **/
    @Excel(name = "营业执照号")
    @NotNull(message = "营业执照号不能为空")
    private String licenseNumber;

    /** 营业执照有效期 **/
    @Excel(name = "营业执照有效期")
    @NotNull(message = "营业执照有效期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date licenseTime;

    /** 营业执照照片 **/
    @NotNull(message = "营业执照照片不能为空")
    private String licensePhoto;

    /** 公司名称 **/
    @Excel(name = "公司名称")
    @NotNull(message = "企业名称不能为空")
    private String companyName;

    /** 公司电话 **/
    @Excel(name = "公司电话")
    @NotNull(message = "公司电话不能为空")
    private String companyPhone;

    /** 公司法人 **/
    @NotNull(message = "公司法人不能为空")
    @Excel(name = "公司法人")
    private String companyRePerson;

    /** 法人身份证号 **/
    @Excel(name = "法人身份证号")
    @NotNull(message = "法人身份证号不能为空")
    private String reIdcard;

    /**统一社会信用代码 **/
    @Excel(name = "统一社会信用代码")
    @NotNull(message = "统一社会信用代码不能为空")
    private String creditCode;

    /** 注册资金**/
    @Excel(name = "注册资金")
    @NotNull(message = "注册资金不能为空")
    private BigDecimal registerAmount;

    /**经营地址 **/
    @Excel(name = "经营地址")
    private String businessAddress;

    /** 邮编**/
    @Excel(name = "邮编")
    @NotNull(message = "邮编不能为空")
    private String postCode;

    /** 经营范围**/
    @Excel(name = "经营范围")
    @NotNull(message = "经营范围不能为空")
    private String businessScope;

    /** 公司简介**/
    @Excel(name = "公司简介")
    @NotNull(message = "公司简介不能为空")
    private String companyIntroduction;

    /** 发票名称**/
    @Excel(name = "发票名称")
    @NotNull(message = "发票名称不能为空")
    private String invoiceName;

    /** 开户行**/
    @Excel(name = "开户行")
    @NotNull(message = "开户行不能为空")
    private String bankName;

    /**开户账号 **/
    @Excel(name = "开户账号")
    @NotNull(message = "开户账号不能为空")
    private String bankNumber;

    /** 税号**/
    @Excel(name = "税号")
    private String taxCode;

    /**单位地址 **/
    @Excel(name = "单位地址")
    private String companyAddress;

    /**联系人姓名 **/
    @NotNull(message = "联系人姓名不能为空")
    @Excel(name = "联系人姓名")
    private String contactName;

    /** 联系人号码**/
    @NotNull(message = "手机号不能为空")
    @Excel(name = "手机号")
    private String contactPhone;

    /**联系人邮箱 **/
    @Excel(name = "企业名称")
    private String contactEmail;

    /**联系地址 **/
    @Excel(name = "联系地址")
    private String contactAddress;

    /**
     * 注册地址
     */
    @Excel(name = "注册地址")
    @NotNull(message = "注册地址不能为空")
    private String registerAddress;

    /**
     * 人员规模
     */
    @Excel(name = "人员规模")
    @NotNull(message = "人员规模不能为空")
    private Integer companyNum;

    /**
     * 商户编号
     */
    @Excel(name = "商户编号")
    @NotNull(message = "商户编号不能为空")
    private String companyCode;

    /**
     * 发票电话
     */
    @Excel(name = "电话")
    private String invoicePhone;

    /**
     * 审核备注
     */
    private String remark;
}
