package com.market.coupon.controller;

import com.market.common.core.controller.BaseController;
import com.market.common.core.domain.AjaxResult;
import com.market.common.core.page.TableDataInfo;
import com.market.common.utils.ValidatorUtil;
import com.market.common.utils.poi.ExcelUtil;
import com.market.coupon.model.ActivityCouponEntity;
import com.market.coupon.model.CouponInfo;
import com.market.coupon.model.CouponParam;
import com.market.coupon.service.CouponService;
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

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 优惠券信息
 *
 * @author ph
 * @date 2023-04-15
 */
@RestController
@RequestMapping("/coupon")
public class CouponController extends BaseController {
    @Autowired
    private CouponService couponService;

    /**
     * 查询优惠券信息列表
     */
    @PreAuthorize("@ss.hasPermi('coupon:coupon:list')")
    @GetMapping("/list")
    public TableDataInfo list(CouponInfo info) {
        startPage();
        List<ActivityCouponEntity> list = couponService.selectCouponInfoList(info);
        return getDataTable(list);
    }

    /**
     * 导出优惠券信息列表
     */
    @PreAuthorize("@ss.hasPermi('coupon:coupon:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, CouponInfo info) {
        List<ActivityCouponEntity> list = couponService.selectCouponInfoList(info);
        ExcelUtil<ActivityCouponEntity> util = new ExcelUtil<ActivityCouponEntity>(ActivityCouponEntity.class);
        util.exportExcel(response, list, "优惠券列表");
    }

    /**
     * 获取优惠券信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('coupon:coupon:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(couponService.selectCouponInfoById(id));
    }

    /**
     * 新增优惠券信息
     */
    @PreAuthorize("@ss.hasPermi('coupon:coupon:add')")
    @PostMapping
    public AjaxResult add(@RequestBody CouponParam param) {
        ValidatorUtil.validateEntity(param);
        return toAjax(couponService.insertCouponInfo(param));
    }

    /**
     * 修改优惠券信息
     */
    @PreAuthorize("@ss.hasPermi('coupon:coupon:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody CouponParam param) {
        ValidatorUtil.validateEntity(param);
        return toAjax(couponService.updateCouponInfo(param));
    }

    /**
     * 删除优惠券信息
     */
    @PreAuthorize("@ss.hasPermi('coupon:coupon:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(couponService.deleteCouponInfoByIds(ids));
    }

    /**
     * 获取新增关联的优惠券列表
     * @param info
     * @return
     */
    @GetMapping("/getCouponDetailList")
    public AjaxResult getCouponDetailList(CouponInfo info) {
        return AjaxResult.success(couponService.getCouponDetailList(info));
    }

    /**
     * 获取活动关联的优惠券列表
     * @param info
     * @return
     */
    @GetMapping("/getActivityCouponList")
    public TableDataInfo getActivityCouponList(CouponInfo info) {
        startPage();
        List<ActivityCouponEntity> list = couponService.getActivityCouponList(info);
        return getDataTable(list);
    }
}
