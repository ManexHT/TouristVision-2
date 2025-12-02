package com.example.demo.repository;

import com.example.demo.model.State;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface StateRepository extends JpaRepository<State, Integer> {

    // state for name
    @Query(value = "SELECT * FROM states WHERE name ILIKE CONCAT('%', :name, '%')", nativeQuery = true)
    List<State> findByNameLike(@Param("name") String name);

    // pagination
    @Query(value = "SELECT * FROM states ORDER BY id_state LIMIT :pageSize OFFSET (:page * :pageSize)", nativeQuery = true)
    List<State> findAllWithPagination(@Param("page") int page, @Param("pageSize") int pageSize);
}
