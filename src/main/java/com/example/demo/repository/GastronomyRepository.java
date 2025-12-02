package com.example.demo.repository;

import com.example.demo.model.Gastronomy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GastronomyRepository extends JpaRepository<Gastronomy, Integer> {

    //Obtiene todos los platillos asociados a un lugar turístico con paginación.
@Query(value = "SELECT * FROM gastronomies ORDER BY id_dish LIMIT :pageSize OFFSET (:page * :pageSize)", nativeQuery = true)
List<Gastronomy> findAllWithPagination(@Param("page") int page, @Param("pageSize") int pageSize);

}
