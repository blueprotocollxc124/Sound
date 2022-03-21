package org.linkworld.yuansystem.config;

/*@Function  MPConfig
 *@Author  LiuXiangCheng
 *@Since   2021/11/30  21:09
 */

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class MybatisPlusConfiguration {
    // 性能分页插件，查询慢SQL
    @Bean
    @Profile({"dev","test"}) // 设置 dev，test 环境开启性能分析，保证我们的效率
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        // 设置SQL的最大执行时间为1ms,超过则报错
        performanceInterceptor.setMaxTime(1000);
        // 是否格式化代码
        performanceInterceptor.setFormat(true);
        return performanceInterceptor;
    }

    // 分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return  new PaginationInterceptor();
    }
}
