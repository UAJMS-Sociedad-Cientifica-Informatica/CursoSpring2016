package controladores;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( "app/usuarios/*" ) 
public class Principal {

	@RequestMapping( "inicio" )
	public String i( Model modelo ){
		
		List<String> vv = new ArrayList<String>();
		vv.add( "1" );
		vv.add( "2" );
		vv.add( "3" );
		vv.add( "4" );
		
		Boolean var = true;
		
		modelo.addAttribute( "mensaje", vv );
		modelo.addAttribute( "booleano", var );
		
		return "index.html";
	}
}
