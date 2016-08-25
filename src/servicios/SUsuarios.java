package servicios;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import configuraciones.Conexxion;
import modelos.MUsuario;

@Service
public class SUsuarios extends Conexxion {
	
	public class Mapeador implements RowMapper<MUsuario>{
		@Override
		public MUsuario mapRow(ResultSet rs, int arg1) throws SQLException {
			MUsuario u = new MUsuario();
			u.setPersona( rs.getInt( "persona" ) );
			u.setRol( rs.getString( "rol" ) );
			u.setAlcanze( rs.getString( "alcance" ) );
			u.setLogin( rs.getString( "login" ) );
			u.setPasswd( rs.getString( "passwd" ) );
			u.setRed_salud( rs.getInt( "red_salud" ) );
			u.setMunicipio( rs.getInt( "municipio" ) );
			u.setCentro_salud( rs.getInt( "centro_salud" ) );
			u.setActivo( rs.getBoolean( "activo" ) );
			return u;
		}
	}
	
	public boolean login( String login,String passwd ){
		
		System.out.println( login+"  "+passwd );
		
		String sql = "select count(*) from usuario where login = ? and passwd = ?";
		int res = db.queryForObject(sql, Integer.class,login,passwd );
		if( res > 0 ) return true;
		else return false;		
	}

}
