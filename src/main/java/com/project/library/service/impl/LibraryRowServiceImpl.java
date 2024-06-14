package com.project.library.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.library.domain.LibraryRow;
import com.project.library.mapper.LibraryRowMapper;
import com.project.library.service.LibraryRowService;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 排service实现类
 * @date 2024/05/29 10:00
 */
@Service
public class LibraryRowServiceImpl extends ServiceImpl<LibraryRowMapper, LibraryRow> implements LibraryRowService {
}
