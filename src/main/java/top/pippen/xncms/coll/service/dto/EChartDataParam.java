package top.pippen.xncms.coll.service.dto;

import lombok.Getter;
import lombok.Setter;
import top.pippen.xncms.coll.enums.DataRangeEnum;

import java.util.List;

/**
 * @author Pippen
 * @since 2021/04/20 16:26
 */
@Getter
@Setter
public class EChartDataParam {

    private DataBean x;
    private List<DataBean> y;
    private DataRangeEnum range = DataRangeEnum.little;


    @Getter
    @Setter
    public static class DataBean {
        private String name;
        private String fieldName;
    }

}
