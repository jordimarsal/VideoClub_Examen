package edu.mentor.core;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class SesionVideoClub extends Thread {
	protected final static String NL = System.getProperty("line.separator");
	private Socket clientSocket = null;
	private int numUser;
	private boolean isActive = true;
	private static int recepciones = 0;
	private String IDpeliSolicitada;
	PrintWriter out;
	DataBaseVidClub dbVC;
	ArrayList<Pelicula> peliSolicitada;

	public SesionVideoClub(Socket socket, int conexActivas) {
		clientSocket = socket;
		numUser = conexActivas;
		dbVC = DataBaseVidClub.getInstance();
		peliSolicitada = new ArrayList<Pelicula>();
	}

	@Override
	public void run() {
		String outputLine = "";
		String inputLine = "";
		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			while (isActive) {

				if (checkMessages()) {
					setYEnviaPeliSolicitada(IDpeliSolicitada);
				}

				if (in.ready()) {
					inputLine = in.readLine();
				}
				if (inputLine != null) {

					System.out.println("SESION " + numUser + " Recibe(" + recepciones++ + "): " + inputLine);

					try {
						Thread.sleep(100);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}

					if (in.ready()) {
						inputLine = in.readLine();
					}
					outputLine = procesarInput(inputLine);
					inputLine = null;
				}
			}
			System.out.println("SESION CERRADA");
			out.close();
			in.close();
			clientSocket.close();

		} catch (IOException ex) {
		}
	}

	private void setYEnviaPeliSolicitada(String ID) {
		if (!ID.equals("") || IDpeliSolicitada == null) {
			peliSolicitada = dbVC.getAlquileres(ID);
			boolean once = true;
			for (Pelicula p : peliSolicitada) {
				if (once) {
					directMessage("ACK1" + numUser + p.getTitulo());
					once = false;
				}
				directMessage("ACK_" + numUser + p.getID());
				directMessage("ACK_" + numUser + p.getTitulo());
				directMessage("ACK_" + numUser + Integer.toString(p.getSocio()));
				directMessage("ACK_" + numUser + p.getFecha());
			}
			IDpeliSolicitada = null;
		}
	}

	private void directMessage(String outputLine) {
		System.out.println("  directMessage SESION " + numUser + ": >" + outputLine + "<");
		out.println(outputLine);
	}

	private String procesarInput(String input) {
		String outputLine = "";
		System.out.println("# procesarInput input:<" + input + ">");
		String pre = input.substring(0, 4);
		String pos = input.substring(5, input.length());

		switch (pre) {
			case "HOLA":
				outputLine = "HELO" + numUser + pos;
				directMessage(outputLine);
				break;
			case "ACK_":
				outputLine = input;
				IDpeliSolicitada = pos;
				setYEnviaPeliSolicitada(IDpeliSolicitada);
				break;
			case "BYE_":
				isActive = false;
				break;
		}
		return outputLine;
	}

	private boolean checkMessages() {
		if (IDpeliSolicitada == null) {
			// System.out.println("    checkMessages==null");
			return false;
		}
		System.out.println("    checkMessages==true");
		return true;
	}
}
