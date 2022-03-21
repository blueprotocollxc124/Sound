package org.linkworld.yuansystem.config;

//改配置文件和Swagger配置文件一致，只是添加了两个注解


import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
//@EnableSwagger2	   //开启 Swagger2
@EnableOpenApi     //开启 Swagger3 ，可不写
@EnableKnife4j     //开启 knife4j ，可不写
 class Knife4jConfig {
    @Bean
    public Docket createRestApi(Environment environment) {
        Profiles dev = Profiles.of("dev");
        boolean isDev = environment.acceptsProfiles(dev);
        // Swagger 2 使用的是：DocumentationType.SWAGGER_2
        // Swagger 3 使用的是：DocumentationType.OAS_30
        return new Docket(DocumentationType.OAS_30)
                // 定义是否开启swagger，false为关闭，可以通过变量控制
                .enable(isDev)
                // 将api的元信息设置为包含在json ResourceListing响应中。
                .apiInfo(new ApiInfoBuilder()
                        .title("嗓音收集数字化系统API——接口文档")
                        // 描述
                        .description("嗓音收集数字化系统API")
                        .contact(new Contact("BlueProtocol", "地址", "1242859747@qq.com"))
                                .version("1.0.0")
                                .build())
                                // 分组名称
                                .groupName("后端接口")
                                // 选择哪些接口作为swagger的doc发布
                                .select()
                                //.apis(RequestHandlerSelectors.any())
                                                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                                //.paths(PathSelectors.any())
                                .build();
    }
}
