package com.gaoshan.chain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * 应用启动入口
 * Created by macro on 2018/4/26.
 */
@EnableConfigurationProperties
@SpringBootApplication
//@EnableTransactionManagement
@ComponentScan(basePackages = {"com.gaoshan.chain","com.gaoshan.common"})
public class GsChainApplication {
    public static void main(String[] args) {
        SpringApplication.run(GsChainApplication.class, args);
    }
}
