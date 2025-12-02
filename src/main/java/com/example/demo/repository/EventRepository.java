package com.example.demo.repository;

import com.example.demo.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {

    //Obtiene eventos asociados a un lugar turístico con paginación.
    @Query(value = "SELECT * FROM events ORDER BY eventDate ASC LIMIT :pageSize OFFSET (:page * :pageSize)", nativeQuery = true)
    List<Event> findAllWithPagination(@Param("page") int page, @Param("pageSize") int pageSize);

}
