package com.project.library.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.library.service.UserService;
import com.project.library.domain.User;
import com.project.library.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @description: 用户service实现类
 * @date 2023/8/28 8:46
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 分页查询用户
     */
    @Override
    public Page<User> getUserPage(User user) {
        Page<User> page = new Page<>(user.getPageNumber(),user.getPageSize());
        return baseMapper.getUserPage(page,user);
    }
}
