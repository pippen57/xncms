package top.pippen.xncms.coll.handle;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hswebframework.ezorm.rdb.operator.ddl.ColumnBuilder;
import org.hswebframework.ezorm.rdb.operator.ddl.TableBuilder;
import org.springframework.stereotype.Component;
import top.pippen.xncms.schema.entity.FieldsEntity;
import top.pippen.xncms.schema.enums.FieldsTypeEnum;

import java.util.Map;

/**
 * @author Pippen
 * @since 2021/04/01 08:54
 */
@Component
@Slf4j
@AllArgsConstructor
public class DateTimeHandle implements FieldsHandles {
    @Override
    public String getType() {
        return FieldsTypeEnum.dateTime.getName();
    }

    @Override
    public TableBuilder columnBud(TableBuilder table, FieldsEntity field) {
        ColumnBuilder comment = table.addColumn()
                .name(field.getName()).datetime().comment(field.getDisplayName());
        if (BooleanUtil.isTrue(field.getRequired())) {
            comment.notNull().commit();
        } else {
            comment.commit();
        }
        return comment.commit();
    }

    @Override
    public Object verifyAndSwitch(Map<String, Object> data, FieldsEntity field) {
        Object o= data.get(field.getName());
        if (o instanceof String) {
            return DateUtil.parse((String) o);
        } else {
            return o;
        }
    }
}

