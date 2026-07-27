package pe.edu.cibertec.nexora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	//Endpoint implementado: GET /marcas
	@GetMapping
	public String listarMarcas(@RequestParam(required=false)String texto,Model model) {
		if(texto==null || texto.isBlank())
			model.addAttribute("marcas",marcaService.listar());
		else
			model.addAttribute("marcas",marcaService.buscarPorTextoIngresado(texto));
		model.addAttribute("texto",texto);
		return "/marcas/lista";
	}
	
	//Endpoint implementado: GET /marcas/nuevo
	@GetMapping("/nuevo")
	public String nuevaMarca(Model model) {
		model.addAttribute("marca", new Marca());
		return "marcas/formulario";
	}
	
	//Endpoint implementado: POST /marcas/guardar
	@PostMapping("/guardar")
	public String guardarMarca(@ModelAttribute Marca marca, RedirectAttributes flash) {
		
		boolean nuevoReg = marca.getIdMarca() == null;
		
		marcaService.guardar(marca);
		
		if(nuevoReg) {
			flash.addFlashAttribute(
					"mensaje",
					"Marca nueva registrada!"
			);
		}else {
			flash.addFlashAttribute(
					"mensaje",
					"Marca "+marca.getIdMarca()+" actualizada!"
			);
		}
		return "redirect:/marcas";
	}
	
	//Endpoint implementado: GET /marcas/editar/{id}
	@GetMapping("/editar/{id}")
	public String editarMarca(@PathVariable Integer id, Model model) {
		model.addAttribute("marca", marcaService.buscarPorId(id));
		return "marcas/formulario";
	}
	//Endpoint implementado: POST /marcas/eliminar/{id}
	@PostMapping("/eliminar/{id}")
	public String eliminarMarca(@PathVariable Integer id, RedirectAttributes flash) {
		marcaService.eliminar(id);
		flash.addFlashAttribute(
				"mensaje",
				"Marca eliminada!"
		);
		return "redirect:/marcas";
	}
	
}
