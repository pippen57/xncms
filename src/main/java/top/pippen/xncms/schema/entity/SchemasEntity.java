package top.pippen.xncms.schema.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hswebframework.ezorm.rdb.mapping.annotation.ColumnType;
import org.hswebframework.ezorm.rdb.mapping.annotation.Comment;
import org.hswebframework.ezorm.rdb.mapping.annotation.DefaultValue;
import org.hswebframework.ezorm.rdb.mapping.annotation.JsonCodec;
import org.hswebframework.web.api.crud.entity.GenericEntity;
import org.hswebframework.web.crud.generator.Generators;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.JDBCType;
import java.util.Date;
import java.util.List;

/**
 * TODO 指定唯一索引不生效.
 * @author Pippen
 * @since 2021/03/25 10:47
 */
@Table(name = "xn_schemas", uniqueConstraints = {
        @UniqueConstraint(columnNames = "collectionName")
})
@Getter
@Setter
public class SchemasEntity extends GenericEntity<String> {

    @Column(
            length = 64,
            updatable = false
    )
    @Id
    @GeneratedValue(
            generator = Generators.MD5
    )
    @Schema(
            description = "id"
    )
    private String id;

    @Column(name = "_createTime", updatable = false)
    @DefaultValue(generator = Generators.CURRENT_TIME)
    @Schema(
            description = "创建时间(只读)"
            //, accessMode = Schema.AccessMode.READ_ONLY
    )
    private Date createTime;
    @Column(name = "_updateTime")
    @DefaultValue(generator = Generators.CURRENT_TIME)
    @Schema(
            description = "修改时间"
    )
    private Date updateTime;

    @Column(name = "collection_name", length = 256, nullable = false)
    @NotBlank(message = "[collectionName]不能为空")
    @Length(max = 256, min = 1, message = "[collectionName]长度不能大于256")
    @Comment("数据库名")
    @Schema(
            description = "数据库名"
    )
    private String collectionName;

    @Column(name = "description")
    @Comment("说明描述")
    @Schema(
            description = "说明描述"
    )
    private String description;

    @Column(name = "display_name", length = 256, nullable = false)
    @NotBlank(message = "[displayName]不能为空")
    @Length(max = 256, min = 1, message = "[displayName]长度不能大于256")
    @Comment("显示名")
    @Schema(
            description = "显示名"
    )
    private String displayName;

    @Column(name = "doc_create_time_field", length = 256, nullable = false)
    @DefaultValue("_create_time")

    private String docCreateTimeField;

    @Column(name = "doc_update_time_field", length = 256, nullable = false)
    @DefaultValue("_update_time")
    private String docUpdateTimeField;

    @JsonCodec
    @ColumnType(jdbcType = JDBCType.CLOB)
    @Column(name = "fields", nullable = false)
    @Comment("字段")
    @Schema(
            description = "字段",
            hidden = true
    )
    private List<FieldsEntity> fields;

    @JsonCodec
    @ColumnType(jdbcType = JDBCType.CLOB)
    @Column(name = "search_fields")
    @Comment("搜索字段")
    @Schema(
            description = "搜索字段",
            hidden = true
    )
    private List<FieldsEntity> searchFields;

    @Column(name = "user_id", length = 64, nullable = false)
    @Comment("用户Id")
    @Schema(
            description = "用户Id"
    )
    private String userId;

    public void of(List<FieldsEntity> defaultField) {
        this.setFields(defaultField);
        this.setDocCreateTimeField(CREATE_TIME_FIELD);
        this.setDocUpdateTimeField(UPDATE_TIME_FIELD);
    }

    public static final String CREATE_TIME_FIELD = "_create_time";
    public static final String UPDATE_TIME_FIELD = "_update_time";


}
