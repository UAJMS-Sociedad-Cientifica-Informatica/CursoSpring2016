package controladores;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( "app/zerrores/*" )
public class ZPaginaNE {
	
	@RequestMapping("error.html")
	public String error( Model model,String titulo,String ex, String url, String status ){
		
		if( ex != null ){
			try {
				byte[] desc = ex.getBytes();
				byte[] latin1 = new String(desc, "UTF-8").getBytes("ISO-8859-1");
				ex =  new String(latin1) ;
			} catch (UnsupportedEncodingException e1) { e1.printStackTrace(); }
		}		
		
		model.addAttribute( "titulo",titulo );
		model.addAttribute( "ex",ex );
		model.addAttribute( "url",url );
		//model.addAttribute( "timestamp",timestamp );
		model.addAttribute( "status",status );
		
		return "zerrores/error.html";
	}

}
