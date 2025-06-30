package com.dtgz.motormaint.config;

import com.dtgz.motormaint.model.Mantenimiento;
import com.dtgz.motormaint.model.Moto;
import com.dtgz.motormaint.model.enums.MarcaMoto;
import com.dtgz.motormaint.model.enums.TipoMotor;
import com.dtgz.motormaint.service.MantenimientoService;
import com.dtgz.motormaint.service.MotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component // Indica a Spring que esta clase es un componente y debe ser gestionada por el contexto
public class DataLoader implements CommandLineRunner {

    private final MotoService motoService;
    private final MantenimientoService mantenimientoService;

    @Autowired // Inyecta los servicios necesarios
    public DataLoader(MotoService motoService, MantenimientoService mantenimientoService) {
        this.motoService = motoService;
        this.mantenimientoService = mantenimientoService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Este método se ejecutará una vez que la aplicación Spring Boot haya arrancado

        System.out.println("Cargando datos de ejemplo...");

        // 1. Crear Motos de Ejemplo
        Moto moto1 = new Moto();
        moto1.setMarca(MarcaMoto.HONDA);
        moto1.setModelo("CBR600RR");
        moto1.setAnoFabricacion(2020);
        moto1.setMatricula("1234ABC");
        moto1.setNumeroBastidor("JH2PC400MK100001");
        moto1.setCilindradaCc(599);
        moto1.setTipoMotor(TipoMotor.CUATRO_TIEMPOS);
        moto1 = motoService.saveMoto(moto1); // Guardar la moto y obtener la instancia gestionada con ID

        Moto moto2 = new Moto();
        moto2.setMarca(MarcaMoto.YAMAHA);
        moto2.setModelo("MT-07");
        moto2.setAnoFabricacion(2022);
        moto2.setMatricula("5678DEF");
        moto2.setNumeroBastidor("JRN078B5N0000002");
        moto2.setCilindradaCc(689);
        moto2.setTipoMotor(TipoMotor.CUATRO_TIEMPOS);
        moto2 = motoService.saveMoto(moto2);

        Moto moto3 = new Moto();
        moto3.setMarca(MarcaMoto.DUCATI);
        moto3.setModelo("Monster 937");
        moto3.setAnoFabricacion(2023);
        moto3.setMatricula("9012GHI");
        moto3.setNumeroBastidor("ZDMW1A00PNB000003");
        moto3.setCilindradaCc(937);
        moto3.setTipoMotor(TipoMotor.CUATRO_TIEMPOS);
        moto3 = motoService.saveMoto(moto3);

        Moto moto4 = new Moto();
        moto4.setMarca(MarcaMoto.VESPA);
        moto4.setModelo("Primavera 125");
        moto4.setAnoFabricacion(2021);
        moto4.setMatricula("PQR345");
        moto4.setNumeroBastidor("VSS2T000000004");
        moto4.setCilindradaCc(125);
        moto4.setTipoMotor(TipoMotor.CUATRO_TIEMPOS);
        moto4 = motoService.saveMoto(moto4);

        System.out.println("Motos de ejemplo creadas.");

        // 2. Crear Mantenimientos de Ejemplo y asociarlos a las motos
        Mantenimiento maint1 = new Mantenimiento();
        maint1.setTipoMantenimiento("Cambio de Aceite y Filtro");
        maint1.setFechaMantenimiento(LocalDate.of(2024, 6, 15));
        maint1.setKilometraje(25000);
        maint1.setNotas("Aceite Motul 7100, filtro K&N. Todo correcto.");
        mantenimientoService.saveMantenimiento(maint1, moto1.getId()); // Asociar con moto1

        Mantenimiento maint2 = new Mantenimiento();
        maint2.setTipoMantenimiento("Revisión de Frenos");
        maint2.setFechaMantenimiento(LocalDate.of(2024, 3, 10));
        maint2.setKilometraje(22000);
        maint2.setNotas("Pastillas delanteras cambiadas, líquido purgado.");
        mantenimientoService.saveMantenimiento(maint2, moto1.getId()); // Asociar con moto1

        Mantenimiento maint3 = new Mantenimiento();
        maint3.setTipoMantenimiento("Cambio de Neumáticos");
        maint3.setFechaMantenimiento(LocalDate.of(2024, 5, 20));
        maint3.setKilometraje(18000);
        maint3.setNotas("Neumáticos Pirelli Diablo Rosso IV.");
        mantenimientoService.saveMantenimiento(maint3, moto2.getId()); // Asociar con moto2

        Mantenimiento maint4 = new Mantenimiento();
        maint4.setTipoMantenimiento("Revisión Anual");
        maint4.setFechaMantenimiento(LocalDate.of(2024, 7, 1));
        maint4.setKilometraje(10000);
        maint4.setNotas("Revisión básica, engrasado de cadena.");
        mantenimientoService.saveMantenimiento(maint4, moto2.getId());

        Mantenimiento maint5 = new Mantenimiento();
        maint5.setTipoMantenimiento("Cambio de Correa Distribución");
        maint5.setFechaMantenimiento(LocalDate.of(2024, 4, 5));
        maint5.setKilometraje(30000);
        maint5.setNotas("Revisión mayor, cambio de correas y ajuste de válvulas.");
        mantenimientoService.saveMantenimiento(maint5, moto3.getId());

        Mantenimiento maint6 = new Mantenimiento();
        maint6.setTipoMantenimiento("Mantenimiento Batería");
        maint6.setFechaMantenimiento(LocalDate.of(2024, 2, 1));
        maint6.setKilometraje(5000);
        maint6.setNotas("Carga y revisión de batería.");
        mantenimientoService.saveMantenimiento(maint6, moto4.getId());

        System.out.println("Mantenimientos de ejemplo creados y asociados.");
        System.out.println("Datos de ejemplo cargados exitosamente.");
    }
}