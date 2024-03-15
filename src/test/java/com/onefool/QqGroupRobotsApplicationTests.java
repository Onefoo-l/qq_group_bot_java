package com.onefool;

import com.onefool.utils.HttpClientGptApi;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
class QqGroupRobotsApplicationTests {

    @Test
    void contextLoads() {
        String s = HttpClientGptApi.openAi("你最新的查询结果日期是多少？");
        System.out.println(s);
    }

}
