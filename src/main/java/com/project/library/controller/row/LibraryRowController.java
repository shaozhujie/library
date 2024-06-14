package com.project.library.controller.row;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.library.domain.LibraryRow;
import com.project.library.domain.Result;
import com.project.library.enums.ResultCode;
import com.project.library.service.LibraryRowService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 排controller
 * @date 2024/05/29 10:00
 */
@Controller
@ResponseBody
@RequestMapping("row")
public class LibraryRowController {

    @Autowired
    private LibraryRowService libraryRowService;

    /** 分页获取排 */
    @PostMapping("getLibraryRowPage")
    public Result getLibraryRowPage(@RequestBody LibraryRow libraryRow) {
        Page<LibraryRow> page = new Page<>(libraryRow.getPageNumber(),libraryRow.getPageSize());
        QueryWrapper<LibraryRow> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(libraryRow.getName()),LibraryRow::getName,libraryRow.getName());
        Page<LibraryRow> libraryRowPage = libraryRowService.page(page, queryWrapper);
        return Result.success(libraryRowPage);
    }

    @GetMapping("getLibraryRowList")
    public Result getLibraryRowList() {
        List<LibraryRow> rowList = libraryRowService.list();
        return Result.success(rowList);
    }

    /** 根据id获取排 */
    @GetMapping("getLibraryRowById")
    public Result getLibraryRowById(@RequestParam("id")String id) {
        LibraryRow libraryRow = libraryRowService.getById(id);
        return Result.success(libraryRow);
    }

    /** 保存排 */
    @PostMapping("saveLibraryRow")
    public Result saveLibraryRow(@RequestBody LibraryRow libraryRow) {
        boolean save = libraryRowService.save(libraryRow);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑排 */
    @PostMapping("editLibraryRow")
    public Result editLibraryRow(@RequestBody LibraryRow libraryRow) {
        boolean save = libraryRowService.updateById(libraryRow);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除排 */
    @GetMapping("removeLibraryRow")
    public Result removeLibraryRow(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                libraryRowService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("排id不能为空！");
        }
    }

}
