package com.shopproject.controller;

import com.shopproject.controller.viewObject.UserVo;
import com.shopproject.error.BusinessException;
import com.shopproject.error.EmumBusinessError;
import com.shopproject.response.CommonReturnType;
import com.shopproject.service.UserService;
import com.shopproject.service.model.UserModel;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    // 用户注册接口
    @RequestMapping(value="/register",method={RequestMethod.POST},consumes={"application/x-www-form-urlencoded"})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name="telphone")String telphone,
                                     @RequestParam(name="otpCode")String otpCode,
                                     @RequestParam(name="name")String name,
                                     @RequestParam(name="gender")Byte gender,
                                     @RequestParam(name="age")Integer age,
                                     @RequestParam(name="password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        // 验证手机号和对应的otpcode符合
        String inSessionOtpCode = (String)httpServletRequest.getSession().getAttribute(telphone);
        if(!com.alibaba.druid.util.StringUtils.equals(otpCode, inSessionOtpCode)) {
            throw new BusinessException(EmumBusinessError.PARAMETER_VALIDATION_ERROR, "短信验证码错误");
        }
        // 用户注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(gender);
        userModel.setAge(age);
        userModel.setTelphone(telphone);
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPassword(this.EncodeByMD5(password));

        userService.register(userModel);
        return CommonReturnType.create(null);
    }
    public String EncodeByMD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // 确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        // 加密字符串
        String newStr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return newStr;
    }

    // 用户获取otp短信
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
