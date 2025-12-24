package com.example.anime_news.pojo;

import lombok.Data;

import javax.persistence.*;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@Data
@Entity
@Table(name = "music")
public class Music implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键", example = "1")
    private Long id;
    
    @ApiModelProperty(value = "视频BV号", example = "BV1822WBwEAz")
    private String bvid;
    
    @ApiModelProperty("封面地址")
    private String pic;
    
    @ApiModelProperty("标题")
    private String title;
    
    @ApiModelProperty("视频url")
    private String url;
    
    @ApiModelProperty("作者")
    private String author;

    @ApiModelProperty("播放量")
    private Integer play;
    
    @ApiModelProperty("评论数")
    private Integer review;
    
    @ApiModelProperty("收藏数")
    private Integer favorites;
    
    @ApiModelProperty("分区ID")
    private Integer cateId;
    
    @ApiModelProperty("分区名称")
    private String cateName;
}