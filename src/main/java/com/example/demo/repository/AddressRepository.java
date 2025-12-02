package com.example.demo.repository;

import com.example.demo.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {

    // Buscar por código postal exacto
    @Query(value = "SELECT * FROM addresses WHERE postalCode = :postalCode", nativeQuery = true)
    List<Address> findByPostalCode(@Param("postalCode") String postalCode);

    // Buscar por colonia (neighborhood) parcial
    @Query(value = "SELECT * FROM addresses WHERE neighborhood ILIKE CONCAT('%', :neighborhood, '%')", nativeQuery = true)
    List<Address> findByNeighborhoodLike(@Param("neighborhood") String neighborhood);

    // Paginación
    @Query(value = "SELECT * FROM addresses ORDER BY id_address LIMIT :pageSize OFFSET GREATEST((:page - 1) * :pageSize, 0)", nativeQuery = true)
    List<Address> findAllWithPagination(@Param("page") int page, @Param("pageSize") int pageSize);

    // Buscar direcciones por nombre parcial de calle (case-insensitive)
    @Query(value = "SELECT * FROM addresses WHERE street ILIKE CONCAT('%', :street, '%')", nativeQuery = true)
    List<Address> findByStreetLike(@Param("street") String street);

}
