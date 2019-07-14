package com.shopproject.controller;

import com.shopproject.controller.viewObject.UserVo;
import com.shopproject.service.UserService;
import com.shopproject.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("user")
@RequestMapping("/user")
public class UserController
{
    @Autowired
    private UserService userService;

    @RequestMapping("/get")
    @ResponseBody
    public UserVo getUser(@RequestParam(name="id") Integer id)
    {
        // 调用servece服务获取对应id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);
        return convertFromModel(userModel);
    }

    private UserVo convertFromModel(UserModel userModel) {
        if (userModel == null){
            return null;
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userModel, userVo);
        return userVo;
    }
}
