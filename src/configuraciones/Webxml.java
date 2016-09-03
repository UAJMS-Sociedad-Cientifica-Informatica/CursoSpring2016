package configuraciones;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

@Configuration
@ComponentScan( {"controladores","servicios"} )
public class Webxml extends WebMvcConfigurationSupport {

	//NUESTRA PAGINA DE INICIO
	// PERMITE REDIRECCIONAR A NUESTRA PAGINA DE LOGIN
	@Override
	protected void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController( "/" ).setViewName( "index.html" );
	}

	//CONFIGURAMOS LOS MAPEOS DE LOS RECURSOS
	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/css/**").addResourceLocations("css/");
		registry.addResourceHandler("/js/**").addResourceLocations("js/");
		registry.addResourceHandler("/img/**").addResourceLocations("img/");
		registry.addResourceHandler("/fonts/**").addResourceLocations("fonts/");
	}

	// PARA CONFIGURAR LA VISTA
	@Bean
	public ViewResolver viewResolver(){
		ThymeleafViewResolver tvr = new ThymeleafViewResolver();
		tvr.setTemplateEngine( templateEngine() );
		return tvr;
	}

	@Bean
	public SpringTemplateEngine templateEngine(){
		SpringTemplateEngine ste = new SpringTemplateEngine();
		ste.setTemplateResolver(templateResolver());
		return ste;
	}

	@Bean
	public TemplateResolver templateResolver(){
		ServletContextTemplateResolver ss = new ServletContextTemplateResolver();
		ss.setCacheable( false );
		ss.setPrefix( "/vistas/" );
		ss.setTemplateMode( "HTML5" );
		ss.setCharacterEncoding( "UTF-8" );
		return ss;
	}
	// FIN DE CONFIGURAR VISTA

	// REGISTRAMOS EL INTERCEPTOR DE LAS URL 
	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
		HandlerInterceptor interceptor = new Interceptor();
		registry.addInterceptor( interceptor );
	}
	
	
	// 1.-IMPORTANTE PARA EL PAGENOTFOUND Dispatcher.java
	// 2.-CONFIGUREHANDLEREXCEPTIONRESOLVERS en Config.java
	@Override
	protected void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		SimpleMappingExceptionResolver simpleMappingExceptionResolver = new ErrorMapeo();		
		simpleMappingExceptionResolver.setDefaultErrorView("redirect:/app/zerrores/error.html");
		exceptionResolvers.add(simpleMappingExceptionResolver);
	}


	/* PARA SUBIR ARCHIVOS */
	@Bean
	public CommonsMultipartResolver multipartResolver(){
		CommonsMultipartResolver mp = new CommonsMultipartResolver();
		mp.setMaxUploadSize(6 * 1024 * 1024); // 6 MB
		return mp;
	}
	/**/


	@Bean
	public DriverManagerDataSource dataSource(){

		DriverManagerDataSource base = new DriverManagerDataSource();
		String driver = "org.postgresql.Driver",
				jdbcUrl = "jdbc:postgresql://localhost:5432/taller1db2",
				usuario = "postgres",
				password = "123456";

		base.setDriverClassName( driver );
		base.setUrl( jdbcUrl );
		base.setUsername( usuario );
		base.setPassword( password );

		return base;
	}

	/*
	 CLASE PARA INTERCEPTAR LAS URLS, PERMITE VALIDAR LA SESSION DEL USUARIO LOGUEADO
	 PERO SI LA URL CONTIENE LA CADENA 'LOGIN' O LOGOUT, NO SE ANALAIZA LA SESSION ACTIVA
	 POR QUE ESTA URL ES DE ACCESO PUBLICO
	 */
	private class Interceptor extends HandlerInterceptorAdapter{

		@Override
		public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object obj) throws Exception {

			String url = req.getPathInfo();
			System.out.println( url );
			if( !url.contains("login") && !url.contains("logout") ){
				HttpSession sesion = req.getSession();
				Object user = sesion.getAttribute("USUARIO");
				if( user == null ){
					System.out.println("SESION EXPIRADA");
					res.sendRedirect( req.getContextPath() + "/app/login?expirado=dsgfhjgsf" );
					return false;
				}
			}
			return true;
		}
	}
}
