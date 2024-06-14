package com.project.library.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.library.domain.LibraryReturn;
import com.project.library.mapper.LibraryReturnMapper;
import com.project.library.service.LibraryReturnService;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 归还service实现类
 * @date 2024/05/29 09:52
 */
@Service
public class LibraryReturnServiceImpl extends ServiceImpl<LibraryReturnMapper, LibraryReturn> implements LibraryReturnService {
}
