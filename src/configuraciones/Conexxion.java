package configuraciones;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class Conexxion {
	
	public JdbcTemplate db;
	
	@Resource(name = "dataSource")
	public void setDataSource( DataSource dataSource){
		db = new JdbcTemplate(dataSource);
	}

}
