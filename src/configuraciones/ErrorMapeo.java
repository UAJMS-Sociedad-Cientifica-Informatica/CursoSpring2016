package configuraciones;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

public class ErrorMapeo extends SimpleMappingExceptionResolver {


	@Override
	protected ModelAndView doResolveException( HttpServletRequest req, HttpServletResponse res,Object h, Exception ex) {
		ModelAndView mav = super.doResolveException(req,res, h, ex);
		if( ex != null ) ex.printStackTrace();
		
		String titulo = "";
		Integer status = 0;
		
		if( ex instanceof NoHandlerFoundException) {
			titulo = "La pagina web solicitada no existe o no esta disponible temporalmente.";
			status = 404;
			
		}else if( ex instanceof CannotGetJdbcConnectionException ) {
			titulo = "Error al conectar con la base de datos";
			status = 500;
			
		}else if (ex instanceof MethodArgumentTypeMismatchException ){
			titulo = "Error argumento no valido";
			status = 400;
			
		}else {
			titulo = "Lo sentimos, estamos trabajando para mejorar nuestro servicio";
		}
		
		System.out.println( "SIMPLE: status "+res.getStatus() );
		
		mav.addObject( "titulo", titulo );
		mav.addObject( "ex", ex.getMessage() );
		mav.addObject( "url", req.getRequestURL() );
		mav.addObject( "timestamp", new Date() );
		mav.addObject( "status", status );
		return mav;

	}

}
