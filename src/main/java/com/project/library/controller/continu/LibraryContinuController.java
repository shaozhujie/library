package com.project.library.controller.continu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.library.domain.LibraryBorrow;
import com.project.library.domain.LibraryContinu;
import com.project.library.domain.Result;
import com.project.library.domain.User;
import com.project.library.enums.ResultCode;
import com.project.library.service.LibraryBorrowService;
import com.project.library.service.LibraryContinuService;
import com.project.library.service.UserService;
import com.project.library.utils.TokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 借阅controller
 * @date 2024/05/29 05:19
 */
@Controller
@ResponseBody
@RequestMapping("continu")
public class LibraryContinuController {

    @Autowired
    private LibraryContinuService libraryContinuService;
    @Autowired
    private LibraryBorrowService libraryBorrowService;
    @Autowired
    private UserService userService;

    /** 分页获取借阅 */
    @PostMapping("getLibraryContinuPage")
    public Result getLibraryContinuPage(@RequestBody LibraryContinu libraryContinu) {
        Page<LibraryContinu> page = new Page<>(libraryContinu.getPageNumber(),libraryContinu.getPageSize());
        QueryWrapper<LibraryContinu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(libraryContinu.getBorrowId()),LibraryContinu::getBorrowId,libraryContinu.getBorrowId())
                .like(StringUtils.isNotBlank(libraryContinu.getName()),LibraryContinu::getName,libraryContinu.getName())
                .eq(libraryContinu.getState() != null,LibraryContinu::getState,libraryContinu.getState())
                .eq(StringUtils.isNotBlank(libraryContinu.getUserId()),LibraryContinu::getUserId,libraryContinu.getUserId())
                .eq(StringUtils.isNotBlank(libraryContinu.getUserName()),LibraryContinu::getUserName,libraryContinu.getUserName());
        Page<LibraryContinu> libraryContinuPage = libraryContinuService.page(page, queryWrapper);
        return Result.success(libraryContinuPage);
    }

    /** 根据id获取借阅 */
    @GetMapping("getLibraryContinuById")
    public Result getLibraryContinuById(@RequestParam("id")String id) {
        LibraryContinu libraryContinu = libraryContinuService.getById(id);
        return Result.success(libraryContinu);
    }

    /** 保存借阅 */
    @PostMapping("saveLibraryContinu")
    public Result saveLibraryContinu(@RequestBody LibraryContinu libraryContinu) {
        LibraryBorrow borrow = libraryBorrowService.getById(libraryContinu.getBorrowId());
        libraryContinu.setName(borrow.getName());
        libraryContinu.setUserId(borrow.getUserId());
        libraryContinu.setUserName(borrow.getUserName());
        boolean save = libraryContinuService.save(libraryContinu);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑借阅 */
    @PostMapping("editLibraryContinu")
    @Transactional(rollbackFor = Exception.class)
    public Result editLibraryContinu(@RequestBody LibraryContinu libraryContinu) {
        if (libraryContinu.getState() == 1) {
            LibraryBorrow borrow = libraryBorrowService.getById(libraryContinu.getBorrowId());
            //更新归还时间
            Calendar instance = Calendar.getInstance();
            instance.setTime(borrow.getReturnTime());
            LibraryContinu continu = libraryContinuService.getById(libraryContinu.getId());
            instance.add(Calendar.DAY_OF_YEAR,continu.getDays());
            borrow.setReturnTime(instance.getTime());
            libraryBorrowService.updateById(borrow);
        }
        boolean save = libraryContinuService.updateById(libraryContinu);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除借阅 */
    @GetMapping("removeLibraryContinu")
    public Result removeLibraryContinu(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                libraryContinuService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("借阅id不能为空！");
        }
    }

}
