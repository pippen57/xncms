package top.pippen.xncms.coll.handle;

import cn.hutool.core.util.BooleanUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hswebframework.ezorm.rdb.operator.ddl.ColumnBuilder;
import org.hswebframework.ezorm.rdb.operator.ddl.TableBuilder;
import org.springframework.stereotype.Component;
import top.pippen.xncms.schema.entity.FieldsEntity;
import top.pippen.xncms.schema.enums.FieldsTypeEnum;

/**
 * @author Pippen
 * @since 2021/04/02 16:08
 */
@Component
@Slf4j
@AllArgsConstructor
public class RadioSelectHandle implements FieldsHandles{
    @Override
    public String getType() {
        return FieldsTypeEnum.radioSelect.getName();
    }

    @Override
    public TableBuilder columnBud(TableBuilder table, FieldsEntity field) {
        ColumnBuilder comment = table.addColumn()
                .name(field.getName())
                .integer()
                .comment(field.getDisplayName());
        if (BooleanUtil.isTrue(field.getRequired())) {
            comment.notNull().commit();
        } else {
            comment.commit();
        }
        return comment.commit();
    }
}
