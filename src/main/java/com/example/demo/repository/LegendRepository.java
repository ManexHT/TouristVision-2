package com.example.demo.repository;

import com.example.demo.model.Legend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LegendRepository extends JpaRepository<Legend, Integer> {


    @Query(value = "SELECT * FROM legends ORDER BY id_legend LIMIT :pageSize OFFSET (:page * :pageSize)", nativeQuery = true)
    List<Legend> findAllWithPagination(@Param("page") int page, @Param("pageSize") int pageSize);
}
