package com.project.library.controller.type;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.library.domain.LibraryType;
import com.project.library.domain.Result;
import com.project.library.enums.ResultCode;
import com.project.library.service.LibraryTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 图书类别controller
 * @date 2024/05/29 09:05
 */
@Controller
@ResponseBody
@RequestMapping("type")
public class LibraryTypeController {

    @Autowired
    private LibraryTypeService libraryTypeService;

    /** 分页获取图书类别 */
    @PostMapping("getLibraryTypePage")
    public Result getLibraryTypePage(@RequestBody LibraryType libraryType) {
        Page<LibraryType> page = new Page<>(libraryType.getPageNumber(),libraryType.getPageSize());
        QueryWrapper<LibraryType> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(libraryType.getName()),LibraryType::getName,libraryType.getName());
        Page<LibraryType> libraryTypePage = libraryTypeService.page(page, queryWrapper);
        return Result.success(libraryTypePage);
    }

    @GetMapping("getLibraryTypeList")
    public Result getLibraryTypeList() {
        List<LibraryType> typeList = libraryTypeService.list();
        return Result.success(typeList);
    }

    /** 根据id获取图书类别 */
    @GetMapping("getLibraryTypeById")
    public Result getLibraryTypeById(@RequestParam("id")String id) {
        LibraryType libraryType = libraryTypeService.getById(id);
        return Result.success(libraryType);
    }

    /** 保存图书类别 */
    @PostMapping("saveLibraryType")
    public Result saveLibraryType(@RequestBody LibraryType libraryType) {
        boolean save = libraryTypeService.save(libraryType);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑图书类别 */
    @PostMapping("editLibraryType")
    public Result editLibraryType(@RequestBody LibraryType libraryType) {
        boolean save = libraryTypeService.updateById(libraryType);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除图书类别 */
    @GetMapping("removeLibraryType")
    public Result removeLibraryType(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                libraryTypeService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("图书类别id不能为空！");
        }
    }

}