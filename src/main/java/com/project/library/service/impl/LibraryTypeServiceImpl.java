package com.project.library.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.library.domain.LibraryType;
import com.project.library.mapper.LibraryTypeMapper;
import com.project.library.service.LibraryTypeService;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 图书类别service实现类
 * @date 2024/05/29 09:05
 */
@Service
public class LibraryTypeServiceImpl extends ServiceImpl<LibraryTypeMapper, LibraryType> implements LibraryTypeService {
}
