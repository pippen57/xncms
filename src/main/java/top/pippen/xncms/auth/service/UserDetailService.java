package top.pippen.xncms.auth.service;

import cn.hutool.core.util.IdUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hswebframework.web.bean.FastBeanCopier;
import org.hswebframework.web.crud.service.GenericReactiveCrudService;
import org.hswebframework.web.system.authorization.api.entity.AuthorizationSettingEntity;
import org.hswebframework.web.system.authorization.api.entity.UserEntity;
import org.hswebframework.web.system.authorization.api.event.UserCreatedEvent;
import org.hswebframework.web.system.authorization.api.service.reactive.ReactiveUserService;
import org.hswebframework.web.system.authorization.defaults.service.DefaultAuthorizationSettingService;
import org.hswebframework.web.system.authorization.defaults.service.DefaultReactiveUserService;
import org.hswebframework.web.validator.ValidatorUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.pippen.xncms.auth.entity.UserDetail;
import top.pippen.xncms.auth.entity.UserDetailEntity;
import top.pippen.xncms.auth.service.request.SaveUserDetailRequest;
import top.pippen.xncms.exception.BadRequestException;
import top.pippen.xncms.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author pippen
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserDetailService extends GenericReactiveCrudService<UserDetailEntity, String> {

    private final ReactiveUserService userService;
    private final DefaultReactiveUserService defaultReactiveUserService;
    private final DefaultAuthorizationSettingService defaultAuthorizationSettingService;
    private final static UserDetailEntity emptyDetail = new UserDetailEntity();

    public Mono<UserDetail> findUserDetail(String userId) {
        return Mono
                .zip(
                        userService.findById(userId), // 基本信息
                        this.findById(userId).defaultIfEmpty(emptyDetail), // 详情
                        UserDetail::of
                );
    }

    public Mono<UserEntity> register(String openId) {
        if (StringUtils.isBlank(openId)) {
            throw new BadRequestException("参数异常");
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword("xn-cms-admin");
        userEntity.setName("小程序用户");
        userEntity.setUsername(StringUtils.usernameEncoder(openId));
        userEntity.setCreateTimeNow();
        userEntity.setCreatorId("1199596756811550720");
        Mono<UserEntity> just = Mono.just(userEntity);
        return defaultReactiveUserService.saveUser(just).thenReturn(userEntity);

    }

    @EventListener
    public void handWxUserRegister(UserCreatedEvent userCreatedEvent) {
        UserEntity userEntity = userCreatedEvent.getUserEntity();
        log.debug("添加用户: {}", userEntity);
        Mono.just(userEntity).flatMap(s -> {
            List<AuthorizationSettingEntity> settingEntities = new ArrayList<>();
            AuthorizationSettingEntity authorizationSettingEntity = new AuthorizationSettingEntity();
            authorizationSettingEntity.setPermission("file");
            authorizationSettingEntity.setDimensionType("user");
            authorizationSettingEntity.setDimensionTypeName("用户");
            authorizationSettingEntity.setDimensionTarget(s.getId());
            authorizationSettingEntity.setDimensionTargetName(s.getId());
            authorizationSettingEntity.setState((byte) 1);
            authorizationSettingEntity.setPriority(10);
            authorizationSettingEntity.setMerge(true);
            Set<String> set = new HashSet<>();
            set.add("upload-static");
            authorizationSettingEntity.setActions(set);
            settingEntities.add(authorizationSettingEntity);
            AuthorizationSettingEntity settingEntity = new AuthorizationSettingEntity();
            settingEntity.setPermission("schema");
            settingEntity.setDimensionType("user");
            settingEntity.setDimensionTypeName("用户");
            settingEntity.setDimensionTarget(s.getId());
            settingEntity.setDimensionTargetName(s.getId());
            settingEntity.setState((byte) 1);
            settingEntity.setPriority(10);
            settingEntity.setMerge(true);
            Set<String> set1 = new HashSet<>();
            set1.add("query");
            set1.add("save");
            set1.add("delete");
            settingEntity.setActions(set1);
            settingEntities.add(settingEntity);
            AuthorizationSettingEntity entity = new AuthorizationSettingEntity();
            entity.setPermission("coll");
            entity.setDimensionType("user");
            entity.setDimensionTypeName("用户");
            entity.setDimensionTarget(s.getId());
            entity.setDimensionTargetName(s.getId());
            entity.setState((byte) 1);
            entity.setPriority(10);
            entity.setMerge(true);
            Set<String> set2 = new HashSet<>();
            set2.add("query");
            set2.add("save");
            set2.add("add");
            set2.add("delete");
            entity.setActions(set2);
            settingEntities.add(entity);
            return defaultAuthorizationSettingService.insertBatch(Flux.just(settingEntities));
        }).subscribe();

    }

    public Mono<UserDetailEntity> findOpenId(String openId) {
        return createQuery()
                .and("open_id", openId)
                .fetchOne();
    }

    public Mono<Void> saveUserDetail(SaveUserDetailRequest request) {
        ValidatorUtils.tryValidate(request);
        UserDetailEntity entity = FastBeanCopier.copy(request, new UserDetailEntity());
        entity.setId(IdUtil.fastSimpleUUID());

        return save(Mono.just(entity))
                .then();
    }
}
