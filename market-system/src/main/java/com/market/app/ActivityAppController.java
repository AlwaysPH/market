package com.market.app;

import com.market.activity.model.ActivityInfo;
import com.market.activity.service.ActivityService;
import com.market.common.annotation.RepeatSubmit;
import com.market.common.core.controller.BaseController;
import com.market.common.core.domain.AjaxResult;
import com.market.common.core.page.TableDataInfo;
import com.market.common.utils.ValidatorUtil;
import com.market.coupon.model.AppParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ph
 * @version 1.0
 * @date 2023-05-17 10:00
 */
@RestController
@RequestMapping("/app/activity")
public class ActivityAppController extends BaseController {

    @Autowired
    private ActivityService activityService;

    /**
     * 获取APP的活动列表
     * @return
     */
    @RepeatSubmit
    @PostMapping("/getActivityList")
    public TableDataInfo getActivityList(){
        startPage();
        List<ActivityInfo> list = activityService.getAppActivityList();
        return getDataTable(list);
    }

    /**
     * 获取APP的活动关联优惠券列表
     * @return
     */
    @RepeatSubmit
    @PostMapping("/getAppActivityCouponList")
    public AjaxResult getAppActivityCouponList(@RequestBody ActivityInfo info){
        return AjaxResult.success(activityService.getAppActivityCouponList(info));
    }

    /**
     * 获取APP优惠券使用门店
     * @return
     */
    @RepeatSubmit
    @PostMapping("/getAPPCouponMerchant")
    public AjaxResult getAPPCouponMerchant(@RequestBody AppParams params){
        return AjaxResult.success(activityService.getAPPCouponMerchant(params));
    }

    /**
     * 领取优惠券
     * @return
     */
    @RepeatSubmit
    @PostMapping("/receiveCoupon")
    public AjaxResult receiveCoupon(@RequestBody AppParams params){
        ValidatorUtil.validateEntity(params);
        return activityService.receiveCoupon(params);
    }

    /**
     * 获取活动详情
     * @return
     */
    @RepeatSubmit
    @PostMapping("/getAPPActivityDetail")
    public AjaxResult getAPPActivityDetail(@RequestBody AppParams params){
        return activityService.getAPPActivityDetail(params);
    }
}
