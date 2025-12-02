package com.example.demo.repository;

import com.example.demo.model.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Integer> {

    @Query(value = "SELECT * FROM stories ORDER BY id_story LIMIT :pageSize OFFSET (:page * :pageSize)", nativeQuery = true)
    List<Story> findAllWithPagination(@Param("page") int page, @Param("pageSize") int pageSize);
}
