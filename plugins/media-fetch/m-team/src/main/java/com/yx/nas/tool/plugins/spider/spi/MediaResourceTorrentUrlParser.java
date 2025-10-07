package com.yx.nas.tool.plugins.spider.spi;

import com.fasterxml.jackson.databind.JsonNode;
import com.yx.framework.spider.enums.DataType;
import com.yx.framework.spider.spi.JsonBodyParser;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MediaResourceTorrentUrlParser implements JsonBodyParser<String> {
    @Override
    public ResultProperty getResultProperty() {
        return new ResultProperty("torrentUrl", DataType.SINGLE);

    }

    @Override
    public List<String> parse(JsonNode node) throws Exception {
        return List.of(node.get("data").asText());
    }

    @Override
    public boolean supports(String url, String ct) {
        return ct != null && url != null && ct.contains("application/json")
                && url.contains("https://api.m-team.cc/api/torrent/genDlToken");
    }
}
