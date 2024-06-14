package com.project.library.controller.shelf;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.library.domain.LibraryShelf;
import com.project.library.domain.Result;
import com.project.library.enums.ResultCode;
import com.project.library.service.LibraryShelfService;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 书架controller
 * @date 2024/05/29 09:48
 */
@Controller
@ResponseBody
@RequestMapping("shelf")
public class LibraryShelfController {

    @Autowired
    private LibraryShelfService libraryShelfService;

    /** 分页获取书架 */
    @PostMapping("getLibraryShelfPage")
    public Result getLibraryShelfPage(@RequestBody LibraryShelf libraryShelf) {
        Page<LibraryShelf> page = new Page<>(libraryShelf.getPageNumber(),libraryShelf.getPageSize());
        QueryWrapper<LibraryShelf> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(libraryShelf.getName()),LibraryShelf::getName,libraryShelf.getName());
        Page<LibraryShelf> libraryShelfPage = libraryShelfService.page(page, queryWrapper);
        return Result.success(libraryShelfPage);
    }

    @GetMapping("getLibraryShelfList")
    public Result getLibraryShelfList() {
        List<LibraryShelf> shelfList = libraryShelfService.list();
        return Result.success(shelfList);
    }

    /** 根据id获取书架 */
    @GetMapping("getLibraryShelfById")
    public Result getLibraryShelfById(@RequestParam("id")String id) {
        LibraryShelf libraryShelf = libraryShelfService.getById(id);
        return Result.success(libraryShelf);
    }

    /** 保存书架 */
    @PostMapping("saveLibraryShelf")
    public Result saveLibraryShelf(@RequestBody LibraryShelf libraryShelf) {
        boolean save = libraryShelfService.save(libraryShelf);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑书架 */
    @PostMapping("editLibraryShelf")
    public Result editLibraryShelf(@RequestBody LibraryShelf libraryShelf) {
        boolean save = libraryShelfService.updateById(libraryShelf);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除书架 */
    @GetMapping("removeLibraryShelf")
    public Result removeLibraryShelf(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                libraryShelfService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("书架id不能为空！");
        }
    }

}
