package controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping( "app/*" )
public class Login {
	
	@RequestMapping( "login" )
	public String l(){
		return "login.html";
	}

	
	@ResponseBody
	@RequestMapping( "validar_login" )
	public String v( String usuario,String clave ){
		
		System.out.println( usuario +"  "+clave );
		
		if( usuario.equals( "juan" ) && clave.equals( "123" ) ){
			return " login correctamente ";
		}else{
			return "error";
		}

	}
	
	@RequestMapping( "validar_login2" )
	public String v2( Model modelo, String usuario,String clave ){
		
		System.out.println( usuario +"  "+clave );
		
		if( usuario.equals( "juan" ) && clave.equals( "123" ) ){
			return "inicio.html";
		}else{
			modelo.addAttribute( "ERROR","Usuario o clave no son correctas" );
			return "login.html";
		}

	}
	
	public void fact(){
		System.out.println(  "estoy en el servidor" );
	}
}
