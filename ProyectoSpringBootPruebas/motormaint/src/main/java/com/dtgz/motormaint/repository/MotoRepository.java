package com.dtgz.motormaint.repository;

import com.dtgz.motormaint.model.Moto;
import com.dtgz.motormaint.model.enums.MarcaMoto; // Importar MarcaMoto
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // Necesario si no está ya

@Repository
public interface MotoRepository extends JpaRepository<Moto, Long> {

    // Buscar motos por marca y modelo (ignorando mayúsculas/minúsculas y buscando contenido)
    List<Moto> findByMarcaAndModeloContainingIgnoreCase(MarcaMoto marca, String modelo);

    // Buscar motos solo por modelo (ignorando mayúsculas/minúsculas y buscando contenido)
    List<Moto> findByModeloContainingIgnoreCase(String modelo);

    // Buscar motos solo por marca
    List<Moto> findByMarca(MarcaMoto marca);

    // Si necesitas un método general que pueda manejar ambos opcionales:
    // No lo usaremos directamente ahora, pero es bueno saber que se pueden construir queries más complejas.
    // @Query("SELECT m FROM Moto m WHERE (:marca IS NULL OR m.marca = :marca) AND (:modelo IS NULL OR LOWER(m.modelo) LIKE LOWER(CONCAT('%', :modelo, '%')))")
    // List<Moto> searchMotos(@Param("marca") MarcaMoto marca, @Param("modelo") String modelo);
}