package controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import modelos.MPersona;
import servicios.SPersona;

@Controller
@RequestMapping("app/usuarios/*")
public class Usuarios {
	
	@Autowired
	SPersona PERSO;
	
	@RequestMapping("usuarios")
	public String u( Model modelo ){
		
		List<MPersona> personas = PERSO.getPersonas( true );
		modelo.addAttribute( "PERSONAS",personas );
		
		for( MPersona p :personas ){
			System.out.println( p.toString() );
		}
		
		return "usuarios.html";
	}
	
	@RequestMapping( "agregar_usuario" )
	public String jdhfkjds( @ModelAttribute MPersona p ){
		
		System.out.println( "*************************" );
		System.out.println( p.getCi() );
		System.out.println( p.getNombre() );
		System.out.println( p.getAp() );
		System.out.println( p.getAm() );
		System.out.println( p.getSexo() );
		System.out.println( p.getFoto() );
		
		p.setFoto( "dsjfhjhf" );	
		
		//System.out.println( foto2.getOriginalFilename() );
		System.out.println( "*************************" );
		
		PERSO.agregar(p);
		
		return "redirect:usuarios";
	}

}
