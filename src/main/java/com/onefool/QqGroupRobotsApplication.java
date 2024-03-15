package com.onefool;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy  //开启aop
@MapperScan("com.onefool.mapper")
public class QqGroupRobotsApplication {

    private static final Logger logger = LoggerFactory.getLogger(QqGroupRobotsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(QqGroupRobotsApplication.class, args);
        logger.info("spring启动成功==========>");
    }

}
