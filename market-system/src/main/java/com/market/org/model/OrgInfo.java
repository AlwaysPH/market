package com.market.org.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.market.common.annotation.Excel;
import com.market.common.core.domain.BaseEntity;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 机构信息
 * @author ph
 * @version 1.0
 * @date 2023-04-15 10:11
 */
@Data
@Table(name = "T_ORG")
public class OrgInfo extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 6841314484703383363L;

    /**
     * 机构ID
     */
    private String id;

    /** 机构名 */
    @Excel(name = "机构名")
    private String orgName;

    /** 机构编号 */
    @Excel(name = "机构编号")
    private String orgCode;

    /** 角色类型  0  营销方  **/
    private String roleType;

    /** 机构类型 0 商户  1 政府  2 服务商**/
    @Excel(name = "机构名", readConverterExp = "0=商户,1=政府,2=服务商")
    private String orgType;

    /** 营业执照号 **/
    private String licenseNumber;

    /** 营业执照有效期 **/
    private Date licenseTime;

    /** 营业执照照片 **/
    private String licensePhoto;

    /** 企业名称 **/
    @Excel(name = "企业名称")
    private String companyName;

    /** 公司电话 **/
    private String companyPhone;

    /** 公司法人 **/
    @Excel(name = "公司法人")
    private String companyRePerson;

    /** 法人身份证 **/
    private String reIdcard;

    /**统一社会信用代码 **/
    private String creditCode;

    /** 注册资金**/
    private BigDecimal registerAmount;

    /**经营地址 **/
    private String businessAddress;

    /** 邮编**/
    private String postCode;

    /** 经营范围**/
    private String businessScope;

    /** 公司简介**/
    private String companyIntroduction;

    /** 发票名称**/
    private String invoiceName;

    /** 开户行**/
    private String bankName;

    /**开户账号 **/
    private String bankNumber;

    /** 税号**/
    private String taxCode;

    /**单位地址 **/
    private String companyAddress;

    /**联系人姓名 **/
    @Excel(name = "联系人姓名")
    private String contactName;

    /** 联系人号码**/
    @Excel(name = "手机号")
    private String contactPhone;

    /**联系人邮箱 **/
    private String contactEmail;

    /**联系地址 **/
    private String contactAddress;

    /** 删除标志（0代表存在 1代表删除）**/
    private String delFlag;

    /** 审核状态   0  待审核   1 审核通过   2 审核拒绝**/
    @Excel(name = "审核状态", readConverterExp = "0=待审核,1=审核通过,2=审核拒绝")
    private String auditStatus;

    /**审核时间 **/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审核时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date auditTime;

    /**
     * 注册地址
     */
    private String registerAddress;

    /**
     * 公司规模
     */
    private Integer companyNum;

    /**
     * 商户编号
     */
    private String companyCode;

    /**
     * 发票电话
     */
    private String invoicePhone;

    /**
     * 审核备注
     */
    @Excel(name = "审核备注")
    private String remark;

}
