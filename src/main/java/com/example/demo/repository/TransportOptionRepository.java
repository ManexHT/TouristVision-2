package com.example.demo.repository;

import com.example.demo.model.TransportOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransportOptionRepository extends JpaRepository<TransportOption, Integer> {

    @Query(value = "SELECT * FROM transportoptions ORDER BY id_transport LIMIT :pageSize OFFSET (:page * :pageSize)", nativeQuery = true)
    List<TransportOption> findAllWithPagination(@Param("page") int page, @Param("pageSize") int pageSize);

    @Query(value = "SELECT * FROM transportoptions WHERE LOWER(type) LIKE LOWER(CONCAT('%', :type, '%')) ORDER BY id_transport", nativeQuery = true)
    List<TransportOption> findByType(@Param("type") String type);
}
