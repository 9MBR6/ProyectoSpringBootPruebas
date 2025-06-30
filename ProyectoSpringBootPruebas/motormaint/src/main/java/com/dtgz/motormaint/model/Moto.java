package com.dtgz.motormaint.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString; // Importar ToString

import java.util.List;

import com.dtgz.motormaint.model.enums.MarcaMoto;
import com.dtgz.motormaint.model.enums.TipoMotor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "motos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Moto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La marca no puede estar vacía.")
    private MarcaMoto marca;

    @NotBlank(message = "El modelo no puede estar vacío.")
    @Size(min = 2, max = 50, message = "El modelo debe tener entre 2 y 50 caracteres.")
    private String modelo;

    @Min(value = 1900, message = "El año de fabricación debe ser al menos 1900.")
    @Max(value = 2100, message = "El año de fabricación no puede ser futuro.")
    private int anoFabricacion;

    @Size(max = 10, message = "La matrícula no puede exceder los 10 caracteres.")
    private String matricula;

    @Size(max = 20, message = "El número de bastidor no puede exceder los 20 caracteres.")
    private String numeroBastidor;

    @Min(value = 50, message = "La cilindrada debe ser al menos 50cc.")
    private int cilindradaCc;

    @NotNull(message = "El tipo de motor no puede estar vacío.")
    private TipoMotor tipoMotor;

    @OneToMany(mappedBy = "moto", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude // <--- ¡Añade esto! Excluye la lista de mantenimientos del toString de Moto
    private List<Mantenimiento> mantenimientos;
}