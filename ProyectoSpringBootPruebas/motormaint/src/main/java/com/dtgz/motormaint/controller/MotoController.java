package com.dtgz.motormaint.controller;

import com.dtgz.motormaint.model.Mantenimiento;
import com.dtgz.motormaint.model.Moto;
import com.dtgz.motormaint.model.enums.MarcaMoto;
import com.dtgz.motormaint.service.MotoService;
import com.dtgz.motormaint.service.MantenimientoService; // Necesario si sigues inyectando ambos

import jakarta.validation.Valid; // Importar la anotación @Valid

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; // Importar BindingResult
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/motos")
public class MotoController {

    @Autowired
    private MotoService motoService;

    @Autowired
    private MantenimientoService mantenimientoService; // Mantener si necesario para otros métodos

     @GetMapping
    public String listMotos(@RequestParam(value = "marca", required = false) MarcaMoto marca, // Parámetro para filtrar por Marca
                            @RequestParam(value = "modeloSearch", required = false) String modeloSearch, // Parámetro para buscar por Modelo
                            Model model) {

        List<Moto> motos;
        // Llama al nuevo método searchMotos del servicio
        motos = motoService.searchMotos(marca, modeloSearch);

        model.addAttribute("motos", motos); // Pasa la lista de motos (filtrada o completa)
        model.addAttribute("allMarcas", MarcaMoto.values()); // Pasa todas las marcas para el selector de filtro
        model.addAttribute("selectedMarca", marca); // Pasa la marca seleccionada para mantener el filtro
        model.addAttribute("modeloSearch", modeloSearch); // Pasa el término de búsqueda para mantenerlo en el campo

        return "motos/list";
    }

    @GetMapping("/new")
    public String showAddMotoForm(Model model) {
        model.addAttribute("moto", new Moto());
        // Quitar la parte de layout/title/content si no usas layout
        return "motos/form";
    }

    // --- MÉTODO POST MODIFICADO PARA VALIDACIÓN ---
    @PostMapping
    public String saveMoto(@Valid @ModelAttribute Moto moto, // Añadir @Valid
                           BindingResult bindingResult,      // Añadir BindingResult
                           RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) { // Si hay errores de validación
            // Vuelve a la vista del formulario para mostrar los errores
            // No redirigir, solo retornar el nombre de la vista
            // Quitar la parte de layout/title/content si no usas layout
            return "motos/form";
        }

        // Si la validación pasa
        motoService.saveMoto(moto);
        redirectAttributes.addFlashAttribute("message", "Moto guardada correctamente.");
        return "redirect:/motos";
    }

    @GetMapping("/edit/{id}")
    public String showEditMotoForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Moto> moto = motoService.getMotoById(id);
        if (moto.isPresent()) {
            model.addAttribute("moto", moto.get());
            // Quitar la parte de layout/title/content si no usas layout
            return "motos/form";
        } else {
            redirectAttributes.addFlashAttribute("error", "Moto no encontrada para editar.");
            return "redirect:/motos";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteMoto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (motoService.existsMotoById(id)) {
            motoService.deleteMoto(id);
            redirectAttributes.addFlashAttribute("message", "Moto eliminada correctamente (y sus mantenimientos asociados).");
        } else {
            redirectAttributes.addFlashAttribute("error", "Moto no encontrada para eliminar.");
        }
        return "redirect:/motos";
    }

    @GetMapping("/{motoId}/mantenimientos")
    public String listMantenimientosByMoto(
            @PathVariable Long motoId,
            @RequestParam(value = "tipoMantenimientoSearch", required = false) String tipoMantenimientoSearch,
            @RequestParam(value = "notasSearch", required = false) String notasSearch,
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate,
            Model model, RedirectAttributes redirectAttributes) {

        Optional<Moto> moto = motoService.getMotoById(motoId);
        if (moto.isPresent()) {
            List<Mantenimiento> mantenimientos = mantenimientoService.searchMantenimientos(
                motoId, // Pasa el ID de la moto
                null, // Marca es null para búsqueda por moto específica
                tipoMantenimientoSearch,
                notasSearch,
                startDate,
                endDate
            );

            model.addAttribute("moto", moto.get());
            model.addAttribute("mantenimientos", mantenimientos);
            // Pasar los parámetros de búsqueda de vuelta al modelo para mantener los valores en el formulario
            model.addAttribute("tipoMantenimientoSearch", tipoMantenimientoSearch);
            model.addAttribute("notasSearch", notasSearch);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);

            return "motos/mantenimientos-por-moto";
        } else {
            redirectAttributes.addFlashAttribute("error", "Moto no encontrada.");
            return "redirect:/motos";
        }
    }
}