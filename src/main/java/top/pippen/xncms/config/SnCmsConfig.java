package top.pippen.xncms.config;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Pippen
 * @since 2021/04/14 17:20
 */
@Configuration
public class SnCmsConfig {

    @Bean
    public TimedCache<String, String> timedCache(){
        TimedCache<String, String> objects = CacheUtil.newTimedCache(1000 * 60 * 5);
        objects.schedulePrune(500);
        return objects;
    }
}
