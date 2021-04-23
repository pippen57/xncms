package top.pippen.xncms.coll.handle;

import org.hswebframework.ezorm.rdb.operator.ddl.TableBuilder;
import org.hswebframework.web.exception.ValidationException;
import top.pippen.xncms.schema.entity.FieldsEntity;
import top.pippen.xncms.utils.StringUtils;

import java.util.Map;

/**
 * @author Pippen
 * @since 2021/03/27 11:27
 */
public interface FieldsHandles {
    /**
     * 获取类型
     *
     * @return
     */
    String getType();

    /**
     * 字段构造器
     *
     * @param table
     * @param field
     * @return
     */
    TableBuilder columnBud(TableBuilder table, FieldsEntity field);

    /**
     * 数据校验与转换
     *
     * @param data
     * @param field
     * @return
     */
    default Object verifyAndSwitch(Map<String, Object> data, FieldsEntity field) {
        Object o = data.get(field.getName());
        if (o instanceof String) {
            if (field.getRequired() && StringUtils.isBlank((String) o)) {
                throw new ValidationException("必填字段[ " + field.getDisplayName() + " ] 不能为空");
            }
        } else {
            return o;
        }
        return o;
    }
}
