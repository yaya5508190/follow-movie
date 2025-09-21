package com.yx.nas.tool.plugins.spider.spi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yx.framework.spider.enums.DataType;
import com.yx.framework.spider.spi.JsonBodyParser;
import com.yx.nas.model.dto.MediaResourceDto;
import com.yx.nas.tool.plugins.model.dto.MTeamMediaResourceDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MTeamMediaResourceTotalParser implements JsonBodyParser<Integer> {
    @Override
    public ResultProperty getResultProperty() {
        return new ResultProperty("total", DataType.SINGLE);
    }

    @Override
    public List<Integer> parse(JsonNode node) throws Exception {
        return List.of(node.get("data").get("total").asInt());
    }

    @Override
    public boolean supports(String url, String ct) {
        return ct != null && url != null && ct.contains("application/json")
                && url.contains("https://api.m-team.cc/api/torrent/search");
    }
}
