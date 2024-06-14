package com.project.library.controller.make;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.library.domain.LibraryItem;
import com.project.library.domain.LibraryMake;
import com.project.library.domain.Result;
import com.project.library.domain.User;
import com.project.library.enums.ResultCode;
import com.project.library.service.LibraryItemService;
import com.project.library.service.LibraryMakeService;
import com.project.library.service.UserService;
import com.project.library.utils.TokenUtils;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 预约controller
 * @date 2024/05/29 09:07
 */
@Controller
@ResponseBody
@RequestMapping("make")
public class LibraryMakeController {

    @Autowired
    private LibraryMakeService libraryMakeService;
    @Autowired
    private LibraryItemService libraryItemService;
    @Autowired
    private UserService userService;

    /** 分页获取预约 */
    @PostMapping("getLibraryMakePage")
    public Result getLibraryMakePage(@RequestBody LibraryMake libraryMake) {
        Page<LibraryMake> page = new Page<>(libraryMake.getPageNumber(),libraryMake.getPageSize());
        QueryWrapper<LibraryMake> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(libraryMake.getItemId()),LibraryMake::getItemId,libraryMake.getItemId())
                .like(StringUtils.isNotBlank(libraryMake.getName()),LibraryMake::getName,libraryMake.getName())
                .eq(StringUtils.isNotBlank(libraryMake.getUserId()),LibraryMake::getUserId,libraryMake.getUserId())
                .like(StringUtils.isNotBlank(libraryMake.getUserName()),LibraryMake::getUserName,libraryMake.getUserName());
        Page<LibraryMake> libraryMakePage = libraryMakeService.page(page, queryWrapper);
        return Result.success(libraryMakePage);
    }

    /** 根据id获取预约 */
    @GetMapping("getLibraryMakeById")
    public Result getLibraryMakeById(@RequestParam("id")String id) {
        LibraryMake libraryMake = libraryMakeService.getById(id);
        return Result.success(libraryMake);
    }

    /** 保存预约 */
    @PostMapping("saveLibraryMake")
    public Result saveLibraryMake(@RequestBody LibraryMake libraryMake) {
        String id = TokenUtils.getUserIdByToken();
        //先看看有没有预约过
        QueryWrapper<LibraryMake> query = new QueryWrapper<>();
        query.lambda().eq(LibraryMake::getUserId,id)
                .eq(LibraryMake::getItemId,libraryMake.getItemId())
                .eq(LibraryMake::getState,0);
        int count = libraryMakeService.count(query);
        if (count > 0) {
            return Result.fail("已存在有效预约记录，请勿重复预约");
        }
        LibraryItem item = libraryItemService.getById(libraryMake.getItemId());
        libraryMake.setName(item.getName());
        libraryMake.setUserId(id);
        User user = userService.getById(id);
        libraryMake.setUserName(user.getUserName());
        boolean save = libraryMakeService.save(libraryMake);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑预约 */
    @PostMapping("editLibraryMake")
    public Result editLibraryMake(@RequestBody LibraryMake libraryMake) {
        boolean save = libraryMakeService.updateById(libraryMake);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除预约 */
    @GetMapping("removeLibraryMake")
    public Result removeLibraryMake(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                libraryMakeService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("预约id不能为空！");
        }
    }

}
