package com.yx.nas.tool.plugins.spider.spi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.yx.framework.spider.enums.DataType;
import com.yx.framework.spider.spi.JsonBodyParser;
import com.yx.nas.model.dto.MovieRankDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class DouBanMovieParser implements JsonBodyParser<MovieRankDto> {
    @Override
    public boolean supports(String url, String ct) {
        return ct != null && url != null && ct.contains("application/json") && url.contains("https://m.douban.com/rexxar/api/v2/subject/recent_hot/movie");
    }

    @Override
    public ResultProperty getResultProperty() {
        return new ResultProperty("data", DataType.COLLECTION);
    }

    @Override
    public List<MovieRankDto> parse(JsonNode node) {
        List<MovieRankDto> movieRankDtos = new ArrayList<>();
        ArrayNode items =  (ArrayNode) node.get("items");
        MovieRankDto movieRankDto;
        for(JsonNode item : items) {
            movieRankDto = new MovieRankDto();
            movieRankDto.setTitle(item.get("title").asText());
            movieRankDto.setUri(item.get("uri").asText().replaceAll("douban://douban.com/movie/","https://movie.douban.com/subject/"));
            movieRankDto.setRating(item.get("rating").get("value").asDouble());
            movieRankDto.setCoverPic(item.get("pic").get("normal").asText());
            movieRankDto.setDesc(item.get("card_subtitle").asText());
            movieRankDtos.add(movieRankDto);
        }
        return movieRankDtos;
    }
}
