package com.project.library.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.library.domain.LibraryBorrow;
import com.project.library.mapper.LibraryBorrowMapper;
import com.project.library.service.LibraryBorrowService;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 借阅service实现类
 * @date 2024/05/29 02:26
 */
@Service
public class LibraryBorrowServiceImpl extends ServiceImpl<LibraryBorrowMapper, LibraryBorrow> implements LibraryBorrowService {
}
