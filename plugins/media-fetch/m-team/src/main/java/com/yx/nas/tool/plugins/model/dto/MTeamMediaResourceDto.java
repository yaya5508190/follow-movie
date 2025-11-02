package com.yx.nas.tool.plugins.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * M-Team 媒体资源信息查询返回VO
 * https://api.m-team.cc/api/torrent/search 接口返回数据结构
 */
@Data
public class MTeamMediaResourceDto {
    private String id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedDate;
    private String name;
    private String smallDescr;
    private String imdb;
    private Double imdbRating;
    private String douban;
    private Double doubanRating;
    private String dmmCode;
    private String author;
    private String category;
    private String source;
    private String medium;
    private String standard;
    private String videoCodec;
    private String audioCodec;
    private String team;
    private String processing;
    private List<String> countries;
    private String numfiles;
    private Long size;
    private String labels;
    private List<String> labelsNew;
    private String msUp;
    private Boolean anonymous;
    private String infoHash;
    private StatusVo status;
    private String dmmInfo;
    private String editedBy;
    private String editDate;
    private Boolean collection;
    private Boolean inRss;
    private Boolean canVote;
    private List<String> imageList;
    private String resetBox;

    @Data
    public static class StatusVo {
        private String id;
        private String createdDate;
        private String lastModifiedDate;
        private String pickType;
        private String toppingLevel;
        private String toppingEndTime;
        private String discount;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime discountEndTime;
        private String timesCompleted;
        private String comments;
        private String lastAction;
        private String lastSeederAction;
        private String views;
        private String hits;
        private String support;
        private String oppose;
        private String status;
        private Integer seeders;
        private Integer leechers;
        private Boolean banned;
        private Boolean visible;
        @JsonDeserialize(using = PromotionRuleDeserializer.class)
        private Object promotionRule;
        private MallSingleFreeVo mallSingleFree;
    }

    @Data
    public static class MallSingleFreeVo {
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdDate;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime lastModifiedDate;
        private String id;
        private String userid;
        private String torrent;
        private Boolean isAdult;
        private String points;
        private String freeDay;
        private String auction;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime startDate;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime endDate;
        private String status;
    }

    @Data
    public static class PromotionRuleVo {
        private String createdDate;
        private String lastModifiedDate;
        private String id;
        private List<String> categories;
        private List<String> teams;
        private String discount;
        private String startTime;
        private String endTime;
        private String operatorId;
        private String operator;
    }

    /**
     * Custom deserializer for promotionRule field to support both String and Object types
     */
    public static class PromotionRuleDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            if (node.isTextual()) {
                return node.asText();
            } else if (node.isObject()) {
                return p.getCodec().treeToValue(node, PromotionRuleVo.class);
            }
            return null;
        }
    }
}
