package com.project.library.controller.comment;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.library.domain.LibraryComment;
import com.project.library.domain.Result;
import com.project.library.enums.ResultCode;
import com.project.library.service.LibraryCommentService;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 图书评论controller
 * @date 2024/05/29 03:54
 */
@Controller
@ResponseBody
@RequestMapping("comment")
public class LibraryCommentController {

    @Autowired
    private LibraryCommentService libraryCommentService;

    /** 分页获取图书评论 */
    @PostMapping("getLibraryCommentPage")
    public Result getLibraryCommentPage(@RequestBody LibraryComment libraryComment) {
        Page<LibraryComment> page = new Page<>(libraryComment.getPageNumber(),libraryComment.getPageSize());
        QueryWrapper<LibraryComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(libraryComment.getItemId()),LibraryComment::getItemId,libraryComment.getItemId())
                .like(StringUtils.isNotBlank(libraryComment.getContent()),LibraryComment::getContent,libraryComment.getContent())
                .orderByDesc(LibraryComment::getCreateTime);
        Page<LibraryComment> libraryCommentPage = libraryCommentService.page(page, queryWrapper);
        return Result.success(libraryCommentPage);
    }

    /** 根据id获取图书评论 */
    @GetMapping("getLibraryCommentById")
    public Result getLibraryCommentById(@RequestParam("id")String id) {
        LibraryComment libraryComment = libraryCommentService.getById(id);
        return Result.success(libraryComment);
    }

    /** 保存图书评论 */
    @PostMapping("saveLibraryComment")
    public Result saveLibraryComment(@RequestBody LibraryComment libraryComment) {
        boolean save = libraryCommentService.save(libraryComment);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑图书评论 */
    @PostMapping("editLibraryComment")
    public Result editLibraryComment(@RequestBody LibraryComment libraryComment) {
        boolean save = libraryCommentService.updateById(libraryComment);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除图书评论 */
    @GetMapping("removeLibraryComment")
    public Result removeLibraryComment(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                libraryCommentService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("图书评论id不能为空！");
        }
    }

}
