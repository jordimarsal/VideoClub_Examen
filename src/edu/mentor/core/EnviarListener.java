package edu.mentor.core;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.mentor.ui.ClienteVideoClub;

//http://stackoverflow.com/questions/13731710/allowing-the-enter-key-to-press-the-submit-button-as-opposed-to-only-using-mo
public class EnviarListener implements ActionListener, KeyListener {

	JTextField campoTexto;
	JTextArea areaTexto;
	String nick;
	ClienteVideoClub cc;
	private ArrayList<Pelicula> listaPelis;
	private Serializar salvame;

	public EnviarListener(ClienteVideoClub cc, JTextField campoTexto, JTextArea areaTexto, String nick, Serializar salva) {
		this.campoTexto = campoTexto;
		this.areaTexto = areaTexto;
		this.nick = nick;
		this.cc = cc;
		this.salvame = salva;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		System.out.println(" ev: " + ev.getSource().getClass());
		String a;
		String clase = "" + ev.getSource().getClass();
		System.out.println(">" + clase + "<");
		if (clase.equals("class javax.swing.JTextField")) {
			System.out.println(">quesi<");
			a = "Buscar";
		} else {
			a = ((AbstractButton) ev.getSource()).getText();
		}
		if (a.equals("Buscar")) {
			System.out.println(" Buscar");
			String idBuscada = campoTexto.getText();
			if (idBuscada != null && !idBuscada.equals("")) {
				if (!tryLoadLista(idBuscada)) {
					enviar();
				}
			}
		}
		if (a.equals("Borrar")) {
			System.out.println(" Borrar");
			campoTexto.setText("");
			areaTexto.setText("");
		}
		if (a.equals("Borrar Cache")) {
			System.out.println(" Borrar Cache");
			String path = getPath();
			File fichero = new File(path + "datos.bin");
			// salvame = new Serializar("datos.bin");
			if (fichero.delete()) {
				System.out.println("El fichero ha sido borrado satisfactoriamente");
				areaTexto.append("El fichero de cache ha sido borrado" + "\n");
			} else
				System.out.println("El fichero no puede ser borrado");
			listaPelis = new ArrayList<Pelicula>();
		}

		System.out.println(" actionperformed: " + a);
	}

	private void enviar() {
		cc.procesarEnvio(campoTexto.getText());
		System.out.println("Enviado desde " + nick + "> " + campoTexto.getText());
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			enviar();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	private boolean tryLoadLista(String idBuscada) {
		listaPelis = salvame.fileDESerial();
		boolean once = true, printed = false;
		if (listaPelis == null) {
			listaPelis = new ArrayList<Pelicula>();

		} else {
			for (Pelicula peli : listaPelis) {
				if (idBuscada.equals(peli.getID())) {
					if (once) {
						areaTexto.append(peli.getTitulo() + "\n");
						once = false;
					}
					areaTexto.append(peli + "\n");
					printed = true;
				}
			}
			if (printed) return true;
		}
		return false;
	}

	private static String getPath() {
		String path = "";
		File f = new File("ruta.txt"); // Creamos un objeto file
		path = f.getAbsolutePath();
		path = path.substring(0, path.length() - 8);
		return path;
	}
}
