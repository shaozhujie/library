package com.project.library.controller.returns;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.library.domain.*;
import com.project.library.enums.ResultCode;
import com.project.library.service.*;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 归还controller
 * @date 2024/05/29 09:52
 */
@Controller
@ResponseBody
@RequestMapping("return")
public class LibraryReturnController {

    @Autowired
    private LibraryReturnService libraryReturnService;
    @Autowired
    private LibraryBorrowService libraryBorrowService;
    @Autowired
    private LibraryItemService libraryItemService;
    @Autowired
    private LibraryMakeService libraryMakeService;
    @Autowired
    private UserService userService;

    /** 分页获取归还 */
    @PostMapping("getLibraryReturnPage")
    public Result getLibraryReturnPage(@RequestBody LibraryReturn libraryReturn) {
        Page<LibraryReturn> page = new Page<>(libraryReturn.getPageNumber(),libraryReturn.getPageSize());
        QueryWrapper<LibraryReturn> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(libraryReturn.getUserId()),LibraryReturn::getUserId,libraryReturn.getUserId())
                .eq(StringUtils.isNotBlank(libraryReturn.getBorrowId()),LibraryReturn::getBorrowId,libraryReturn.getBorrowId())
                .like(StringUtils.isNotBlank(libraryReturn.getName()),LibraryReturn::getName,libraryReturn.getName())
                .eq(libraryReturn.getState() != null,LibraryReturn::getState,libraryReturn.getState())
                .like(StringUtils.isNotBlank(libraryReturn.getUserName()),LibraryReturn::getUserName,libraryReturn.getUserName());
        Page<LibraryReturn> libraryReturnPage = libraryReturnService.page(page, queryWrapper);
        return Result.success(libraryReturnPage);
    }

    /** 根据id获取归还 */
    @GetMapping("getLibraryReturnById")
    public Result getLibraryReturnById(@RequestParam("id")String id) {
        LibraryReturn libraryReturn = libraryReturnService.getById(id);
        return Result.success(libraryReturn);
    }

    /** 保存归还 */
    @PostMapping("saveLibraryReturn")
    public Result saveLibraryReturn(@RequestBody LibraryReturn libraryReturn) {
        LibraryBorrow borrow = libraryBorrowService.getById(libraryReturn.getBorrowId());
        libraryReturn.setName(borrow.getName());
        libraryReturn.setUserName(borrow.getUserName());
        libraryReturn.setUserId(borrow.getUserId());
        boolean save = libraryReturnService.save(libraryReturn);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑归还 */
    @PostMapping("editLibraryReturn")
    @Transactional(rollbackFor = Exception.class)
    public Result editLibraryReturn(@RequestBody LibraryReturn libraryReturn) throws MessagingException {
        if (libraryReturn.getState() == 1) {
            //确认归还要先把书还回去
            LibraryReturn aReturn = libraryReturnService.getById(libraryReturn.getId());
            LibraryBorrow borrow = libraryBorrowService.getById(aReturn.getBorrowId());
            borrow.setState(3);
            libraryBorrowService.updateById(borrow);
            //跟新图书剩余数量
            QueryWrapper<LibraryItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(LibraryItem::getIsbn,borrow.getIsbn()).last("limit 1");
            LibraryItem item = libraryItemService.getOne(queryWrapper);
            item.setSurplus(item.getSurplus() + 1);
            libraryItemService.updateById(item);
            //有书之后发送邮箱通知最早预约的那个人
            QueryWrapper<LibraryMake> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(LibraryMake::getItemId,item.getId())
                    .eq(LibraryMake::getState,0).orderByAsc(LibraryMake::getCreateTime).last("limit 1");
            List<LibraryMake> makeList = libraryMakeService.list(wrapper);
            for (LibraryMake libraryMake : makeList) {
                User user = userService.getById(libraryMake.getUserId());
                JavaMailSenderImpl sender = new JavaMailSenderImpl();
                sender.setPort(25);
                sender.setHost("smtp.qq.com");
                sender.setUsername("1760272627@qq.com");
                sender.setPassword("nwavnzopbtpibchc");
                sender.setDefaultEncoding("utf-8");
                MimeMessage msg = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(msg, true);
                helper.setFrom(sender.getUsername());
                helper.setTo(user.getEmail());
                helper.setSubject("图书预约借阅提醒");
                helper.setText("您预约借阅的图书："+ borrow.getName() + "已有余量，请及时借阅",true);
                sender.send(msg);
                libraryMake.setState(1);
                libraryMakeService.updateById(libraryMake);
            }
        }
        boolean save = libraryReturnService.updateById(libraryReturn);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除归还 */
    @GetMapping("removeLibraryReturn")
    public Result removeLibraryReturn(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                libraryReturnService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("归还id不能为空！");
        }
    }

}
