package com.kaishengit.service.impl;

import com.kaishengit.service.UserService;

public class UserServiceImpl implements UserService {
    public String getUserName(Integer id) {
        if(id == 1) {
            return "hanks";
        } else {
            return "tom";
        }
    }
}
