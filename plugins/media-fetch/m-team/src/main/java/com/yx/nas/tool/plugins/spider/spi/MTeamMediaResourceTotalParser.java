package com.yx.nas.tool.plugins.spider.spi;

import com.fasterxml.jackson.databind.JsonNode;
import com.yx.framework.spider.enums.DataType;
import com.yx.framework.spider.spi.JsonBodyParser;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MTeamMediaResourceTotalParser implements JsonBodyParser<Long> {
    @Override
    public ResultProperty getResultProperty() {
        return new ResultProperty("total", DataType.SINGLE);
    }

    @Override
    public List<Long> parse(JsonNode node) throws Exception {
        return List.of(node.get("data").get("total").asLong());
    }

    @Override
    public boolean supports(String url, String ct) {
        return ct != null && url != null && ct.contains("application/json")
                && url.contains("https://api.m-team.cc/api/torrent/search");
    }
}
