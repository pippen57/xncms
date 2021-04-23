package top.pippen.xncms.coll.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pippen
 * @since 2021/04/20 16:58
 */
@Getter
@Setter
public class EChartDataRDto {

    private List<Object> xData = new ArrayList<>();
    private String titleName;
    private List<Series> yData = new ArrayList<>();



    @Getter
    @Setter
    public static class Series {
        private String name;
        private String type = "line";
        private List<Object> data = new ArrayList<>();

    }

}
