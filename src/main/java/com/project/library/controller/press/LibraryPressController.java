package com.project.library.controller.press;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.library.domain.LibraryPress;
import com.project.library.domain.Result;
import com.project.library.enums.ResultCode;
import com.project.library.service.LibraryPressService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 出版社controller
 * @date 2024/05/29 09:23
 */
@Controller
@ResponseBody
@RequestMapping("press")
public class LibraryPressController {

    @Autowired
    private LibraryPressService libraryPressService;

    /** 分页获取出版社 */
    @PostMapping("getLibraryPressPage")
    public Result getLibraryPressPage(@RequestBody LibraryPress libraryPress) {
        Page<LibraryPress> page = new Page<>(libraryPress.getPageNumber(),libraryPress.getPageSize());
        QueryWrapper<LibraryPress> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(libraryPress.getName()),LibraryPress::getName,libraryPress.getName());
        Page<LibraryPress> libraryPressPage = libraryPressService.page(page, queryWrapper);
        return Result.success(libraryPressPage);
    }

    @GetMapping("getLibraryPressList")
    public Result getLibraryPressList() {
        List<LibraryPress> pressList = libraryPressService.list();
        return Result.success(pressList);
    }

    /** 根据id获取出版社 */
    @GetMapping("getLibraryPressById")
    public Result getLibraryPressById(@RequestParam("id")String id) {
        LibraryPress libraryPress = libraryPressService.getById(id);
        return Result.success(libraryPress);
    }

    /** 保存出版社 */
    @PostMapping("saveLibraryPress")
    public Result saveLibraryPress(@RequestBody LibraryPress libraryPress) {
        boolean save = libraryPressService.save(libraryPress);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑出版社 */
    @PostMapping("editLibraryPress")
    public Result editLibraryPress(@RequestBody LibraryPress libraryPress) {
        boolean save = libraryPressService.updateById(libraryPress);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除出版社 */
    @GetMapping("removeLibraryPress")
    public Result removeLibraryPress(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                libraryPressService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("出版社id不能为空！");
        }
    }

}
