package com.shopproject;

import com.shopproject.dao.UserDOMapper;
import com.shopproject.dataobject.UserDO;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages = {"com.shopproject"})
@RestController
@MapperScan("com.shopproject.dao")
public class App 
{
    @Resource
    private UserDOMapper userDOMapper;

    @RequestMapping("/")
    public String Home()
    {
        UserDO userDO = userDOMapper.selectByPrimaryKey(1);
        if(userDO == null) {
            return "no user";
        } else {
            return userDO.getName();
        }
    }
    public static void main( String[] args )

    {
        System.out.println( "Hello World!" );
        SpringApplication.run(App.class, args);
    }
}
