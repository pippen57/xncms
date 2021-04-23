package top.pippen.xncms.schema.web;

import io.r2dbc.spi.R2dbcDataIntegrityViolationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hswebframework.web.api.crud.entity.QueryOperation;
import org.hswebframework.web.api.crud.entity.QueryParamEntity;
import org.hswebframework.web.authorization.annotation.Authorize;
import org.hswebframework.web.authorization.annotation.QueryAction;
import org.hswebframework.web.authorization.annotation.Resource;
import org.hswebframework.web.authorization.annotation.SaveAction;
import org.hswebframework.web.crud.service.ReactiveCrudService;
import org.hswebframework.web.crud.web.reactive.ReactiveServiceCrudController;
import org.hswebframework.web.exception.BusinessException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.pippen.xncms.coll.service.CollService;
import top.pippen.xncms.schema.entity.FieldsEntity;
import top.pippen.xncms.schema.entity.SchemasEntity;
import top.pippen.xncms.schema.service.SchemasService;

/**
 * @author Pippen
 * @since 2021/03/25 14:14
 */

@Slf4j
@RestController
@RequestMapping("/schema")
@AllArgsConstructor
@Authorize
@Resource(id = "schema", name = "内容管理")
@Tag(name = "内容模型")
public class SchemasController implements ReactiveServiceCrudController<SchemasEntity, String> {

    private final SchemasService schemasService;
    private final CollService collService;

    @Override
    public ReactiveCrudService<SchemasEntity, String> getService() {
        return this.schemasService;
    }

    @GetMapping("/query/no-paging")
    @QueryAction
    @QueryOperation(summary = "使用GET方式分页动态查询(不返回总数)",
            description = "此操作不返回分页总数,如果需要获取全部数据,请设置参数paging=false")
    public Flux<SchemasEntity> queryNoPaging(@Parameter(hidden = true) QueryParamEntity query) {
        return this.schemasService.queryByPage(query);
    }

    @PostMapping("/save/create")
    @SaveAction
    @Operation(
            summary = "新增单个数据"
    )
    public Mono<Boolean> addAndCreate(@Validated @RequestBody Mono<SchemasEntity> payload) {
        return ReactiveServiceCrudController.super.add(payload)
                .flatMap(collService::createDb)
                .onErrorMap(R2dbcDataIntegrityViolationException.class, err -> new BusinessException("内容模型已存在", err));
    }

    @PutMapping("/update/{id}/{name}")
    @SaveAction
    @Operation(
            summary = "添加字段"
    )
    public Mono<Boolean> updateById(@Validated @RequestBody Mono<FieldsEntity> payload, @PathVariable String id, @PathVariable String name) {
        return schemasService.updateFields(id, name, payload);
    }


}
