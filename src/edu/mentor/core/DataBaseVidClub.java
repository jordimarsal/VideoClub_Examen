package edu.mentor.core;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DataBaseVidClub {

	// constantes
	private static final String SQL_UNAPELI = "SELECT * FROM ALQUILERES WHERE ID='";
	private static final String SQL_ELIMINARFILA = "DELETE FROM ALQUILERES WHERE ID='";
	private static final String SQL_UPDATE1 = "UPDATE ALQUILERES SET TITULO='";
	private static final String SQL_UP3 = "', FECHA='";
	private static final String SQL_UP2 = "', SOCIO=";
	private static final String SQL_UPDATEFINAL = " WHERE ID='";
	// variables de clase
	private static DataBaseVidClub database;
	// private static String _usuario = "root";
	// private static String _pwd = "";
	private static String _bd = "videoclub";
	private static String _path = "";// "C:/Users/Jordi/workspaceAulaMentor/Unidad3_1A1_1/assetsdb/";
	private static String _url = "";// "jdbc:derby:" + _path + _bd;
	private static Connection conn = null;
	private static Statement st;
	private static ResultSet rs;

	// Implementación del patrón Singleton
	// Constructor - privado -
	private DataBaseVidClub() {
		this._path = getPathInverseSlash() + "assetsdb/";
		System.out.println("path:#" + _path + "#");
		_url = "jdbc:derby:" + _path + _bd;
		// CreateDataBase.createDBFile(_bd, _path);
	}

	public static DataBaseVidClub getInstance() {
		if (database == null) {
			database = new DataBaseVidClub();
		}
		return database;
	}

	public ArrayList<Pelicula> getAlquileres(String id) {
		connect();
		ArrayList<Pelicula> alquilerUnaPeli = new ArrayList<Pelicula>();
		try {
			st = conn.createStatement();
			rs = st.executeQuery(SQL_UNAPELI + id + "'");
			while (rs.next()) {
				Pelicula film = new Pelicula();
				film.setFecha(rs.getString(4));
				film.setTitulo(rs.getString(2));
				film.setID(rs.getString(1));
				film.setSocio(rs.getInt(3));

				System.out.println(film);
				alquilerUnaPeli.add(film);
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			System.out.println("Fallo al crear statement");
			e.printStackTrace();
		}
		close();
		return alquilerUnaPeli;
	}

	public void insertarAlumno(Pelicula al) {

		connect();
		try {
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ALQUILERES VALUES (?,?,?,?)");
			pstmt.setString(1, al.getID());
			pstmt.setString(2, al.getTitulo());
			pstmt.setString(4, al.getFecha());
			pstmt.setInt(3, al.getSocio());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println("Error en el update de Alumno");
			e.printStackTrace();
		}
		close();
	}

	public void actualizarPeli(Pelicula al) {
		connect();
		try {
			st = conn.createStatement();
			st.executeUpdate(SQL_UPDATE1 + al.getTitulo() + SQL_UP2 + al.getSocio() + SQL_UP3 + al.getFecha()
					+ SQL_UPDATEFINAL + al.getID() + "'");
			System.out.println("Actualizada Pelicula: " + al.getID());
			rs.close();
			st.close();
		} catch (SQLException e) {
			System.out.println("Fallo al actualizar Pelicula");
			e.printStackTrace();
		}
		close();
	}

	public void eliminarPelicula(Pelicula al) {
		connect();
		try {
			st = conn.createStatement();
			System.out.println("clave:#" + al.getID() + "#");
			st.executeUpdate(SQL_ELIMINARFILA + al.getID() + "'");
			System.out.println("Eliminada Pelicula " + al.getID());
			rs.close();
			st.close();
		} catch (SQLException e) {
			System.out.println("Fallo al eliminar Pelicula");
			e.printStackTrace();
		}
		close();
	}

	private void connect() {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			conn = DriverManager.getConnection(_url);
		} catch (ClassNotFoundException e) {
			System.out.println("Driver mal enlazado");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Está activa la base de datos? es ese path?");
			e.printStackTrace();
		}
	}

	private void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("Error al cerrar la conexión");
			e.printStackTrace();
		}
	}

	private static String getPath() {
		String path = "";
		File f = new File("ruta.txt"); // Creamos un objeto file
		path = f.getAbsolutePath();
		path = path.substring(0, path.length() - 8);
		return path;
	}

	private static String getPathInverseSlash() {
		String path = getPath();
		String cha = "";
		for (char c : path.toCharArray()) {
			if (c == '\\') c = '/';
			cha = cha + c;
		}
		return cha;
	}
}
