package com.shopproject.controller;

import com.shopproject.controller.viewObject.UserVo;
import com.shopproject.error.BusinessException;
import com.shopproject.response.CommonReturnType;
import com.shopproject.service.UserService;
import com.shopproject.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
@Controller("user")
@RequestMapping("/user")
public class UserController extends BaseController
{
    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping(value="/getotp",method={RequestMethod.POST},consumes={"application/x-www-form-urlencoded"})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam("telphone") String telphone) {
        // 生成随机OTP验证码
        Random  random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);

        // 将OTP验证码同对应用户的手机号关联，使用httpsession方式绑定他的手机号与otpcode
        httpServletRequest.getSession().setAttribute(telphone, otpCode);

        // 将opt验证码通过短信通道发送给用户，省略
        System.out.println("telphone " + telphone + "& optcode " + otpCode);

        return CommonReturnType.create(null);
    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name="id") Integer id) throws BusinessException {
        // 调用servece服务获取对应id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);

        // 若错误处理
        if (userModel == null) {
            userModel.setEncrptPassword("123");
//            throw new BusinessException(EmumBusinessError.USER_NOT_EXIST);
        }

        // 转换为供前端使用的viewobject
        UserVo userVo = convertFromModel(userModel);

        return CommonReturnType.create(userVo);
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
