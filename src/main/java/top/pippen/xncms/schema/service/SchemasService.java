package top.pippen.xncms.schema.service;

import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hswebframework.web.api.crud.entity.QueryParamEntity;
import org.hswebframework.web.authorization.Authentication;
import org.hswebframework.web.crud.service.GenericReactiveCrudService;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.pippen.xncms.coll.service.CollService;
import top.pippen.xncms.schema.entity.FieldsEntity;
import top.pippen.xncms.schema.entity.SchemasEntity;
import top.pippen.xncms.schema.enums.DateFormatTypeEnum;
import top.pippen.xncms.schema.enums.FieldsTypeEnum;
import top.pippen.xncms.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pippen
 * @since 2021/03/25 14:10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SchemasService extends GenericReactiveCrudService<SchemasEntity, String> {

    private final CollService collService;
    private static final List<FieldsEntity> DEFAULT_FIELD = new ArrayList<>();

    static {
        DEFAULT_FIELD.add(
                new FieldsEntity(DateFormatTypeEnum.timestamp_ms.getText(),
                        "系统字段，请勿随意修改",
                        "创建时间",
                        "_createTime",
                        true, "_create_time", FieldsTypeEnum.dateTime.getName(), true)
        );
        DEFAULT_FIELD.add(
                new FieldsEntity(DateFormatTypeEnum.timestamp_ms.getText(),
                        "系统字段，请勿随意修改",
                        "修改时间",
                        "_updateTime",
                        true, "_update_time", FieldsTypeEnum.dateTime.getName())
        );
    }

    private static void accept(SchemasEntity s) {
        s.of(DEFAULT_FIELD);
    }

    public Flux<SchemasEntity> queryByPage(QueryParamEntity query){
        return Authentication.currentReactive().flatMapMany(s ->
                getRepository()
                        .createQuery()
                        //.setParam(query)
                        .and(SchemasEntity::getUserId, s.getUser().getId())
                        .fetch()
        );

    }

    @Override
    public Mono<Integer> insert(Publisher<SchemasEntity> entityPublisher) {
        return Flux.from(entityPublisher)
                .doOnNext(SchemasService::accept)
                .zipWith(Authentication.currentReactive(), ((u, e) -> {
                    u.setUserId(e.getUser().getId());
                    return u;
                }))
                .as(super::insert);
    }

    @Transactional(
            transactionManager = "connectionFactoryTransactionManager"
    )
    public Mono<Boolean> updateFields(String id, String collectionName, Publisher<FieldsEntity> fields) {
        return Mono.zip(super.findById(id), Mono.from(fields))
                .flatMap(res -> {
                    SchemasEntity t1 = res.getT1();
                    FieldsEntity t2 = res.getT2();
                    t2.setId(IdUtil.simpleUUID());
                    t2.setName(StringUtils.toUnderScoreCase(t2.getName()));
                    List<FieldsEntity> list = t1.getFields();
                    list.add(t2);
                    t1.setFields(list);
                    return Authentication.currentReactive().flatMap(s -> createUpdate()
                            .set(SchemasEntity::getFields, list)
                            .where(SchemasEntity::getId, id)
                            .and(SchemasEntity::getUserId, s.getUser().getId())
                            .execute().thenReturn(t2));

                })
                .as(s -> collService.updateColumn(collectionName, s));
    }


}
