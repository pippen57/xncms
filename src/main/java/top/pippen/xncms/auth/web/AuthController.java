package top.pippen.xncms.auth.web;

import cn.hutool.cache.impl.TimedCache;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import org.hswebframework.web.authorization.ReactiveAuthenticationInitializeService;
import org.hswebframework.web.authorization.ReactiveAuthenticationManager;
import org.hswebframework.web.authorization.annotation.Authorize;
import org.hswebframework.web.authorization.events.AuthorizationSuccessEvent;
import org.hswebframework.web.authorization.exception.AuthenticationException;
import org.hswebframework.web.authorization.simple.PlainTextUsernamePasswordAuthenticationRequest;
import org.hswebframework.web.exception.NotFoundException;
import org.hswebframework.web.system.authorization.api.entity.UserEntity;
import org.hswebframework.web.system.authorization.defaults.service.DefaultReactiveUserService;
import org.hswebframework.web.validator.ValidatorUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import top.pippen.xncms.auth.service.UserDetailService;
import top.pippen.xncms.auth.service.XnWxService;
import top.pippen.xncms.auth.web.request.AuthParameter;
import top.pippen.xncms.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Pippen
 * @since 2021/04/15 09:27
 */
@Hidden
@RestController
@AllArgsConstructor
@Authorize(ignore = true)
@RequestMapping("/user/wechat")
public class AuthController {

    private final XnWxService xnWxService;
    private final DefaultReactiveUserService defaultReactiveUserService;
    private final UserDetailService userDetailService;
    private final TimedCache<String, String> timedCache;
    private final ReactiveAuthenticationManager authenticationManager;

    private final ReactiveAuthenticationInitializeService initializeService;

    private final ApplicationEventPublisher eventPublisher;

    @GetMapping("/qr")
    public Map<String, String> getLoginQr() {
        return xnWxService.createWxaCodeUnlimit();
    }


    /**
     * 小程序扫码发送确认登录信息
     */
    @PostMapping("/qr/save")
    public Mono<Boolean> qrLoginSave(@RequestBody AuthParameter request) {
        ValidatorUtils.tryValidate(request);
        String uuid = request.getUuid();
        String s1 = timedCache.get(uuid);
        String encode = StringUtils.usernameEncoder(request.getOpenId());
        return defaultReactiveUserService.findByUsername(encode)
                .switchIfEmpty(Mono.error(NotFoundException::new))
                .map(s -> {

                    timedCache.put(uuid, s.getUsername());
                    return true;
                });
    }

    /**
     * 微信小程序通过此接口进行登录,只需要通过openId 进行认证就可以
     *
     * @param parameter
     * @return
     */
    @PostMapping("/wx/login")
    public Mono<Map<String, Object>> wxLogin(@RequestBody Mono<Map<String, Object>> parameter) {
        return parameter.flatMap(parameters -> {
            String openId = (String) parameters.get("openId");
            // 加密后的openId
            String encode = StringUtils.usernameEncoder(openId);
            Function<String, Object> parameterGetter = parameters::get;
            return defaultReactiveUserService.findByUsername(encode)
                    .switchIfEmpty(userDetailService.register(openId))
                    .flatMap(s-> this.loginInfo(s,parameterGetter));


        });
    }

    @PostMapping("/web/login")
    public Mono<Map<String, Object>> webLogin(@RequestBody Mono<Map<String, Object>> parameter) {

        return parameter.flatMap(parameters -> {
            String uuid = (String) parameters.get("uuid");
            String openId = timedCache.get(uuid);
            Function<String, Object> parameterGetter = parameters::get;
            if (StringUtils.isBlank(openId)) {
                return Mono.just(new HashMap<>(8));
            }
            timedCache.remove(uuid);
            return defaultReactiveUserService.findByUsername(openId)
                    .switchIfEmpty(Mono.error(NotFoundException::new))
                    .flatMap(s-> this.loginInfo(s,parameterGetter));


        });

    }

    private Mono<Map<String, Object>> loginInfo(UserEntity userEntity,Function<String, Object> parameterGetter){
        return authenticationManager.authenticate(Mono.just(new PlainTextUsernamePasswordAuthenticationRequest(userEntity.getUsername(), "xn-cms-admin")))
                .switchIfEmpty(Mono.error(() -> new AuthenticationException(AuthenticationException.ILLEGAL_PASSWORD, "密码错误")))
                .flatMap(auth -> {
                    //触发授权成功事件
                    AuthorizationSuccessEvent event = new AuthorizationSuccessEvent(auth, parameterGetter);
                    return event
                            .publish(eventPublisher)
                            .then(Mono.fromCallable(event::getResult));
                });
    }
}
