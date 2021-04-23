package top.pippen.xncms.coll.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hswebframework.ezorm.rdb.executor.wrapper.ResultWrappers;
import org.hswebframework.ezorm.rdb.mapping.defaults.SaveResult;
import org.hswebframework.ezorm.rdb.mapping.defaults.record.DefaultRecord;
import org.hswebframework.ezorm.rdb.operator.DatabaseOperator;
import org.hswebframework.ezorm.rdb.operator.dml.query.SortOrder;
import org.hswebframework.web.api.crud.entity.PagerResult;
import org.hswebframework.web.api.crud.entity.QueryParamEntity;
import org.hswebframework.web.exception.ValidationException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.pippen.xncms.coll.enums.DataRangeEnum;
import top.pippen.xncms.coll.handle.FieldsHandles;
import top.pippen.xncms.coll.handle.StringHandle;
import top.pippen.xncms.coll.service.dto.EChartDataParam;
import top.pippen.xncms.coll.service.dto.EChartDataRDto;
import top.pippen.xncms.schema.entity.FieldsEntity;
import top.pippen.xncms.schema.entity.SchemasEntity;
import top.pippen.xncms.utils.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 内容集合操作
 *
 * @author Pippen
 * @since 2021/03/27 08:42
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CollService {

    private final DatabaseOperator operator;

    private final List<FieldsHandles> fieldsHandles;

    public Mono<Boolean> createDb(SchemasEntity s) {
        String collectionName = s.getCollectionName();
        return operator.ddl().createOrAlter(StringUtils.toUnderScoreCase(collectionName))
                .addColumn().name("_id").varchar(255).comment("主键id").primaryKey().commit()
                .addColumn().name(s.getDocCreateTimeField()).datetime().comment("创建时间").commit()
                .addColumn().name(s.getDocUpdateTimeField()).datetime().comment("更新时间").commit()
                .commit().reactive();
    }

    public Mono<Boolean> updateColumn(String collectionName, Mono<FieldsEntity> fields) {

        return fields.flatMap(field -> getFieldHandle(field.getType())
                .flatMap(f -> f.columnBud(operator.ddl().createOrAlter(collectionName), field).commit().reactive()));
    }

    public Mono<PagerResult<Map<String, Object>>> queryPage(Mono<SchemasEntity> schemasEntityMono, QueryParamEntity query) {

        return Mono.zip(
                schemasEntityMono.flatMap(s -> operator.dml()
                        .createReactiveRepository(s.getCollectionName())
                        .createQuery().setParam(query).count()),
                schemasEntityMono.flatMap(s -> operator.dml()
                        .query(s.getCollectionName())
                        .setParam(query)
                        .orderBy(s.getFields().stream().filter(sort -> BooleanUtil.isTrue(sort.getOrderField()))
                                .map((sort) -> "asc".equals(sort.getOrderDirection()) ? SortOrder.asc(sort.getName()) : SortOrder.desc(sort.getName())).toArray(SortOrder[]::new))
                        .fetch(ResultWrappers.mapStream())
                        .reactive().collect(Collectors.toList())),
                PagerResult::of);
    }

    public Mono<SaveResult> saveColl(Mono<Map<String, Object>> data, Mono<SchemasEntity> schemasEntityMono) {
        return data.zipWith(schemasEntityMono).flatMap((tup) -> {
            Map<String, Object> d = tup.getT1();
            SchemasEntity s = tup.getT2();
            toMap(d, s);
            d.put(s.getDocCreateTimeField(), new Date());
            return operator.dml().createReactiveRepository(s.getCollectionName())
                    .save(new DefaultRecord(d));
        });

    }

    public Mono<Integer> updateColl(String id, Mono<Map<String, Object>> data, Mono<SchemasEntity> schemasEntityMono) {
        return data.zipWith(schemasEntityMono).flatMap((tup) -> {
            Map<String, Object> d = tup.getT1();
            SchemasEntity s = tup.getT2();
            toMap(d, s);
            return operator.dml().createReactiveRepository(s.getCollectionName())
                    .createUpdate()
                    .set(new DefaultRecord(d))
                    .where("_id", id).execute();
        });
    }

    public Mono<Integer> deleteById(String id, Mono<SchemasEntity> schemasEntityMono) {
        return schemasEntityMono.flatMap(s -> operator.dml()
                .createReactiveRepository(s.getCollectionName())
                .createDelete().where("_id", id).execute()
        );
    }

    private void toMap(Map<String, Object> d, SchemasEntity s) {
        d.put("_id", IdUtil.fastSimpleUUID());

        d.put(s.getDocUpdateTimeField(), new Date());
        List<FieldsEntity> fields = s.getFields();
        for (FieldsEntity entity : fields) {
            String type = entity.getType();
            FieldsHandles fieldsHandles = getSyncFieldHandle(type);
            Object o = fieldsHandles.verifyAndSwitch(d, entity);
            d.put(entity.getName(), o);
        }
    }

    public Mono<EChartDataRDto> getChartData(Mono<SchemasEntity> schemasEntityMono, Mono<EChartDataParam> eChartDataParamMono) {
        EChartDataRDto chartDataRDto = new EChartDataRDto();
        Mono<List<Map<String, Object>>> listMono = schemasEntityMono.flatMap(s -> {
            chartDataRDto.setTitleName(s.getDisplayName());
            return operator.dml()
                    .query(s.getCollectionName())
                    .orderBy(SortOrder.asc(s.getDocCreateTimeField()))
                    .fetch(ResultWrappers.mapList())
                    .reactive().collect(Collectors.toList());
        });
        return eChartDataParamMono.zipWith(listMono, (e, s) -> {
            EChartDataParam.DataBean xAxis = e.getX();
            List<EChartDataParam.DataBean> yAxis = e.getY();
            DataRangeEnum range = e.getRange();
            s.forEach(_c -> {
                Object o = _c.get(xAxis.getFieldName());
                if (o instanceof LocalDateTime) {
                    o = DateUtil.format((LocalDateTime) o, "yyyy-MM-dd HH:mm");
                }
                List<Object> xData = chartDataRDto.getXData();
                xData.add(o);
                for (int i = 0; i < yAxis.size(); i++) {
                    Object yd = _c.get(yAxis.get(i).getFieldName());
                    List<EChartDataRDto.Series> yData = chartDataRDto.getYData();
                    if (yData.size() <= 0) {
                        series(yAxis.get(i), yd, yData);
                    } else {
                        boolean flag = false;
                        EChartDataRDto.Series series = null;
                        for (int i1 = 0; i1 < yData.size(); i1++) {
                            if (StringUtils.equals(yData.get(i1).getName(), yAxis.get(i).getName())) {
                                flag = true;
                                series = yData.get(i1);
                            }
                        }
                        if (flag){
                            List<Object> data = series.getData();
                            data.add(yd);
                        }else {
                            series(yAxis.get(i), yd, yData);
                        }
                    }
                }
            });
            return chartDataRDto;
        });
    }

    private void series(EChartDataParam.DataBean y, Object yd, List<EChartDataRDto.Series> yData) {
        EChartDataRDto.Series series = new EChartDataRDto.Series();
        List<Object> data = new ArrayList<>();
        data.add(yd);
        series.setName(y.getName());
        series.setData(data);
        yData.add(series);
    }

    private Mono<FieldsHandles> getFieldHandle(String type) {
        return Flux.fromIterable(fieldsHandles)
                .filter(s -> s.getType().equals(type))
                .switchIfEmpty(Mono.error(() -> new ValidationException("字段类型非法")))
                .single();
    }

    private FieldsHandles getSyncFieldHandle(String type) {
        Optional<FieldsHandles> first = fieldsHandles.stream()
                .filter(fh -> fh.getType().equals(type))
                .findFirst();
        return first.orElseGet(StringHandle::new);
    }

}
