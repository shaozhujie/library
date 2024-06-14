package com.project.library.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.library.domain.LibraryItem;
import com.project.library.mapper.LibraryItemMapper;
import com.project.library.service.LibraryItemService;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 图书service实现类
 * @date 2024/05/29 10:30
 */
@Service
public class LibraryItemServiceImpl extends ServiceImpl<LibraryItemMapper, LibraryItem> implements LibraryItemService {
}
