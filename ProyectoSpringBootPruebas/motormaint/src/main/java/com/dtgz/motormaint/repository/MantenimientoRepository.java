package com.dtgz.motormaint.repository;

import com.dtgz.motormaint.model.Mantenimiento;
import com.dtgz.motormaint.model.enums.MarcaMoto; // Importar MarcaMoto
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate; // Importar LocalDate
import java.util.List;

@Repository
public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Long> {

    // Buscar mantenimientos por tipo y notas (para lista global o por moto)
    List<Mantenimiento> findByTipoMantenimientoContainingIgnoreCaseAndNotasContainingIgnoreCase(String tipoMantenimiento, String notas);

    // Buscar mantenimientos solo por tipo (para lista global o por moto)
    List<Mantenimiento> findByTipoMantenimientoContainingIgnoreCase(String tipoMantenimiento);

    // Buscar mantenimientos solo por notas (para lista global o por moto)
    List<Mantenimiento> findByNotasContainingIgnoreCase(String notas);

    // Buscar mantenimientos por rango de fechas (para lista global o por moto)
    List<Mantenimiento> findByFechaMantenimientoBetween(LocalDate startDate, LocalDate endDate);

    // Buscar mantenimientos para una moto específica (ya existía)
    List<Mantenimiento> findByMotoId(Long motoId);

    // --- Nuevos métodos combinados para búsquedas más específicas ---

    // Buscar por motoId y tipo/notas
    List<Mantenimiento> findByMotoIdAndTipoMantenimientoContainingIgnoreCaseAndNotasContainingIgnoreCase(Long motoId, String tipoMantenimiento, String notas);
    List<Mantenimiento> findByMotoIdAndTipoMantenimientoContainingIgnoreCase(Long motoId, String tipoMantenimiento);
    List<Mantenimiento> findByMotoIdAndNotasContainingIgnoreCase(Long motoId, String notas);

    // Buscar por motoId y rango de fechas
    List<Mantenimiento> findByMotoIdAndFechaMantenimientoBetween(Long motoId, LocalDate startDate, LocalDate endDate);

    // Para la lista global: Filtrar por marca de moto asociada
    List<Mantenimiento> findByMoto_Marca(MarcaMoto marca);

    // Combinando marca de moto, tipo y notas (para lista global)
    List<Mantenimiento> findByMoto_MarcaAndTipoMantenimientoContainingIgnoreCaseAndNotasContainingIgnoreCase(MarcaMoto marca, String tipoMantenimiento, String notas);
    List<Mantenimiento> findByMoto_MarcaAndTipoMantenimientoContainingIgnoreCase(MarcaMoto marca, String tipoMantenimiento);
    List<Mantenimiento> findByMoto_MarcaAndNotasContainingIgnoreCase(MarcaMoto marca, String notas);

    // Combinando marca de moto y rango de fechas (para lista global)
    List<Mantenimiento> findByMoto_MarcaAndFechaMantenimientoBetween(MarcaMoto marca, LocalDate startDate, LocalDate endDate);
}