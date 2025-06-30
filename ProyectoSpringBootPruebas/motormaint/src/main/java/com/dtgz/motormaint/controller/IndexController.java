package com.dtgz.motormaint.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    // Maneja la ruta raíz ("/") y muestra la página principal.
    @GetMapping("/")
    public String index() {
        return "index"; // Devuelve directamente index.html
    }

    // Mapea la ruta "/dashboard" y muestra el dashboard de la aplicación.
    @GetMapping("/dashboard")
    public String dashboard() {
        return "index"; // Devuelve directamente index.html
    }
}