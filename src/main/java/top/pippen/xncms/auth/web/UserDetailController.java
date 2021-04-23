package top.pippen.xncms.auth.web;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.hswebframework.web.authorization.Authentication;
import org.hswebframework.web.authorization.exception.UnAuthorizedException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import top.pippen.xncms.auth.entity.UserDetail;
import top.pippen.xncms.auth.service.UserDetailService;
import top.pippen.xncms.auth.service.XnWxService;
import top.pippen.xncms.auth.service.request.SaveUserDetailRequest;

/**
 * @author pippen
 */
@RestController
@RequestMapping("/user/detail")
@AllArgsConstructor
@Tag(name = "用户信息接口")
@Hidden
public class UserDetailController {

    private final UserDetailService userDetailService;
    private final XnWxService xnWxService;

    /**
     * 获取当前登录用户详情
     *
     * @return 用户详情
     */
    @GetMapping
    @Operation(summary = "获取当前登录用户详情")
    public Mono<UserDetail> getCurrentLoginUserDetail() {
        return Authentication
            .currentReactive()
            .switchIfEmpty(Mono.error(UnAuthorizedException::new))
            .flatMap(autz -> userDetailService.findUserDetail(autz.getUser().getId()));
    }

    /**
     * 保存当前用户详情
     *
     * @return 用户详情
     */
    @PutMapping
    @Operation(summary = "保存当前用户详情")
    public Mono<Void> saveUserDetail(@RequestBody Mono<SaveUserDetailRequest> request) {
        return request
            .switchIfEmpty(Mono.error(UnAuthorizedException::new))
            .flatMap(userDetailService::saveUserDetail);
    }




}