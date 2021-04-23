package top.pippen.xncms.coll.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hswebframework.ezorm.rdb.mapping.defaults.SaveResult;
import org.hswebframework.web.api.crud.entity.PagerResult;
import org.hswebframework.web.api.crud.entity.QueryParamEntity;
import org.hswebframework.web.authorization.annotation.*;
import org.hswebframework.web.exception.ValidationException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import top.pippen.xncms.coll.service.CollService;
import top.pippen.xncms.coll.service.dto.EChartDataParam;
import top.pippen.xncms.coll.service.dto.EChartDataRDto;
import top.pippen.xncms.schema.service.SchemasService;

import java.util.Map;

/**
 * @author Pippen
 * @since 2021/03/31 11:42
 */
@Slf4j
@RestController
@RequestMapping("/coll")
@AllArgsConstructor
@Authorize
@Resource(id = "coll", name = "内容管理")
@Tag(name = "内容集合")
public class CollController {

    private final CollService collService;
    private final SchemasService schemasService;


    @GetMapping("/{id}")
    @Operation(
            summary = "查询数据"
    )
    @QueryAction
    public Mono<PagerResult<Map<String, Object>>> queryPager(@Parameter(hidden = true) QueryParamEntity query,
                                                             @PathVariable String id) {

        if (query.getPageSize()==-1){
            query.setPaging(false);
        }
        return schemasService.findById(id)
                .switchIfEmpty(Mono.error(() -> new ValidationException("未查询到")))
                .as(s -> collService.queryPage(s, query));
    }

    @PostMapping("/{id}")
    @Operation(
            summary = "新增单个数据"
    )
    @CreateAction
    public Mono<SaveResult> saveData(@RequestBody Mono<Map<String, Object>> payload,
                                     @PathVariable String id) {
        return schemasService.findById(id)
                .switchIfEmpty(Mono.error(() -> new ValidationException("未查询到")))
                .as(s -> collService.saveColl(payload, s));

    }

    @PutMapping("/{id}/{dataId}")
    @Operation(
            summary = "修改数据"
    )
    @CreateAction
    public Mono<Integer> updateData(@RequestBody Mono<Map<String, Object>> payload,
                                    @PathVariable String id, @PathVariable String dataId) {
        return schemasService.findById(id)
                .switchIfEmpty(Mono.error(() -> new ValidationException("未查询到")))
                .as(s -> collService.updateColl(dataId, payload, s));
    }


    @DeleteMapping("/{id}/{dataId}")
    @Operation(
            summary = "删除数据"
    )
    @DeleteAction
    public Mono<Integer> deleteData(@PathVariable String id, @PathVariable String dataId) {
        return schemasService.findById(id)
                .switchIfEmpty(Mono.error(() -> new ValidationException("未查询到")))
                .as(s -> collService.deleteById(dataId, s));
    }

    @QueryAction
    @PutMapping("/echart/{id}")
    public Mono<EChartDataRDto> getEchartData(@PathVariable String id, @RequestBody EChartDataParam eChartDataParamMono) {
        return collService.getChartData(schemasService.findById(id)
                        .switchIfEmpty(Mono.error(() -> new ValidationException("未查询到"))),
                Mono.just(eChartDataParamMono));
    }

}
