package com.market.index.controller;

import com.market.common.core.controller.BaseController;
import com.market.common.core.domain.AjaxResult;
import com.market.common.core.page.TableDataInfo;
import com.market.common.utils.poi.ExcelUtil;
import com.market.coupon.model.ActivityCouponEntity;
import com.market.coupon.model.CouponInfo;
import com.market.index.model.IndexParam;
import com.market.index.model.SummaryInfo;
import com.market.index.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author ph
 * @version 1.0
 * @date 2023-05-14 13:59
 */
@RestController
@RequestMapping("/index")
public class IndexController extends BaseController {

    @Autowired
    private IndexService indexService;

    /**
     * 获取统计数据
     * @param param
     * @return
     */
    @GetMapping("/getSummaryData")
    public AjaxResult getSummaryData(IndexParam param){
        return AjaxResult.success(indexService.getSummaryData(param));
    }

    /**
     * 获取统计列表数据
     * @param param
     * @return
     */
    @GetMapping("/getSummaryListData")
    public TableDataInfo getSummaryListData(IndexParam param){
        startPage();
        List<SummaryInfo> list = indexService.getSummaryListData(param);
        return getDataTable(list);
    }

    /**
     * 导出优惠券使用详情列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, IndexParam param) {
        List<SummaryInfo> list = indexService.getSummaryListData(param);
        ExcelUtil<SummaryInfo> util = new ExcelUtil<SummaryInfo>(SummaryInfo.class);
        util.exportExcel(response, list, "优惠券使用详情列表");
    }
}
