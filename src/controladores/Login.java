package controladores;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;

import modelos.MUsuario;
import servicios.SUsuarios;

@Controller
@RequestMapping( "app/*" )
public class Login {

	@Autowired
	SUsuarios USER;

	@RequestMapping( "login" )
	public String l( Model modelo,String expirado ){

		System.out.println( "exp:   "+expirado );
		if( expirado != null ){
			modelo.addAttribute( "expirado",true );
		}
		return "inicio.html";
	}

	@RequestMapping( "menu" )
	public String m( HttpServletRequest req,Model modelo ){

		HttpSession sesion = req.getSession( true );		
		modelo.addAttribute( "USUARIO",sesion.getAttribute( "USUARIO" ) );

		MUsuario u =  (MUsuario) sesion.getAttribute( "USUARIO" );

		if( u != null ) return "menu.html";
		else return "redirect:login?expirado=jfjsh";
	}

	@ResponseBody
	@RequestMapping( "validarlogin" )
	public String vl( HttpServletRequest req,Model modelo,String usuario,String clave ){
		// json = { exito:'boolean',mensaje:'string' } 
		JsonObject res = new JsonObject();
		try{			
			boolean exito = USER.login(usuario, clave);

			if( exito ){

				MUsuario u = new MUsuario();
				u.setLogin( "dskjhfds" );

				HttpSession sesion = req.getSession( true );
				sesion.setAttribute( "USUARIO" , u );
				sesion.setMaxInactiveInterval( 60*15 );



				modelo.addAttribute( "USUARIO",sesion.getAttribute( "USUARIO" ) );


				res.addProperty( "exito", true);
				return res.getAsJsonObject().toString();
			}else{
				res.addProperty( "exito", false);
				res.addProperty( "mensaje", "usuario o clave incorrecta");
				return res.getAsJsonObject().toString();
			}
		}catch(Exception e){
			e.printStackTrace();
			res.addProperty( "exito", false);
			res.addProperty( "mensaje", e.getMessage() );
			return res.getAsJsonObject().toString();
		}
	}
	
	@RequestMapping("logout")
	public String salir( HttpServletRequest req ){
		HttpSession sesion = req.getSession( true );
		sesion.setAttribute( "USUARIO" , null );
		return "inicio.html";
	}

}
