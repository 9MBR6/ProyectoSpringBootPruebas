package com.dtgz.motormaint.model;

import jakarta.persistence.*;
import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString; // Importar ToString

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.PastOrPresent;

@Entity
@Table(name = "mantenimientos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El tipo de mantenimiento no puede estar vacío.")
    @Size(min = 3, max = 100, message = "El tipo de mantenimiento debe tener entre 3 y 100 caracteres.")
    private String tipoMantenimiento;

    @NotNull(message = "La fecha de mantenimiento no puede estar vacía.")
    private LocalDate fechaMantenimiento;

    @Min(value = 0, message = "El kilometraje no puede ser negativo.")
    private int kilometraje;

    @Size(max = 500, message = "Las notas no pueden exceder los 500 caracteres.")
    private String notas;

    @ManyToOne
    @JoinColumn(name = "moto_id", nullable = false)
    @NotNull(message = "Debe seleccionar una moto.")
    @ToString.Exclude // <--- ¡Añade esto! Excluye la referencia a Moto del toString de Mantenimiento
    private Moto moto;
}