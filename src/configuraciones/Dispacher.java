package configuraciones;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.springframework.util.ObjectUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class Dispacher extends AbstractAnnotationConfigDispatcherServletInitializer {
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return null;
	}



	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[]{ Webxml.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[]{ "/*" };
	}


	// 1.-IMPORTANTE PARA EL Dispatcher.java
	// 2.-CONFIGUREHANDLEREXCEPTIONRESOLVERS en Config.java
	@Override
	protected void registerDispatcherServlet(ServletContext servletContext) {
		String servletName = super.getServletName();
		WebApplicationContext servletAppContext = super.createServletApplicationContext();
		DispatcherServlet dispatcherServlet = new DispatcherServlet(servletAppContext);
		dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
		ServletRegistration.Dynamic registration = servletContext.addServlet(servletName, dispatcherServlet);

		registration.setLoadOnStartup(1);
		registration.addMapping(getServletMappings());
		registration.setAsyncSupported(isAsyncSupported());

		Filter[] filters=getServletFilters();
		if (!ObjectUtils.isEmpty(filters)) {
			for (    Filter filter : filters) {
				registerServletFilter(servletContext,filter);
			}
		}

		super.customizeRegistration(registration);
	}


}
