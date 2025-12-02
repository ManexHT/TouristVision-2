package com.example.demo.repository;

import com.example.demo.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

    // Buscar usuarios por nombre de usuario parcial (case-insensitive)
    @Query(value = "SELECT * FROM appusers WHERE username ILIKE CONCAT('%', :username, '%')", nativeQuery = true)
    List<AppUser> findByUsernameLike(@Param("username") String username);

    // Paginación
    @Query(value = "SELECT * FROM appusers ORDER BY id_user LIMIT :pageSize OFFSET (:page * :pageSize)", nativeQuery = true)
    List<AppUser> findAllWithPagination(@Param("page") int page, @Param("pageSize") int pageSize);

    Optional<AppUser> findByUsername(@Param("username") String username);

}
