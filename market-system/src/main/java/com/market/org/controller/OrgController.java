package com.market.org.controller;

import com.google.common.collect.Lists;
import com.market.common.core.controller.BaseController;
import com.market.common.core.domain.AjaxResult;
import com.market.common.core.domain.R;
import com.market.common.core.domain.entity.SysUser;
import com.market.common.core.page.TableDataInfo;
import com.market.common.exception.ServiceException;
import com.market.common.utils.ValidatorUtil;
import com.market.common.utils.poi.ExcelError;
import com.market.common.utils.poi.ExcelErrorUtils;
import com.market.common.utils.poi.ExcelUtil;
import com.market.common.utils.uuid.IdUtils;
import com.market.org.model.OrgInfo;
import com.market.org.model.OrgParam;
import com.market.org.service.OrgService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author ph
 * @version 1.0
 * @date 2023-04-15 10:05
 */
@RestController
@RequestMapping("/org")
public class OrgController extends BaseController {

    @Autowired
    private OrgService orgService;

    /**
     * 查询机构列表
     */
    @PreAuthorize("@ss.hasPermi('org:org:list')")
    @GetMapping("/list")
    public TableDataInfo list(OrgInfo orgInfo) {
        startPage();
        List<OrgInfo> list = orgService.selectOrgInfoList(orgInfo);
        return getDataTable(list);
    }

    /**
     * 导出机构列表
     */
    @PreAuthorize("@ss.hasPermi('org:org:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, OrgInfo orgInfo) {
        List<OrgInfo> list = orgService.selectOrgInfoList(orgInfo);
        ExcelUtil<OrgInfo> util = new ExcelUtil<OrgInfo>(OrgInfo.class);
        util.exportExcel(response, list, "机构列表");
    }

    /**
     * 获取机构详细信息
     */
    @PreAuthorize("@ss.hasPermi('org:org:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(orgService.selectOrgInfoById(id));
    }

    /**
     * 新增机构
     */
    @PreAuthorize("@ss.hasPermi('org:org:add')")
    @PostMapping
    public AjaxResult add(@RequestBody OrgParam param) {
        ValidatorUtil.validateEntity(param);
        return toAjax(orgService.insertOrgInfo(param));
    }

    /**
     * 修改机构
     */
    @PreAuthorize("@ss.hasPermi('org:org:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody OrgInfo orgInfo) {
        return toAjax(orgService.updateOrgInfo(orgInfo));
    }

    /**
     * 删除机构
     */
    @PreAuthorize("@ss.hasPermi('org:org:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(orgService.deleteOrgInfoByIds(ids));
    }

    /**
     * 数据导入
     * @param file
     * @return
     * @throws Exception
     */
    @PreAuthorize("@ss.hasPermi('org:org:import')")
    @PostMapping("/importData")
    public R<?> importData(MultipartFile file) throws Exception {
        ExcelUtil<OrgParam> util = new ExcelUtil<OrgParam>(OrgParam.class);
        List<OrgParam> list = util.importExcel(file.getInputStream());
        StringBuilder sb = new StringBuilder();
        List<OrgInfo> data = orgService.importData(list, sb);
        if(StringUtils.isNotEmpty(sb)){
            throw new ServiceException(sb.toString(), 88888);
        }
        if(CollectionUtils.isNotEmpty(data)){
            orgService.batchInsertData(data);
        }
        return R.ok();
    }

    /**
     * 下载模板
     * @param response
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil.downloadTemp(response, "机构模板.xlsx");
    }
}
