package com.project.library.controller.recommend;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.library.domain.LibraryRecommend;
import com.project.library.domain.Result;
import com.project.library.enums.ResultCode;
import com.project.library.service.LibraryRecommendService;
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
 * @description: 推荐controller
 * @date 2024/05/29 10:51
 */
@Controller
@ResponseBody
@RequestMapping("recommend")
public class LibraryRecommendController {

    @Autowired
    private LibraryRecommendService libraryRecommendService;

    /** 分页获取推荐 */
    @PostMapping("getLibraryRecommendPage")
    public Result getLibraryRecommendPage(@RequestBody LibraryRecommend libraryRecommend) {
        Page<LibraryRecommend> page = new Page<>(libraryRecommend.getPageNumber(),libraryRecommend.getPageSize());
        QueryWrapper<LibraryRecommend> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(libraryRecommend.getName()),LibraryRecommend::getName,libraryRecommend.getName())
                .like(StringUtils.isNotBlank(libraryRecommend.getAuthor()),LibraryRecommend::getAuthor,libraryRecommend.getAuthor())
                .eq(StringUtils.isNotBlank(libraryRecommend.getUserId()),LibraryRecommend::getUserId,libraryRecommend.getUserId());
        Page<LibraryRecommend> libraryRecommendPage = libraryRecommendService.page(page, queryWrapper);
        return Result.success(libraryRecommendPage);
    }

    /** 根据id获取推荐 */
    @GetMapping("getLibraryRecommendById")
    public Result getLibraryRecommendById(@RequestParam("id")String id) {
        LibraryRecommend libraryRecommend = libraryRecommendService.getById(id);
        return Result.success(libraryRecommend);
    }

    /** 保存推荐 */
    @PostMapping("saveLibraryRecommend")
    public Result saveLibraryRecommend(@RequestBody LibraryRecommend libraryRecommend) {
        String id = TokenUtils.getUserIdByToken();
        libraryRecommend.setUserId(id);
        boolean save = libraryRecommendService.save(libraryRecommend);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑推荐 */
    @PostMapping("editLibraryRecommend")
    public Result editLibraryRecommend(@RequestBody LibraryRecommend libraryRecommend) {
        boolean save = libraryRecommendService.updateById(libraryRecommend);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除推荐 */
    @GetMapping("removeLibraryRecommend")
    public Result removeLibraryRecommend(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                libraryRecommendService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("推荐id不能为空！");
        }
    }

}
