package com.example.anime_news.pojo;

import lombok.Data;

import javax.persistence.*;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@Data
@Entity
@Table(name = "anime")
public class Anime implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键", example = "1")
    private Long id;
    
    @ApiModelProperty(value = "番剧ID", example = "100000")
    private Long seasonId;
    
    @ApiModelProperty("封面地址")
    private String cover;
    
    @ApiModelProperty("标题")
    private String title;
    
    @ApiModelProperty("番剧url")
    private String url;
    
    @ApiModelProperty("更新剧集")
    private String pubIndex;

    @ApiModelProperty("更新星期")
    private Integer week;
    
    @ApiModelProperty("更新日期")
    private String date;
    
    @ApiModelProperty("更新时间")
    private String pubTime;
    
    @ApiModelProperty("是否今日更新")
    private Integer isToday;
}