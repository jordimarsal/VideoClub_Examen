package edu.mentor.core;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Serializar {
	private String path, fileName;

	public Serializar(String fileName) {
		this.fileName = fileName;
		this.path = getPath();

	}

	public void fileSerial(ArrayList<Pelicula> listaPelis) {
		try {
			FileOutputStream fos = new FileOutputStream(path + fileName);
			ObjectOutputStream sos = new ObjectOutputStream(fos);
			sos.writeObject("Lista Pelicula");
			sos.writeObject(listaPelis);
			sos.close();
		} catch (FileNotFoundException ex) {
			ex.getMessage();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Pelicula> fileDESerial() {
		ArrayList<Pelicula> listaPelis = new ArrayList<Pelicula>();
		try {
			FileInputStream fis = new FileInputStream(path + fileName);
			ObjectInputStream sis = new ObjectInputStream(fis);
			System.out.println((String) sis.readObject());
			listaPelis = (ArrayList<Pelicula>) sis.readObject();
			sis.close();
		} catch (FileNotFoundException ex) {
			ex.getMessage();
			return null;
		} catch (IOException ex) {
			ex.getMessage();
			return null;
		} catch (ClassNotFoundException ex) {
			ex.getMessage();
			return null;
		}
		return listaPelis;
	}

	public static String getPath() {
		String path = "";
		File f = new File("ruta.txt"); // Creamos un objeto file
		path = f.getAbsolutePath();
		path = path.substring(0, path.length() - 8);
		return path;
	}
}
