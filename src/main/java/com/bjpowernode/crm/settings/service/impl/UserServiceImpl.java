package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    private UserDao userDao= SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public User login(String username, String pwd, String ip) throws LoginException {
        Map<String,String> map=new HashMap<>();
        map.put("username",username);
        map.put("pwd",pwd);
        User user=userDao.login(map);

        if(user==null){
            throw new LoginException("账号或密码错误");
        }

        String time = DateTimeUtil.getSysTime();
        String expireTime = user.getExpireTime();
        if(time.compareTo(expireTime)>0){
            throw new LoginException("账户已过期");
        }

        String lockState = user.getLockState();
        if("0".equals(lockState)){
            throw new LoginException("账户已锁定");
        }

//        if(!user.getAllowIps().contains(ip)){
//            throw new LoginException("ip地址受限");
//        }
        return user;
    }

    @Override
    public List<User> getUserList() {
        List<User> ulist=userDao.getUserList();
        return ulist;
    }
}
