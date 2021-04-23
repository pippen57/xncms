package top.pippen.xncms.coll.handle;

import cn.hutool.core.util.BooleanUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hswebframework.ezorm.rdb.operator.ddl.ColumnBuilder;
import org.hswebframework.ezorm.rdb.operator.ddl.TableBuilder;
import org.springframework.stereotype.Component;
import top.pippen.xncms.schema.entity.FieldsEntity;
import top.pippen.xncms.schema.enums.FieldsTypeEnum;

import java.sql.JDBCType;

/**
 * 数字处理
 *
 * @author Pippen
 * @since 2021/04/02 09:04
 */
@Component
@Slf4j
@AllArgsConstructor
public class NumberHandle implements FieldsHandles {
    @Override
    public String getType() {
        return FieldsTypeEnum.number.getName();
    }

    @Override
    public TableBuilder columnBud(TableBuilder table, FieldsEntity field) {
        ColumnBuilder comment = table.addColumn()
                .name(field.getName())
                .type(JDBCType.DOUBLE, Double.class)
                .comment(field.getDisplayName());
        if (BooleanUtil.isTrue(field.getRequired())) {
            comment.notNull().commit();
        } else {
            comment.commit();
        }
        return comment.commit();
    }

}
