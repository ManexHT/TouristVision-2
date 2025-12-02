package com.example.demo.repository;

import com.example.demo.model.Rol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RolRepository extends JpaRepository<Rol, Integer> {

    @Query(value = "SELECT * FROM roles WHERE name ILIKE CONCAT('%', :name, '%')", nativeQuery = true)
    List<Rol> findByNameLike(@Param("name") String name);

}
