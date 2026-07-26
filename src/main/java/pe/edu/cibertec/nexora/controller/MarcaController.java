package pe.edu.cibertec.nexora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pe.edu.cibertec.nexora.entity.Marca;
import pe.edu.cibertec.nexora.service.MarcaService;

@Controller
@RequestMapping("/marcas")
public class MarcaController {
	@Autowired
	private MarcaService marcaService;
	
	@GetMapping
	public String listarMarcas(@RequestParam(required=false)String texto,Model model) {
		if(texto==null || texto.isBlank())
			model.addAttribute("marcas",marcaService.listar());
		else
			model.addAttribute("marcas",marcaService.buscarPorTextoIngresado(texto));
		model.addAttribute("texto",texto);
		return "/marcas/lista";
	}
	
	@GetMapping("/nuevo")
	public String nuevaMarca(Model model) {
		model.addAttribute("marca", new Marca());
		return "marcas/formulario";
	}
	
	
	
}
