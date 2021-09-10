package com.xueyi.gen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.xueyi.common.security.annotation.EnableCustomConfig;
import com.xueyi.common.security.annotation.EnableRyFeignClients;
import com.xueyi.common.swagger.annotation.EnableCustomSwagger2;

/**
 * 代码生成
 *
 * @author xueyi
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication
public class XueYiGenApplication {
    public static void main(String[] args) {
        SpringApplication.run(XueYiGenApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  代码生成模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                "  _____     __   ____     __        \n" +
                "  \\   _\\   /  /  \\   \\   /  /   \n" +
                "  .-./ ). /  '    \\  _. /  '       \n" +
                "  \\ '_ .') .'      _( )_ .'        \n" +
                " (_ (_) _) '   ___(_ o _)'          \n" +
                "   /    \\   \\ |   |(_,_)'         \n" +
                "   `-'`-'    \\|   `-'  /           \n" +
                "  /  /   \\    \\\\      /          \n" +
                " '--'     '----'`-..-'              ");
    }
}