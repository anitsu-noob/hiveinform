package com.example.hiveinform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Map;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInform {
    @Id
    private String id ;
    private long userId ;
    private String userName ;
    @Field("article_ids")
    private List<String> ArticleId ;
    private String imageId ;
    private Map<String,Integer> groupReadLine ;
    private List<String> groupJoined ;
    private List<String> groupVisited ;
    // 阅文记录 阅读信息
    // 现在还不知道要写什么 先放在这哈 以后慢慢加 这个不写死了 总之 dto 和 entity 的关系就是 entity -> dto -> 前端

}
 // 用户信息 只能查询 和 添加 作为 法律相关的要求 非关键信息
 // 的存储都放在 inform 当中 作用为 记录 在本应用当中的行为和 个性化非关键信息