package com.market.app;

import com.market.activity.model.ActivityInfo;
import com.market.common.annotation.RepeatSubmit;
import com.market.common.core.controller.BaseController;
import com.market.common.core.domain.AjaxResult;
import com.market.common.core.page.TableDataInfo;
import com.market.coupon.model.AppParams;
import com.market.coupon.model.CouponThreshold;
import com.market.coupon.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ph
 * @version 1.0
 * @date 2023-05-17 09:59
 */
@RestController
@RequestMapping("/app/coupon")
public class CouponAppController extends BaseController {

    @Autowired
    private CouponService couponService;

    /**
     * 获取我的优惠券列表
     * @return
     */
    @RepeatSubmit
    @PostMapping("/getUserCouponList")
    public TableDataInfo getUserCouponList(@RequestBody AppParams params){
        startPage();
        List<CouponThreshold> list = couponService.getUserCouponList(params);
        return getDataTable(list);
    }

    /**
     * 更新优惠券使用状态
     * @return
     */
    @RepeatSubmit
    @PostMapping("/useCoupon")
    public AjaxResult useCoupon(@RequestBody AppParams params){
        return AjaxResult.success(couponService.useCoupon(params));
    }
}
