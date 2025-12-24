package com.example.anime_news.pojo;

import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建实体类
 * 1.使用@Entity，标识为JPA实体类
 * 2.使用@Table，指定映射的表名
 * 3.使用@Data，扩展类的set和get方法
 * 4.使用@Id，指定映射的表中主键，使用@GeneratedValue，标识为自增主键
 */
@Entity
@Table(name = "tb_news")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键", example = "1")
    private Long id;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("来源链接")
    private String url;
    @ApiModelProperty("封面")
    private String image;
    @ApiModelProperty(value = "创建修改时间", example = "1")
    private String time;
    @ApiModelProperty("标签")
    private String tag;
    @ApiModelProperty("摘要")
    private String note;
    @ApiModelProperty("内容")
    @Lob
    private String content;
    @ApiModelProperty("访问量")
    private Integer visits;
}
