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
public class MTeamMediaResourceDtoParser implements JsonBodyParser<MediaResourceDto> {
    @Override
    public ResultProperty getResultProperty() {
        return new ResultProperty("data", DataType.COLLECTION);
    }

    @Override
    public List<MediaResourceDto> parse(JsonNode node) throws Exception {
        // 直接将 JSON 转换为 MTeamMediaResourceDto 使用jackson转换
        List<MediaResourceDto> mediaResources = new ArrayList<>();
        ArrayNode data = (ArrayNode) node.get("data").get("data");
        MTeamMediaResourceDto mTeamMediaResourceDto;
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        for (JsonNode item : data) {
            mTeamMediaResourceDto = mapper.treeToValue(item, MTeamMediaResourceDto.class);
            mediaResources.add(convertToMediaResourceDto(mTeamMediaResourceDto));
        }
        return mediaResources;
    }

    @Override
    public boolean supports(String url, String ct) {
        return ct != null && url != null && ct.contains("application/json")
                && url.contains("https://api.m-team.cc/api/torrent/search");
    }

    private MediaResourceDto convertToMediaResourceDto(MTeamMediaResourceDto mTeamMediaResourceDto) {
        MediaResourceDto mediaResourceDto = new MediaResourceDto();
        mediaResourceDto.setId(mTeamMediaResourceDto.getId());
        mediaResourceDto.setName(mTeamMediaResourceDto.getName());
        mediaResourceDto.setDesc(mTeamMediaResourceDto.getSmallDescr());
        mediaResourceDto.setImages(mTeamMediaResourceDto.getImageList());
        mediaResourceDto.setDiscount(mTeamMediaResourceDto.getStatus().getDiscount());
        mediaResourceDto.setDiscountEndTime(mTeamMediaResourceDto.getStatus().getDiscountEndTime());
        mediaResourceDto.setImdb(mTeamMediaResourceDto.getImdb());
        mediaResourceDto.setImdbRating(mTeamMediaResourceDto.getImdbRating());
        mediaResourceDto.setDouban(mTeamMediaResourceDto.getDouban());
        mediaResourceDto.setDoubanRating(mTeamMediaResourceDto.getDoubanRating());
        mediaResourceDto.setSize(mTeamMediaResourceDto.getSize());
        mediaResourceDto.setCreatedDate(mTeamMediaResourceDto.getCreatedDate());
        mediaResourceDto.setSeeders(mTeamMediaResourceDto.getStatus().getSeeders());
        mediaResourceDto.setLeechers(mTeamMediaResourceDto.getStatus().getLeechers());
        mediaResourceDto.setLabels(mTeamMediaResourceDto.getLabelsNew());
        return mediaResourceDto;
    }
}
