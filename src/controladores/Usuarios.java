package controladores;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import modelos.MPersona;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import servicios.SPersona;
import util.ImageResizer;

import com.google.gson.JsonObject;

@Controller
@RequestMapping("app/usuarios/*")
public class Usuarios {
	
	@Autowired
	SPersona PERSO;
	
	@RequestMapping("usuarios")
	public String u( Model modelo,Boolean estado ){
		if( estado == null ) estado = true;
		
		List<MPersona> personas = PERSO.getPersonas( estado );
		modelo.addAttribute( "PERSONAS",personas );
		modelo.addAttribute( "ESTADO",estado );
		
		for( MPersona p :personas ){
			System.out.println( p.toString() );
		}
		
		return "usuarios.html";
	}
	
	@ResponseBody
	@RequestMapping( "agregar_usuario" )
	public String jdhfkjds( HttpServletRequest req,@ModelAttribute MPersona p, MultipartFile foto2 ){
		JsonObject json = new JsonObject();		
		
		System.out.println( "*************************" );
		System.out.println( p.getCi() );
		System.out.println( p.getNombre() );
		System.out.println( p.getAp() );
		System.out.println( p.getAm() );
		System.out.println( p.getSexo() );
		System.out.println( p.getFoto() );
		
		
		
		/* VALIDAMOS QUE HAY UNA IMAGEN */
		if( foto2 != null ){
			p.setFoto( "img/"+foto2.getOriginalFilename() );	
			System.out.println( foto2.getOriginalFilename() );
			subirImagen(req, foto2, foto2.getOriginalFilename());
		}else{
			json.addProperty("exito", false);
			json.addProperty("msg", "Debe enviar una imagen.");
			return json.getAsJsonObject().toString();
		}
		
				
		boolean exito = PERSO.agregar(p);
		
		
		if( exito ) {
			json.addProperty("exito", true);
		}else{
			json.addProperty("exito", false);
			json.addProperty("msg", "El ci ya esta registrado.");
		}
		return json.getAsJsonObject().toString();
	}
	
	@ResponseBody
	@RequestMapping( "get_usuario" )
	public String get_persona(String ci ){
		
		MPersona persona = PERSO.get( ci );
		JsonObject res = new JsonObject();
		res.addProperty( "ci", persona.getCi());
		res.addProperty( "nombre", persona.getNombre());
		res.addProperty( "ap", persona.getAp());
		res.addProperty( "am", persona.getAm());
		res.addProperty( "sexo", persona.getSexo());
		res.addProperty( "foto", persona.getFoto());
		
		return res.getAsJsonObject().toString();
	}
	
	@ResponseBody
	@RequestMapping( "modificar_usuario" )
	public String mod( HttpServletRequest req,@ModelAttribute MPersona p, MultipartFile foto2 ){
		JsonObject res = new JsonObject();
		
		System.out.println( "=========================" );
		System.out.println( p.getCi() );
		System.out.println( p.getNombre() );
		System.out.println( p.getAp() );
		System.out.println( p.getAm() );
		System.out.println( p.getSexo() );
		System.out.println( p.getFoto() );
		
		/* VALIDAMOS QUE HAY UNA IMAGEN */
		if( foto2 != null ){
			p.setFoto( "img/"+foto2.getOriginalFilename() );	
			System.out.println( foto2.getOriginalFilename() );
			subirImagen(req, foto2, foto2.getOriginalFilename());
			PERSO.modificarConFoto(p);
		}else{
			PERSO.modificarSinFoto(p);
		}
		
		res.addProperty( "exito", true );
		return res.getAsJsonObject().toString();
	}
	
	@RequestMapping( "borrar_usuario" )
	public String b( String ci ){
				
		PERSO.baja(ci, false);
		
		return "redirect:usuarios";
	}

	
	/* METODO STATICO PARA SUBIR NUESTROS ARCHIVOS AL SERVIDOR */
	public static void subirImagen(HttpServletRequest req, MultipartFile imagen, String rutaimg){
		String proyecto = req.getContextPath(),
				home=Thread.currentThread().getContextClassLoader().getResource("/").getPath();
		home = home.substring(0,home.indexOf(proyecto));/**/

		File user_dir = new File(home,proyecto+"/img/"+rutaimg);
		String prefix = rutaimg.substring(0, rutaimg.lastIndexOf(".") ),
				subfix = rutaimg.substring( rutaimg.lastIndexOf(".") );

		File img_256 = new File(home,proyecto+"/img/"+prefix+"256x256"+subfix);
		File img_128 = new File(home,proyecto+"/img/"+prefix+"128x128"+subfix);

		try {
			String rutaFoto = user_dir.getAbsolutePath();
			File serverFile = new File(rutaFoto);
			BufferedOutputStream stream = new BufferedOutputStream( new FileOutputStream(serverFile) );
			byte[] bytes = imagen.getBytes();
			stream.write(bytes);
			stream.close();

			if( !subfix.equals(".gif")){
				ImageResizer resizer = new ImageResizer();
				resizer.copyImage(serverFile.toString(),img_256.toString(), 256, 256);
				resizer.copyImage(serverFile.toString(),img_128.toString(), 128, 128);
			}
			
			System.out.println(user_dir);

		} catch (Exception e) { e.printStackTrace(); }
	}
}
