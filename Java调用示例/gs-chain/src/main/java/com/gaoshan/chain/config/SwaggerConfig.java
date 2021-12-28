package com.gaoshan.chain.config;

import com.gaoshan.common.config.BaseSwaggerConfig;
import com.gaoshan.common.domain.SwaggerProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger API文档相关配置
 * Created by macro on 2018/4/26.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.gaoshan.chain.controller")
                .title("GS-企业后台管理系统（多租户）")
                .description("GS-企业后台管理系统相关接口文档（多租户）")
                .contactName("Gaoshan")
                .version("1.0")
                .enableSecurity(true)
                .build();
    }
}
