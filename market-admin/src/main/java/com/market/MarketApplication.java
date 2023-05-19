package com.market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author ph
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MarketApplication {
    public static void main(String[] args) {
        SpringApplication.run(MarketApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  营销平台启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
