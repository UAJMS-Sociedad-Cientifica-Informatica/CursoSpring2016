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
	
	public MPersona get( String ci){
		String sql = "SELECT * FROM persona WHERE ci=?;";
		return db.queryForObject(sql, new mapeadora(),ci);
	}
	
	public boolean agregar( MPersona p ){
		String sql = "SELECT count(ci) FROM persona WHERE ci=?";
		int cantidad = db.queryForObject(sql, Integer.class,p.getCi());
		if( cantidad == 0 ){
			sql = "INSERT INTO persona(ci,nombre,ap,am,sexo,foto) values(?,?,?,?,?,?);";
			db.update( sql,p.getCi(),p.getNombre(),p.getAp(),p.getAm(),p.getSexo(),p.getFoto() );
			return true;
		}else{
			return false;
		}
	}
	
	public boolean modificarConFoto( MPersona p ){
		String sql = "UPDATE persona set nombre=?,ap=?,am=?,sexo=?,foto=? WHERE ci=?";
		db.update( sql,p.getNombre(),p.getAp(),p.getAm(),p.getSexo(),p.getFoto(),p.getCi() );
		return true;
	}
	
	public boolean modificarSinFoto( MPersona p ){
		String sql = "UPDATE persona set nombre=?,ap=?,am=?,sexo=? WHERE ci=?";
		db.update( sql,p.getNombre(),p.getAp(),p.getAm(),p.getSexo(),p.getCi() );
		return true;
	}
	
	public boolean baja( String ci,boolean estado ){
		String sql = "UPDATE persona set estado=? WHERE ci=?";
		db.update( sql,estado,ci );
		return true;
	}

}
