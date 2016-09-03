package servicios;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import configuraciones.Conexxion;
import modelos.MPersona;

@Service
public class SPersona extends Conexxion {
	
	public class mapeadora implements RowMapper<MPersona>{
		@Override
		public MPersona mapRow(ResultSet rs, int arg1) throws SQLException {
			MPersona p = new MPersona();
			p.setCi( rs.getString( "ci" ) );
			p.setNombre( rs.getString( "nombre" ) );
			p.setAp( rs.getString( "ap" ) );
			p.setAm( rs.getString( "am" ) );
			p.setFoto( rs.getString( "foto" ) );
			p.setSexo( rs.getString( "sexo" ) );
			p.setEstado( rs.getBoolean( "estado" ) );
			return p;
		}
	}
	
	public List<MPersona> getPersonas( boolean estado ){
		String sql = "SELECT * FROM persona WHERE estado=?";
		return db.query(sql, new mapeadora(),estado);
	}
	
	public boolean agregar( MPersona p ){
		String sql = "INSERT INTO persona(ci,nombre,ap,am,sexo,foto) values(?,?,?,?,?,?);";
		db.update( sql,p.getCi(),p.getNombre(),p.getAp(),p.getAm(),p.getSexo(),p.getFoto() );
		return true;
	}

}
