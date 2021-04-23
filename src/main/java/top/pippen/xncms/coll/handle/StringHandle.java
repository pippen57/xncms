package top.pippen.xncms.coll.handle;

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
 * 单行文本数据处理
 *
 * @author Pippen
 * @since 2021/03/27 16:36
 */
@Component
@Slf4j
@AllArgsConstructor
public class StringHandle implements FieldsHandles {
    @Override
    public String getType() {
        return FieldsTypeEnum.string.getName();
    }

    @Override
    public TableBuilder columnBud(TableBuilder table, FieldsEntity field) {
        ColumnBuilder comment = table.addColumn()
                .name(field.getName())
                .varchar(field.getMax() == null || field.getMax() == 0 ? 255 : field.getMax())
                .comment(field.getDisplayName());
        if (BooleanUtil.isTrue(field.getRequired())) {
            comment.notNull().commit();
        } else {
            comment.commit();
        }
        return comment.commit();
    }

    @Override
    public Object verifyAndSwitch(Map<String, Object> data, FieldsEntity field) {
        return FieldsHandles.super.verifyAndSwitch(data, field);
    }
}
