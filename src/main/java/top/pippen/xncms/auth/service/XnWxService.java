package top.pippen.xncms.auth.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.codec.Base64;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.hswebframework.web.id.SnowflakeIdGenerator;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Pippen
 * @since 2021/04/14 15:33
 */
@Service
@RequiredArgsConstructor
public class XnWxService {
    private final WxMaService maService;
    private final SnowflakeIdGenerator snowflakeIdGenerator = SnowflakeIdGenerator.getInstance();
    private final TimedCache<String, String> timedCache;

    @SneakyThrows
    public Map<String, String> createWxaCodeUnlimit() {
        String uuid = snowflakeIdGenerator.nextId()+"";
        File wxaCode = this.maService.getQrcodeService()
                .createWxaCodeUnlimit("xn=" + uuid, "pages/index/index");
        String encode = Base64.encode(wxaCode);
        Map<String, String> map = new HashMap<>();
        map.put("src", "data:image/jpg;base64," + encode);
        map.put("uuid", uuid);
        timedCache.put(uuid, "");
        return map;
    }
}
