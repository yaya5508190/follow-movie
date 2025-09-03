package com.yx.nas.tool.plugins.spider.spi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.yx.framework.spider.enums.DataType;
import com.yx.framework.spider.model.Page;
import com.yx.framework.spider.spi.PageParser;
import com.yx.nas.tool.plugins.model.movie.Movie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


@Component
public class DouBanMovieTotalParser implements PageParser<Integer> {
    @Override
    public boolean supports(String url, String ct) {
        return ct != null && url != null && ct.contains("application/json") && url.contains("https://m.douban.com/rexxar/api/v2/subject/recent_hot/movie");
    }

    @Override
    public ResultProperty getResultProperty() {
        return new ResultProperty("total", DataType.SINGLE);
    }

    @Override
    public List<Integer> parse(Page page) throws JsonProcessingException {
        Document doc = Jsoup.parse(new String(page.body(), StandardCharsets.UTF_8), page.request().url());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(doc.body().text());
        return List.of(node.get("total").asInt());
    }
}
