package com.market.common.utils.poi;

import lombok.Data;

/**
 * @author ph
 * @version 1.0
 * @date 2023-04-20 17:09
 */
@Data
public class ExcelError {

    //行
    private int row;
    //列
    private int column;
    //错误信息
    private String errorMessage;

    public ExcelError(int row,  String errorMessage) {
        this.row = row;
        this.errorMessage = errorMessage;
    }
}
