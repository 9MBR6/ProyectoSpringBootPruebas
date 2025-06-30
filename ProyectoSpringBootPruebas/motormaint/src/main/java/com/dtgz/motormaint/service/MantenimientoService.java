package com.dtgz.motormaint.service;

/**
 * Servicio para gestionar las operaciones relacionadas con los mantenimientos.
 * Utiliza MotoService para asociar mantenimientos a motos y proporciona métodos para
 * obtener, guardar, actualizar y eliminar mantenimientos, así como consultar por moto.
 */
import com.dtgz.motormaint.model.Mantenimiento;
import com.dtgz.motormaint.model.Moto;
import com.dtgz.motormaint.model.enums.MarcaMoto;
import com.dtgz.motormaint.repository.MantenimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MantenimientoService {

    /**
     * Repositorio para acceder a los datos de Mantenimiento en la base de datos.
     */
    @Autowired
    private MantenimientoRepository mantenimientoRepository;

    /**
     * Servicio para acceder a los datos de Moto y asociar mantenimientos.
     */
    @Autowired
    private MotoService motoService;

    /**
     * Obtiene todos los mantenimientos registrados en la base de datos.
     * @return Lista de objetos Mantenimiento
     */
    public List<Mantenimiento> getAllMantenimientos() {
        return mantenimientoRepository.findAll();
    }

    /**
     * Busca un mantenimiento por su identificador único.
     * @param id Identificador del mantenimiento
     * @return Optional con el mantenimiento encontrado o vacío si no existe
     */
    public Optional<Mantenimiento> getMantenimientoById(Long id) {
        return mantenimientoRepository.findById(id);
    }

    /**
     * Guarda un nuevo mantenimiento o actualiza uno existente, asociándolo a una moto específica.
     * @param mantenimiento Objeto Mantenimiento a guardar o actualizar
     * @param motoId Identificador de la moto asociada
     * @return El mantenimiento guardado o actualizado
     * @throws RuntimeException si la moto no existe
     */
    public Mantenimiento saveMantenimiento(Mantenimiento mantenimiento, Long motoId) {
        Optional<Moto> motoOptional = motoService.getMotoById(motoId);
        if (motoOptional.isPresent()) {
            mantenimiento.setMoto(motoOptional.get());
            return mantenimientoRepository.save(mantenimiento);
        } else {
            throw new RuntimeException("Error: Moto con ID " + motoId + " no encontrada.");
        }
    }

    /**
     * Elimina un mantenimiento por su identificador.
     * @param id Identificador del mantenimiento a eliminar
     */
    public void deleteMantenimiento(Long id) {
        mantenimientoRepository.deleteById(id);
    }

    /**
     * Verifica si existe un mantenimiento con el identificador proporcionado.
     * @param id Identificador del mantenimiento
     * @return true si existe, false en caso contrario
     */
    public boolean existsMantenimientoById(Long id) {
        return mantenimientoRepository.existsById(id);
    }

    /**
     * Actualiza los datos de un mantenimiento existente, asociándolo a una moto específica.
     * @param id Identificador del mantenimiento a actualizar
     * @param mantenimientoDetails Objeto con los nuevos datos del mantenimiento
     * @param motoId Identificador de la moto asociada
     * @return Optional con el mantenimiento actualizado o vacío si no se encontró
     * @throws RuntimeException si la moto no existe
     */
    public Optional<Mantenimiento> updateMantenimiento(Long id, Mantenimiento mantenimientoDetails, Long motoId) {
        return mantenimientoRepository.findById(id)
            .flatMap(mantenimientoExistente -> {
                Optional<Moto> motoOptional = motoService.getMotoById(motoId);
                if (motoOptional.isPresent()) {
                    mantenimientoExistente.setMoto(motoOptional.get());
                    mantenimientoExistente.setTipoMantenimiento(mantenimientoDetails.getTipoMantenimiento());
                    mantenimientoExistente.setFechaMantenimiento(mantenimientoDetails.getFechaMantenimiento());
                    mantenimientoExistente.setKilometraje(mantenimientoDetails.getKilometraje());
                    mantenimientoExistente.setNotas(mantenimientoDetails.getNotas());
                    return Optional.of(mantenimientoRepository.save(mantenimientoExistente));
                } else {
                    throw new RuntimeException("Error: Moto con ID " + motoId + " no encontrada para actualizar el mantenimiento.");
                }
            });
    }

    /**
     * Obtiene todos los mantenimientos asociados a una moto específica por su ID.
     * @param motoId Identificador de la moto
     * @return Lista de mantenimientos asociados a la moto
     */
    public List<Mantenimiento> getMantenimientosByMotoId(Long motoId) {
        return mantenimientoRepository.findByMotoId(motoId);
    }

     public List<Mantenimiento> searchMantenimientos(
            Long motoId, // Null para búsqueda global
            MarcaMoto marcaMoto, // Solo para búsqueda global
            String tipoMantenimientoSearch,
            String notasSearch,
            LocalDate startDate,
            LocalDate endDate) {

        // Normalizar parámetros de búsqueda de texto
        boolean hasTipo = tipoMantenimientoSearch != null && !tipoMantenimientoSearch.isBlank();
        boolean hasNotas = notasSearch != null && !notasSearch.isBlank();
        boolean hasDateRange = startDate != null && endDate != null;

        if (motoId != null) { // Búsqueda para mantenimientos de una MOTO ESPECÍFICA
            if (hasTipo && hasNotas) {
                return mantenimientoRepository.findByMotoIdAndTipoMantenimientoContainingIgnoreCaseAndNotasContainingIgnoreCase(motoId, tipoMantenimientoSearch, notasSearch);
            } else if (hasTipo) {
                return mantenimientoRepository.findByMotoIdAndTipoMantenimientoContainingIgnoreCase(motoId, tipoMantenimientoSearch);
            } else if (hasNotas) {
                return mantenimientoRepository.findByMotoIdAndNotasContainingIgnoreCase(motoId, notasSearch);
            } else if (hasDateRange) {
                return mantenimientoRepository.findByMotoIdAndFechaMantenimientoBetween(motoId, startDate, endDate);
            } else {
                return mantenimientoRepository.findByMotoId(motoId); // Sin filtros, solo por motoId
            }
        } else { // Búsqueda GLOBAL de mantenimientos
            if (marcaMoto != null) { // Filtrando por marca de moto asociada
                if (hasTipo && hasNotas) {
                    return mantenimientoRepository.findByMoto_MarcaAndTipoMantenimientoContainingIgnoreCaseAndNotasContainingIgnoreCase(marcaMoto, tipoMantenimientoSearch, notasSearch);
                } else if (hasTipo) {
                    return mantenimientoRepository.findByMoto_MarcaAndTipoMantenimientoContainingIgnoreCase(marcaMoto, tipoMantenimientoSearch);
                } else if (hasNotas) {
                    return mantenimientoRepository.findByMoto_MarcaAndNotasContainingIgnoreCase(marcaMoto, notasSearch);
                } else if (hasDateRange) {
                    return mantenimientoRepository.findByMoto_MarcaAndFechaMantenimientoBetween(marcaMoto, startDate, endDate);
                } else {
                    return mantenimientoRepository.findByMoto_Marca(marcaMoto); // Solo por marca de moto
                }
            } else { // Búsqueda global sin filtro de marca de moto
                if (hasTipo && hasNotas) {
                    return mantenimientoRepository.findByTipoMantenimientoContainingIgnoreCaseAndNotasContainingIgnoreCase(tipoMantenimientoSearch, notasSearch);
                } else if (hasTipo) {
                    return mantenimientoRepository.findByTipoMantenimientoContainingIgnoreCase(tipoMantenimientoSearch);
                } else if (hasNotas) {
                    return mantenimientoRepository.findByNotasContainingIgnoreCase(notasSearch);
                } else if (hasDateRange) {
                    return mantenimientoRepository.findByFechaMantenimientoBetween(startDate, endDate);
                } else {
                    return mantenimientoRepository.findAll(); // Sin ningún filtro
                }
            }
        }

}
}