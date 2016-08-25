package configuraciones;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

@Configuration
@ComponentScan( {"controladores","servicios"} )
public class Webxml extends WebMvcConfigurationSupport {
	
	@Override
	protected void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController( "/" ).setViewName( "index.html" );
	}
	
	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/css/**").addResourceLocations("css/");
		registry.addResourceHandler("/js/**").addResourceLocations("js/");
		registry.addResourceHandler("/img/**").addResourceLocations("img/");
		registry.addResourceHandler("/fonts/**").addResourceLocations("fonts/");
	}

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
}
