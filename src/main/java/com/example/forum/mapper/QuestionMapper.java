package com.example.forum.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import com.example.forum.model.Question;

@Mapper
public interface QuestionMapper {

    @Insert("INSERT INTO question (title, description, tag, creator, gmt_create, gmt_modified) VALUES (#{title}, #{description}, #{tag}, #{creator}, #{gmtCreate}, #{gmtModified}))")
    void create(Question Question);
}
