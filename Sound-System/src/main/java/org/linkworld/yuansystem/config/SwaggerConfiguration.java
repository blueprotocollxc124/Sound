package org.linkworld.yuansystem.config;

/*@Function  RestFul API文档在线生成
 *@Author  LiuXiangCheng
 *@Since   2021/11/30  20:30
 */

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;

@Configuration
@EnableOpenApi
@EnableKnife4j
public class SwaggerConfiguration extends WebMvcConfigurationSupport {

/*
    @Bean
    public Docket getDocket(Environment environment) {
        Profiles dev = Profiles.of("dev");
        boolean isDev = environment.acceptsProfiles(dev);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .enable(isDev)
                .select()
                .apis(RequestHandlerSelectors.any())
                .build();

    }




    private ApiInfo getApiInfo() {
        Contact contact = new Contact("LinkWorld","http://180.76.164.146:880/teamwork/lingwo/","liuxiangcheng@stu.gdou.edu.cn");
        return new ApiInfo("嗓音收集系统",
                "为了更好的声乐",
                "v1.0",
                "urn:tos",
                contact,
                "Will get license",
                "...",
                new ArrayList<>());
    }
*/

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(
                "classpath:/static/");
        registry.addResourceHandler("doc.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
    }
}
