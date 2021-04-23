package top.pippen.xncms.schema.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字段类型枚举
 *
 * @author Pippen
 * @since 2021/03/26 17:24
 */
@AllArgsConstructor
@Getter
public enum FieldsTypeEnum {
    string("string", "单行字符串"),
    multiLineString("multiLineString", "多行字符串"),
    dateTime("dateTime", "日期时间"),
    time("time", "时间"),
    number("number", "数字"),
    markdown("markdown", "Markdown"),
    richText("richText", "富文本"),
    radioSelect("radioSelect", "单选"),
    date("date", "日期");

    private final String name;
    private final String description;


}
