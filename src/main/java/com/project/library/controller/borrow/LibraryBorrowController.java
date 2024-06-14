package com.project.library.controller.borrow;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.library.domain.LibraryBorrow;
import com.project.library.domain.LibraryItem;
import com.project.library.domain.Result;
import com.project.library.domain.User;
import com.project.library.enums.ResultCode;
import com.project.library.service.LibraryBorrowService;
import com.project.library.service.LibraryItemService;
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
 * @description: 借阅controller
 * @date 2024/05/29 02:26
 */
@Controller
@ResponseBody
@RequestMapping("borrow")
public class LibraryBorrowController {

    @Autowired
    private LibraryBorrowService libraryBorrowService;
    @Autowired
    private LibraryItemService libraryItemService;
    @Autowired
    private UserService userService;

    /** 分页获取借阅 */
    @PostMapping("getLibraryBorrowPage")
    public Result getLibraryBorrowPage(@RequestBody LibraryBorrow libraryBorrow) {
        Page<LibraryBorrow> page = new Page<>(libraryBorrow.getPageNumber(),libraryBorrow.getPageSize());
        QueryWrapper<LibraryBorrow> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(libraryBorrow.getUserId()),LibraryBorrow::getUserId,libraryBorrow.getUserId())
                .eq(libraryBorrow.getState() != null,LibraryBorrow::getState,libraryBorrow.getState())
                .like(StringUtils.isNotBlank(libraryBorrow.getIsbn()),LibraryBorrow::getIsbn,libraryBorrow.getIsbn())
                .like(StringUtils.isNotBlank(libraryBorrow.getName()),LibraryBorrow::getName,libraryBorrow.getName());
        Page<LibraryBorrow> libraryBorrowPage = libraryBorrowService.page(page, queryWrapper);
        return Result.success(libraryBorrowPage);
    }

    /** 根据id获取借阅 */
    @GetMapping("getLibraryBorrowById")
    public Result getLibraryBorrowById(@RequestParam("id")String id) {
        LibraryBorrow libraryBorrow = libraryBorrowService.getById(id);
        return Result.success(libraryBorrow);
    }

    /** 保存借阅 */
    @PostMapping("saveLibraryBorrow")
    @Transactional(rollbackFor = Exception.class)
    public Result saveLibraryBorrow(@RequestBody LibraryBorrow libraryBorrow) {
        //先查询图书
        QueryWrapper<LibraryItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(LibraryItem::getIsbn,libraryBorrow.getIsbn()).last("limit 1");
        LibraryItem item = libraryItemService.getOne(queryWrapper);
        if (item.getSurplus() <= 0) {
            return Result.fail("图书余量不足，无法借阅，您可预约借阅，可借阅后会通过邮箱告知");
        }
        //扣减余量
        item.setSurplus(item.getSurplus() - 1);
        libraryItemService.updateById(item);
        String idByToken = TokenUtils.getUserIdByToken();
        User service = userService.getById(idByToken);
        if (service.getUserType() != 1) {
            libraryBorrow.setState(2);
        }
        //设置图书数据
        libraryBorrow.setIsbn(item.getIsbn());
        libraryBorrow.setName(item.getName());
        libraryBorrow.setImage(item.getImage());
        libraryBorrow.setShelf(item.getShelf());
        libraryBorrow.setRow(item.getRow());
        //设置用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getLoginAccount,libraryBorrow.getLoginAccount()).last("limit 1");
        User user = userService.getOne(wrapper);
        libraryBorrow.setUserId(user.getId());
        libraryBorrow.setUserName(user.getUserName());
        boolean save = libraryBorrowService.save(libraryBorrow);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑借阅 */
    @PostMapping("editLibraryBorrow")
    public Result editLibraryBorrow(@RequestBody LibraryBorrow libraryBorrow) {
        boolean save = libraryBorrowService.updateById(libraryBorrow);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除借阅 */
    @GetMapping("removeLibraryBorrow")
    public Result removeLibraryBorrow(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                libraryBorrowService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("借阅id不能为空！");
        }
    }

}
