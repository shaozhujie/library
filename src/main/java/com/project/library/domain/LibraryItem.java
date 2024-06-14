package com.project.library.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 图书
 * @date 2024/05/29 10:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("library_item")
public class LibraryItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * ISBN编码
     */
    private String isbn;

    /**
     * 作者
     */
    private String author;

    /**
     * 类别
     */
    private String type;

    /**
     * 出版社
     */
    private String press;

    /**
     * 语种
     */
    private String language;

    /**
     * 存放书架
     */
    private String shelf;

    /**
     * 存放排数
     */
    private String row;

    /**
     * 译者
     */
    private String translate;

    /**
     * 版次
     */
    private String version;

    /**
     * 价格
     */
    private Float price;

    /**
     * 页数
     */
    private Integer page;

    /**
     * 字数
     */
    private Integer font;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 馆内余量
     */
    private Integer surplus;

    /**
     * 图片
     */
    private String image;

    /**
     * 创建者
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新者
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 简介
     */
    private String remark;

    @TableField(exist = false)
    private Integer pageNumber;

    @TableField(exist = false)
    private Integer pageSize;
}
