package com.market.index.service;

import com.market.index.model.IndexParam;
import com.market.index.model.SummaryInfo;

import java.util.List;

/**
 * @author ph
 * @version 1.0
 * @date 2023-05-14 14:00
 */
public interface IndexService {

    /**
     * 获取统计数据
     * @param param
     * @return
     */
    SummaryInfo getSummaryData(IndexParam param);

    /**
     * 获取统计列表数据
     * @param param
     * @return
     */
    List<SummaryInfo> getSummaryListData(IndexParam param);
}
