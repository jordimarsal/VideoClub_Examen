package edu.mentor.core;
import java.io.Serializable;

public class Pelicula implements Serializable {

	protected final static String NL = System.getProperty("line.separator");

	/**
	       
	     */
	private static final long serialVersionUID = 1L;
	private String clave;
	private String nombre;
	private String fecha;
	private int numsocio;

	public Pelicula(String clave) {
		this.clave = clave;

	}

	public Pelicula() {

	}

	public String getID() {
		return clave;
	}

	public void setID(String clave) {
		this.clave = clave;
	}

	public String getTitulo() {
		return nombre;
	}

	public void setTitulo(String nombre) {
		this.nombre = nombre;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public int getSocio() {
		return numsocio;
	}

	public void setSocio(int numsocio) {
		this.numsocio = numsocio;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(fecha).append(" por socio ");
		sb.append(numsocio);// .append(NL);
		return sb.toString();
	}

}
