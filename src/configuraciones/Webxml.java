package configuraciones;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

@Configuration
@ComponentScan( {"controladores","servicios"} )
public class Webxml extends WebMvcConfigurationSupport {

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
}
