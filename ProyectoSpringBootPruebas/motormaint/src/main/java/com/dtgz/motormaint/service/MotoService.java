package com.dtgz.motormaint.service;

/**
 * Servicio para gestionar las operaciones relacionadas con las motos.
 * Proporciona métodos para obtener, guardar, actualizar y eliminar motos,
 * así como verificar su existencia.
 */
import com.dtgz.motormaint.model.Moto;
import com.dtgz.motormaint.model.enums.MarcaMoto;
import com.dtgz.motormaint.repository.MotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MotoService {

    /**
     * Repositorio para acceder a los datos de Moto en la base de datos.
     */
    @Autowired
    private MotoRepository motoRepository;

    /**
     * Obtiene todas las motos registradas en la base de datos.
     * @return Lista de objetos Moto
     */
    public List<Moto> getAllMotos() {
        return motoRepository.findAll();
    }

    /**
     * Busca una moto por su identificador único.
     * @param id Identificador de la moto
     * @return Optional con la moto encontrada o vacío si no existe
     */
    public Optional<Moto> getMotoById(Long id) {
        return motoRepository.findById(id);
    }

    /**
     * Guarda una nueva moto o actualiza una existente en la base de datos.
     * @param moto Objeto Moto a guardar o actualizar
     * @return La moto guardada o actualizada
     */
    public Moto saveMoto(Moto moto) {
        return motoRepository.save(moto);
    }

    /**
     * Elimina una moto por su identificador.
     * Al eliminar una moto, todos sus mantenimientos asociados también serán eliminados automáticamente
     * debido a la configuración de la relación en la entidad Moto.
     * @param id Identificador de la moto a eliminar
     */
    public void deleteMoto(Long id) {
        motoRepository.deleteById(id);
    }

    /**
     * Verifica si existe una moto con el identificador proporcionado.
     * @param id Identificador de la moto
     * @return true si existe, false en caso contrario
     */
    public boolean existsMotoById(Long id) {
        return motoRepository.existsById(id);
    }

        public List<Moto> searchMotos(MarcaMoto marca, String modeloSearch) {
        if (marca != null && modeloSearch != null && !modeloSearch.isBlank()) {
            // Si se proporcionan ambos (marca y modelo), usa la búsqueda combinada
            return motoRepository.findByMarcaAndModeloContainingIgnoreCase(marca, modeloSearch);
        } else if (modeloSearch != null && !modeloSearch.isBlank()) {
            // Si solo se proporciona el modelo, busca solo por modelo
            return motoRepository.findByModeloContainingIgnoreCase(modeloSearch);
        } else if (marca != null) {
            // Si solo se proporciona la marca, busca solo por marca
            return motoRepository.findByMarca(marca);
        } else {
            // Si no se proporciona ningún criterio, devuelve todas las motos
            return motoRepository.findAll();
        }
    }
}