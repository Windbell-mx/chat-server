package com.windbell.mm.service;

import com.windbell.mm.model.entities.User;

public interface UserLoginService {


    String login(String account, String password);

    void register(User user);
}
