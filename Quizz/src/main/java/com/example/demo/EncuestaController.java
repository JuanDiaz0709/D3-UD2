package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class EncuestaController {

    // --- ENTRADA INICIAL ---
    @GetMapping("/")
    public String inicio() {
        return "paso1"; // Entramos por primera vez, sin datos
    }

    // --- RUTA PARA VOLVER AL PASO 1 ---
    // Recibe los datos que ya teníamos ocultos en el Paso 2 para restaurarlos
    @PostMapping("/paso1")
    public String volverAPaso1(
            @RequestParam(value = "jugador", defaultValue = "") String jugador,
            @RequestParam(value = "posicion", defaultValue = "") String posicion,
            @RequestParam(value = "nivel", defaultValue = "") String nivel,
            @RequestParam(value = "montura", defaultValue = "") String montura,
            @RequestParam(value = "resolucion", defaultValue = "") String resolucion,
            Model model) {
        
        model.addAttribute("jugador", jugador);
        model.addAttribute("posicion", posicion);
        model.addAttribute("nivel", nivel);
        model.addAttribute("montura", montura);
        model.addAttribute("resolucion", resolucion);
        return "paso1";
    }

    // --- RUTA PARA IR O VOLVER AL PASO 2 ---
    @PostMapping("/paso2")
    public String irAPaso2(
            @RequestParam(value = "jugador", defaultValue = "") String jugador,
            @RequestParam(value = "posicion", defaultValue = "") String posicion,
            @RequestParam(value = "nivel", defaultValue = "") String nivel,
            @RequestParam(value = "montura", defaultValue = "") String montura,
            @RequestParam(value = "resolucion", defaultValue = "") String resolucion,
            Model model) {
        
        // 1. Validamos los datos del Paso 1
        if (jugador.trim().isEmpty() || posicion.trim().isEmpty() || nivel.trim().isEmpty()) {
            model.addAttribute("error", "Por favor, rellena todos los campos del paso 1.");
            // Devolvemos lo escrito para no perderlo
            model.addAttribute("jugador", jugador);
            model.addAttribute("posicion", posicion);
            model.addAttribute("nivel", nivel);
            return "paso1"; 
        }

        // 2. Si todo es correcto, inyectamos Paso 1
        model.addAttribute("jugador", jugador);
        model.addAttribute("posicion", posicion);
        model.addAttribute("nivel", nivel);
        
        // 3. Inyectamos Paso 2 por si estamos retrocediendo desde el Paso 3
        model.addAttribute("montura", montura);
        model.addAttribute("resolucion", resolucion);
        
        return "paso2";
    }

    // --- RUTA PARA IR AL PASO 3 ---
    @PostMapping("/paso3")
    public String irAPaso3(
            // Arrastramos Paso 1
            @RequestParam(value = "jugador") String jugador,
            @RequestParam(value = "posicion") String posicion,
            @RequestParam(value = "nivel") String nivel,
            // Recibimos Paso 2
            @RequestParam(value = "montura", defaultValue = "") String montura,
            @RequestParam(value = "resolucion", defaultValue = "") String resolucion,
            Model model) {
        
        // Validamos los datos del Paso 2
        if (montura.trim().isEmpty() || resolucion.trim().isEmpty()) {
            model.addAttribute("error", "Por favor, selecciona montura y resolución.");
            model.addAttribute("jugador", jugador);
            model.addAttribute("posicion", posicion);
            model.addAttribute("nivel", nivel);
            model.addAttribute("montura", montura);
            model.addAttribute("resolucion", resolucion);
            return "paso2";
        }

        model.addAttribute("jugador", jugador);
        model.addAttribute("posicion", posicion);
        model.addAttribute("nivel", nivel);
        model.addAttribute("montura", montura);
        model.addAttribute("resolucion", resolucion);
        return "paso3";
    }

    // --- RESULTADO FINAL ---
    @PostMapping("/resultado")
    public String procesarFinal(
            @RequestParam(value = "jugador") String jugador,
            @RequestParam(value = "posicion") String posicion,
            @RequestParam(value = "nivel") String nivel,
            @RequestParam(value = "montura") String montura,
            @RequestParam(value = "resolucion") String resolucion,
            @RequestParam(value = "accesorios", required = false) List<String> accesorios,
            Model model) {
        
        List<String> resumen = new ArrayList<>();
        resumen.add("Jugador: " + jugador);
        resumen.add("Posición: " + posicion);
        resumen.add("Nivel: " + nivel);
        resumen.add("Montura de cámara: " + montura);
        resumen.add("Calidad de grabación: " + resolucion);
        
        if (accesorios != null && !accesorios.isEmpty()) {
            resumen.add("Accesorios extra: " + String.join(", ", accesorios));
        } else {
            resumen.add("Accesorios extra: Ninguno");
        }

        model.addAttribute("datos", resumen);
        return "resultado";
    }
}
