package com.project.library.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.library.domain.LibraryComment;
import com.project.library.mapper.LibraryCommentMapper;
import com.project.library.service.LibraryCommentService;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 图书评论service实现类
 * @date 2024/05/29 03:54
 */
@Service
public class LibraryCommentServiceImpl extends ServiceImpl<LibraryCommentMapper, LibraryComment> implements LibraryCommentService {
}
