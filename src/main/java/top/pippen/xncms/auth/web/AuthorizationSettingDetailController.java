package top.pippen.xncms.auth.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.hswebframework.web.authorization.annotation.Authorize;
import org.hswebframework.web.authorization.annotation.Resource;
import org.hswebframework.web.authorization.annotation.SaveAction;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.pippen.xncms.auth.service.AuthorizationSettingDetailService;
import top.pippen.xncms.auth.web.request.AuthorizationSettingDetail;

@RestController
@RequestMapping("/autz-setting/detail")
@Authorize
@Resource(
    id = "autz-setting",
    name = "权限分配",
    group = "system"
)
@AllArgsConstructor
@Tag(name = "权限分配")
public class AuthorizationSettingDetailController {

    private final AuthorizationSettingDetailService settingService;

    @PostMapping("/_save")
    @SaveAction
    @Operation(summary = "赋权")
    public Mono<Boolean> saveSettings(@RequestBody Flux<AuthorizationSettingDetail> detailFlux) {

        return settingService
            .saveDetail(detailFlux)
            .thenReturn(true);
    }

    @GetMapping("/{targetType}/{target}")
    @SaveAction
    @Operation(summary = "获取权限详情")
    public Mono<AuthorizationSettingDetail> getSettings(@PathVariable @Parameter(description = "权限类型") String targetType,
                                                        @PathVariable @Parameter(description = "权限类型对应数据ID") String target) {

        return settingService
            .getSettingDetail(targetType, target)
            ;
    }

}
