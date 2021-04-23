package top.pippen.xncms.schema.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 字段
 *
 * @author Pippen
 * @since 2021/03/25 15:00
 */
@Getter
@Setter
@NoArgsConstructor
public class FieldsEntity {

    /**
     * 日期格式化
     */
    private String dateFormatType;
    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 字段说明
     */
    private String description;
    /**
     * 显示名
     */
    private String displayName;

    /**
     * 字段id
     */
    private String id;

    /**
     * 是否是系统字段
     */
    private Boolean system;
    /**
     * 数据库字段名
     */
    @NotBlank
    private String name;
    /**
     * 字段类型
     */
    @NotBlank(message = "字段类型不能为空")
    private String type;
    /**
     * 排序方式
     */
    private String orderDirection;

    private List<OptionsBean> options;

    /**
     * 是否必填
     */
    private Boolean required;
    /**
     * 是否隐藏
     */
    private Boolean hidden;

    /**
     * 是否排序
     */
    private Boolean orderField;
    /**
     * 最大长度
     */
    private Integer max;
    /**
     * 最小程度
     */
    private Integer min;

    public FieldsEntity(String dateFormatType, String description, String displayName,
                        String id, Boolean system, String name, String type) {
        this.dateFormatType = dateFormatType;
        this.description = description;
        this.displayName = displayName;
        this.id = id;
        this.system = system;
        this.name = name;
        this.type = type;

    }
    public FieldsEntity(String dateFormatType, String description, String displayName,
                        String id, Boolean system, String name, String type,Boolean orderField) {
        this.dateFormatType = dateFormatType;
        this.description = description;
        this.displayName = displayName;
        this.id = id;
        this.system = system;
        this.name = name;
        this.type = type;
        this.orderField = orderField;

    }
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("dateFormatType", dateFormatType)
                .append("defaultValue", defaultValue)
                .append("description", description)
                .append("displayName", displayName)
                .append("id", id)
                .append("system", system)
                .append("name", name)
                .append("type", type)
                .append("orderDirection", orderDirection)
                .append("required", required)
                .append("hidden", hidden)
                .append("orderField", orderField)
                .append("max", max)
                .append("min", min)
                .toString();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class OptionsBean {
        private String value;
        private String label;
    }
}
