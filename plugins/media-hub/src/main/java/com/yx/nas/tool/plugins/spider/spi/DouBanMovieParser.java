package com.yx.nas.tool.plugins.spider.spi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.yx.framework.spider.enums.DataType;
import com.yx.framework.spider.spi.JsonBodyParser;
import com.yx.nas.tool.plugins.model.movie.Movie;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class DouBanMovieParser implements JsonBodyParser<Movie> {
    @Override
    public boolean supports(String url, String ct) {
        return ct != null && url != null && ct.contains("application/json") && url.contains("https://m.douban.com/rexxar/api/v2/subject/recent_hot/movie");
    }

    @Override
    public ResultProperty getResultProperty() {
        return new ResultProperty("data", DataType.COLLECTION);
    }

    @Override
    public List<Movie> parse(JsonNode node) {
        List<Movie> movies = new ArrayList<>();
        ArrayNode items =  (ArrayNode) node.get("items");
        Movie movie;
        for(JsonNode item : items) {
            movie = new Movie();
            movie.setTitle(item.get("title").asText());
            movie.setUri(item.get("uri").asText());
            movie.setRating(item.get("rating").get("value").asDouble());
            movie.setCoverPic(item.get("pic").get("normal").asText());
            movie.setDesc(item.get("card_subtitle").asText());
            movies.add(movie);
        }
        return movies;
    }
}
