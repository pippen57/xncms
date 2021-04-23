package top.pippen.xncms.schema.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hswebframework.web.dict.EnumDict;

/**
 * 日期类型
 *
 * @author Pippen
 * @since 2021/03/26 16:56
 */
@AllArgsConstructor
@Getter
public enum DateFormatTypeEnum implements EnumDict<String> {
    timestamp_ms("timestamp-ms")
    ;

    private final String text;

    @Override
    public String getValue() {
        return name();
    }
}
