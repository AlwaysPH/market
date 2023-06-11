package com.market.activity.controller;

import java.util.List;

import com.market.activity.model.*;
import com.market.activity.service.ActivityService;
import com.market.common.core.controller.BaseController;
import com.market.common.core.domain.AjaxResult;
import com.market.common.core.domain.R;
import com.market.common.core.page.TableDataInfo;
import com.market.common.exception.ServiceException;
import com.market.common.utils.ValidatorUtil;
import com.market.common.utils.poi.ExcelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 活动信息Controller
 *
 * @author ph
 * @date 2023-04-15
 */
@RestController
@RequestMapping("/activity")
public class ActivityController extends BaseController {
    @Autowired
    private ActivityService activityService;

    /**
     * 查询活动信息列表
     */
    @PreAuthorize("@ss.hasPermi('activity:activity:list')")
    @GetMapping("/list")
    public TableDataInfo list(ActivityInfo info) {
        startPage();
        List<ActivityInfo> list = activityService.selectActivityInfoList(info);
        return getDataTable(list);
    }

    /**
     * 导出活动信息列表
     */
    @PreAuthorize("@ss.hasPermi('activity:activity:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, ActivityInfo info) {
        List<ActivityInfo> list = activityService.selectActivityInfoList(info);
        ExcelUtil<ActivityInfo> util = new ExcelUtil<ActivityInfo>(ActivityInfo.class);
        util.exportExcel(response, list, "活动列表");
    }

    /**
     * 获取活动信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('activity:activity:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(activityService.selectActivityInfoById(id));
    }

    /**
     * 新增活动信息
     */
    @PreAuthorize("@ss.hasPermi('activity:activity:add')")
    @PostMapping
    public AjaxResult add(@RequestBody ActivityInfo info) {
        return toAjax(activityService.insertActivityInfo(info));
    }

    /**
     * 修改活动信息
     */
    @PreAuthorize("@ss.hasPermi('activity:activity:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody ActivityInfo info) {
        return toAjax(activityService.updateActivityInfo(info));
    }

    /**
     * 删除活动信息
     */
    @PreAuthorize("@ss.hasPermi('activity:activity:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(activityService.deleteActivityInfoByIds(ids));
    }

    /**
     * 查询优惠券关联活动列表
     */
    @GetMapping("/getListByCoupon")
    public TableDataInfo getListByCoupon(ActivityInfo info) {
        startPage();
        List<ActivityInfo> list = activityService.getListByCoupon(info);
        return getDataTable(list);
    }

    /**
     * 提交审核
     */
    @PreAuthorize("@ss.hasPermi('activity:activity:submitAudit')")
    @PostMapping("/submitAudit")
    public AjaxResult submitAudit(@RequestBody ActivityInfo info) {
        return toAjax(activityService.submitAudit(info));
    }

    /**
     * 审核
     */
    @PreAuthorize("@ss.hasPermi('activity:activity:audit')")
    @PostMapping("/audit")
    public AjaxResult audit(@RequestBody ActivityInfo info) {
        return toAjax(activityService.audit(info));
    }

    /**
     * 配置优惠券
     */
    @PreAuthorize("@ss.hasPermi('activity:activity:coupon')")
    @PostMapping("/submitCoupon")
    public AjaxResult submitCoupon(@RequestBody ActivityCouponParams params) {
        ValidatorUtil.validateEntity(params);
        return toAjax(activityService.submitCoupon(params));
    }

    /**
     * 指定用户
     */
    @PreAuthorize("@ss.hasPermi('activity:activity:specifyUser')")
    @PostMapping("/specifyUser")
    public AjaxResult specifyUser(@RequestBody ActivityCouponParams params) {
        return toAjax(activityService.specifyUser(params));
    }

    /**
     * 获取活动关联优惠券信息
     * @param activityId
     * @return
     */
    @GetMapping("/getGrantCoupon/{activityId}")
    public AjaxResult getGrantCoupon(@PathVariable String activityId) {
        return AjaxResult.success(activityService.getGrantCoupon(activityId));
    }

