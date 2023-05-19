package com.market.common.utils.poi;

import com.market.common.exception.ServiceException;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ph
 * @version 1.0
 * @date 2023-04-20 20:50
 */
public class ExcelErrorUtils {

    private static final int EXCEL_WIRTER_CODE = 88888;

    public static List<ExcelError> create(List<ExcelError> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public static List<ExcelError> add(List<ExcelError>... list) {
        if (list == null || list.length == 0) {
            return null;
        }
        List<ExcelError> newList = null;
        for (List<ExcelError> ts : list) {
            if (ts != null) {
                if (newList == null) {
                    newList = new ArrayList<ExcelError>();
                }
                newList.addAll(ts);
            }
        }
        return newList;
    }


    public static String message(List<ExcelError> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            return list.stream().map(item-> "第"+item.getRow()+"行"+item.getErrorMessage()).collect(Collectors.joining("\r\n"));
        }
        return "";
    }


    @SafeVarargs
    public static void validate(List<ExcelError> ...list) {
        List<ExcelError> add = add(list);
        if (CollectionUtils.isNotEmpty(add)) {
            add.sort(Comparator.comparingInt(ExcelError::getRow));
            throw new ServiceException(message(add), EXCEL_WIRTER_CODE);
        }
    }
}
