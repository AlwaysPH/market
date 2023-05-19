package com.market.coupon.controller;

import com.market.common.core.controller.BaseController;
import com.market.common.core.page.TableDataInfo;
import com.market.common.utils.poi.ExcelUtil;
import com.market.coupon.model.UserCouponEntity;
import com.market.coupon.model.UserCouponParam;
import com.market.coupon.service.CenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 优惠券中心
 * @author ph
 * @version 1.0
 * @date 2023-04-20 11:11
 */
@RestController
@RequestMapping("/center")
public class CenterController extends BaseController {

    @Autowired
    private CenterService centerService;

    /**
     * 查询用户领取优惠券列表
     */
    @GetMapping("/list")
    public TableDataInfo list(UserCouponParam param) {
        startPage();
        List<UserCouponEntity> list = centerService.list(param);
        return getDataTable(list);
    }

    /**
     * 导出用户领取优惠券列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserCouponParam param) {
        List<UserCouponEntity> list = centerService.list(param);
        ExcelUtil<UserCouponEntity> util = new ExcelUtil<UserCouponEntity>(UserCouponEntity.class);
        util.exportExcel(response, list, "用户领取优惠券列表");
    }
}