    /**
     * 发放优惠券
     */
    @PreAuthorize("@ss.hasPermi('activity:activity:grant')")
    @PostMapping("/grantCoupon")
    public AjaxResult grantCoupon(@RequestBody GrantInfo info) {
        return toAjax(activityService.grantCoupon(info));
    }

    /**
     * 卡分类下载模板
     * @param response
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil.downloadTemp(response, "卡分类模板.xlsx");
    }

    /**
     * 卡分类数据导入
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/importData")
    public R<?> importData(MultipartFile file, String activityId) throws Exception {
        ExcelUtil<CardInfo> util = new ExcelUtil<CardInfo>(CardInfo.class);
        List<CardInfo> list = util.importExcel(file.getInputStream());
        StringBuilder sb = new StringBuilder();
        List<CardInfo> data = activityService.importData(list, sb, activityId);
        if(StringUtils.isNotEmpty(sb)){
            throw new ServiceException(sb.toString(), 88888);
        }
        if(CollectionUtils.isNotEmpty(data)){
            activityService.batchInsertCardData(data);
        }
        return R.ok();
    }

    /**
     * 获取活动已配置的商户信息
     * @param activityId
     * @return
     */
    @GetMapping("/getConfigMerchant/{activityId}")
    public AjaxResult getConfigMerchant(@PathVariable String activityId) {
        return AjaxResult.success(activityService.getConfigMerchant(activityId));
    }

    /**
     * 配置商户
     */
    @PreAuthorize("@ss.hasPermi('activity:activity:merchant')")
    @PostMapping("/configMerchant")
    public AjaxResult configMerchant(@RequestBody ActivityCouponParams params) {
        return toAjax(activityService.configMerchant(params));
    }

    /**
     * 商户下载模板
     * @param response
     */
    @PostMapping("/importMerchantTemplate")
    public void importMerchantTemplate(HttpServletResponse response) {
        ExcelUtil.downloadTemp(response, "商户模板.xlsx");
    }

    /**
     * 商户数据导入
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/merchantImportData")
    public AjaxResult importMerchantData(MultipartFile file, String activityId) throws Exception {
        ExcelUtil<MerchantInfo> util = new ExcelUtil<MerchantInfo>(MerchantInfo.class);
        List<MerchantInfo> list = util.importExcel(file.getInputStream());
//        StringBuilder sb = new StringBuilder();
//        List<MerchantInfo> data = activityService.importMerchantData(list, sb, activityId);
//        if(StringUtils.isNotEmpty(sb)){
//            throw new ServiceException(sb.toString(), 88888);
//        }
//        if(CollectionUtils.isNotEmpty(data)){
//            activityService.batchInsertMerchantData(data);
//        }
        return AjaxResult.success(list);
    }

    /**
     * 停止活动
     */
    @PreAuthorize("@ss.hasPermi('activity:activity:stop')")
    @PostMapping("/stop")
    public AjaxResult stop(@RequestBody ActivityCouponParams params) {
        return toAjax(activityService.stop(params));
    }

    /**
     * 重启活动
     */
    @PreAuthorize("@ss.hasPermi('activity:activity:restart')")
    @PostMapping("/restart")
    public AjaxResult restart(@RequestBody ActivityCouponParams params) {
        return toAjax(activityService.restart(params));
    }

    /**
     * 获取卡属性用户信息
     * @param cardNo
     * @return
     */
    @GetMapping("/getCardUserInfo/{cardNo}")
    public AjaxResult getCardUserInfo(@PathVariable String cardNo) {
        return AjaxResult.success(activityService.getCardUserInfo(cardNo));
    }

    /**
     * 获取商户列表
     * @param industryType 行业类型  0 大型商场  1 大型超市  2 娱乐  3 影院 4 连锁便利店
     * @return
     */
    @GetMapping("/getMerchantList/{industryType}")
    public AjaxResult getMerchantList(@PathVariable String industryType) {
        return AjaxResult.success(activityService.getMerchantList(industryType));
    }

}
