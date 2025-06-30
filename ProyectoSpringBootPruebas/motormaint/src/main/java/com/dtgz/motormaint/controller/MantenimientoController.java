package com.dtgz.motormaint.controller;

import com.dtgz.motormaint.model.Mantenimiento;
import com.dtgz.motormaint.model.Moto;
import com.dtgz.motormaint.model.enums.MarcaMoto;
import com.dtgz.motormaint.service.MantenimientoService;
import com.dtgz.motormaint.service.MotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/mantenimientos")
public class MantenimientoController {

    @Autowired
    private MantenimientoService mantenimientoService;

    @Autowired
    private MotoService motoService;

    // Muestra la lista GLOBAL de todos los mantenimientos
 @GetMapping
    public String listAllMantenimientos(
            @RequestParam(value = "marcaMoto", required = false) MarcaMoto marcaMoto, // Filtro por marca de moto
            @RequestParam(value = "tipoMantenimientoSearch", required = false) String tipoMantenimientoSearch, // Búsqueda por tipo
            @RequestParam(value = "notasSearch", required = false) String notasSearch, // Búsqueda por notas
            @RequestParam(value = "startDate", required = false) LocalDate startDate, // Filtro por fecha inicio
            @RequestParam(value = "endDate", required = false) LocalDate endDate, // Filtro por fecha fin
            Model model) {

        List<Mantenimiento> mantenimientos;
        mantenimientos = mantenimientoService.searchMantenimientos(
            null, // motoId es null para búsqueda global
            marcaMoto,
            tipoMantenimientoSearch,
            notasSearch,
            startDate,
            endDate
        );

        model.addAttribute("mantenimientos", mantenimientos);
        model.addAttribute("allMarcas", MarcaMoto.values()); // Para el filtro de marca de moto
        model.addAttribute("selectedMarcaMoto", marcaMoto); // Para mantener el valor seleccionado
        model.addAttribute("tipoMantenimientoSearch", tipoMantenimientoSearch); // Para mantener el valor
        model.addAttribute("notasSearch", notasSearch); // Para mantener el valor
        model.addAttribute("startDate", startDate); // Para mantener el valor
        model.addAttribute("endDate", endDate); // Para mantener el valor

        return "mantenimientos/list";
    }

    // Muestra el formulario para añadir un nuevo mantenimiento
    @GetMapping("/new")
    public String showAddForm(@RequestParam(value = "motoId", required = false) Long motoId, Model model) {
        Mantenimiento mantenimiento = new Mantenimiento();
        if (motoId != null) {
            Optional<Moto> moto = motoService.getMotoById(motoId);
            moto.ifPresent(mantenimiento::setMoto);
        }
        model.addAttribute("mantenimiento", mantenimiento);
        List<Moto> motos = motoService.getAllMotos();
        model.addAttribute("motos", motos);
        return "mantenimientos/form"; // Devuelve directamente mantenimientos/form.html
    }

    @PostMapping
    public String saveOrUpdateMantenimiento(@ModelAttribute Mantenimiento mantenimiento,
                                            @RequestParam("motoId") Long motoId,
                                            RedirectAttributes redirectAttributes) {
        Long redirectMotoId = null;
        try {
            if (mantenimiento.getId() != null) {
                Optional<Mantenimiento> existingMaint = mantenimientoService.getMantenimientoById(mantenimiento.getId());
                if (existingMaint.isPresent()) {
                    redirectMotoId = existingMaint.get().getMoto().getId();
                }
            }

            Mantenimiento savedMaint = mantenimientoService.saveMantenimiento(mantenimiento, motoId);
            if (redirectMotoId == null && savedMaint.getMoto() != null) {
                redirectMotoId = savedMaint.getMoto().getId();
            }

            redirectAttributes.addFlashAttribute("message", "Mantenimiento guardado correctamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        if (redirectMotoId != null) {
            return "redirect:/motos/" + redirectMotoId + "/mantenimientos";
        }
        return "redirect:/mantenimientos";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Mantenimiento> mantenimientoOptional = mantenimientoService.getMantenimientoById(id);
        if (mantenimientoOptional.isPresent()) {
            model.addAttribute("mantenimiento", mantenimientoOptional.get());
            List<Moto> motos = motoService.getAllMotos();
            model.addAttribute("motos", motos);
            return "mantenimientos/form"; // Devuelve directamente mantenimientos/form.html
        } else {
            redirectAttributes.addFlashAttribute("error", "Mantenimiento no encontrado para editar.");
            return "redirect:/mantenimientos";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteMantenimiento(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Long redirectMotoId = null;
        Optional<Mantenimiento> mantenimientoToDelete = mantenimientoService.getMantenimientoById(id);
        if (mantenimientoToDelete.isPresent()) {
            redirectMotoId = mantenimientoToDelete.get().getMoto().getId();
            mantenimientoService.deleteMantenimiento(id);
            redirectAttributes.addFlashAttribute("message", "Mantenimiento eliminado correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Mantenimiento no encontrado para eliminar.");
        }

        if (redirectMotoId != null) {
            return "redirect:/motos/" + redirectMotoId + "/mantenimientos";
        }
        return "redirect:/mantenimientos";
    }
}