package com.project.library.controller.item;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.library.domain.LibraryItem;
import com.project.library.domain.Result;
import com.project.library.enums.ResultCode;
import com.project.library.service.LibraryItemService;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 图书controller
 * @date 2024/05/29 10:30
 */
@Controller
@ResponseBody
@RequestMapping("item")
public class LibraryItemController {

    @Autowired
    private LibraryItemService libraryItemService;

    /** 分页获取图书 */
    @PostMapping("getLibraryItemPage")
    public Result getLibraryItemPage(@RequestBody LibraryItem libraryItem) {
        Page<LibraryItem> page = new Page<>(libraryItem.getPageNumber(),libraryItem.getPageSize());
        QueryWrapper<LibraryItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(libraryItem.getName()),LibraryItem::getName,libraryItem.getName())
                .like(StringUtils.isNotBlank(libraryItem.getIsbn()),LibraryItem::getIsbn,libraryItem.getIsbn())
                .like(StringUtils.isNotBlank(libraryItem.getAuthor()),LibraryItem::getAuthor,libraryItem.getAuthor())
                .eq(StringUtils.isNotBlank(libraryItem.getType()),LibraryItem::getType,libraryItem.getType())
                .eq(StringUtils.isNotBlank(libraryItem.getPress()),LibraryItem::getPress,libraryItem.getPress())
                .eq(StringUtils.isNotBlank(libraryItem.getLanguage()),LibraryItem::getLanguage,libraryItem.getLanguage())
                .eq(StringUtils.isNotBlank(libraryItem.getRemark()),LibraryItem::getRemark,libraryItem.getRemark());
        Page<LibraryItem> libraryItemPage = libraryItemService.page(page, queryWrapper);
        return Result.success(libraryItemPage);
    }

    /** 根据id获取图书 */
    @GetMapping("getLibraryItemById")
    public Result getLibraryItemById(@RequestParam("id")String id) {
        LibraryItem libraryItem = libraryItemService.getById(id);
        return Result.success(libraryItem);
    }

    /** 保存图书 */
    @PostMapping("saveLibraryItem")
    public Result saveLibraryItem(@RequestBody LibraryItem libraryItem) {
        boolean save = libraryItemService.save(libraryItem);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑图书 */
    @PostMapping("editLibraryItem")
    public Result editLibraryItem(@RequestBody LibraryItem libraryItem) {
        boolean save = libraryItemService.updateById(libraryItem);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除图书 */
    @GetMapping("removeLibraryItem")
    public Result removeLibraryItem(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                libraryItemService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("图书id不能为空！");
        }
    }

}
