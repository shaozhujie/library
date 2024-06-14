package com.project.library.controller.language;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.library.domain.LibraryLanguage;
import com.project.library.domain.Result;
import com.project.library.enums.ResultCode;
import com.project.library.service.LibraryLanguageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 语种controller
 * @date 2024/05/29 09:36
 */
@Controller
@ResponseBody
@RequestMapping("language")
public class LibraryLanguageController {

    @Autowired
    private LibraryLanguageService libraryLanguageService;

    /** 分页获取语种 */
    @PostMapping("getLibraryLanguagePage")
    public Result getLibraryLanguagePage(@RequestBody LibraryLanguage libraryLanguage) {
        Page<LibraryLanguage> page = new Page<>(libraryLanguage.getPageNumber(),libraryLanguage.getPageSize());
        QueryWrapper<LibraryLanguage> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(libraryLanguage.getName()),LibraryLanguage::getName,libraryLanguage.getName());
        Page<LibraryLanguage> libraryLanguagePage = libraryLanguageService.page(page, queryWrapper);
        return Result.success(libraryLanguagePage);
    }

    @GetMapping("getLibraryLanguageList")
    public Result getLibraryLanguageList() {
        List<LibraryLanguage> languageList = libraryLanguageService.list();
        return Result.success(languageList);
    }

    /** 根据id获取语种 */
    @GetMapping("getLibraryLanguageById")
    public Result getLibraryLanguageById(@RequestParam("id")String id) {
        LibraryLanguage libraryLanguage = libraryLanguageService.getById(id);
        return Result.success(libraryLanguage);
    }

    /** 保存语种 */
    @PostMapping("saveLibraryLanguage")
    public Result saveLibraryLanguage(@RequestBody LibraryLanguage libraryLanguage) {
        boolean save = libraryLanguageService.save(libraryLanguage);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑语种 */
    @PostMapping("editLibraryLanguage")
    public Result editLibraryLanguage(@RequestBody LibraryLanguage libraryLanguage) {
        boolean save = libraryLanguageService.updateById(libraryLanguage);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除语种 */
    @GetMapping("removeLibraryLanguage")
    public Result removeLibraryLanguage(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                libraryLanguageService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("语种id不能为空！");
        }
    }

}
