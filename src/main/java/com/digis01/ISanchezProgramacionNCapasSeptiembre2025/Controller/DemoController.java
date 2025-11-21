package com.digis01.ISanchezProgramacionNCapasSeptiembre2025.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("demo")
public class DemoController {

    @GetMapping("saludo/{nombre}")
    public String SaludoConNombre(@PathVariable("nombre") String nombre, Model model){
        model.addAttribute("nombre", nombre);
        return "HolaMundo";
    }
    
    @GetMapping("saludo")
    public String Saludo(){
        return "HolaMundo";
    }
    
    @GetMapping("multParam")
    public String MultiplicacionParametros(@RequestParam("numA") String numA, @RequestParam("numB") String numB, Model model){
        String res = numA + numB;
        model.addAttribute("res", res);
        return "MultNum";
    }
    
}
