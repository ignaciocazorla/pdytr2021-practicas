import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import jade.core.*;

public class AgenteMovil extends Agent
{

	private Location origen = null;
	private ArrayList<String> containers_list = null;
	private int position = 0;
	private String filename = null;
	private int suma = 0;

	// Ejecutado por unica vez en la creacion
	public void setup()
	{
		this.origen = here();
		System.out.println("\n\nHola, agente con nombre local " + getLocalName());
		System.out.println("Y nombre completo... " + getName());
		System.out.println("Y en location " + this.origen.getID() + "\n\n");

		Object[] args = getArguments();
		if (args != null) {
			System.out.println("El nombre del archivo:");
			System.out.println("- " + args[0] + "\n");
			filename = args[0].toString();
		}else{
			System.out.println("\n\n\n[Error] --- El programa debe recibir como argumento el nombre del archivo a procesar.\n\n\n");
			doDelete();
			System.exit(0);
		}

		this.getContainersNames();

		move();
	}

	// Llena una lista con los nombres de los containers, pero no agrega el nombre del origen al final
	protected void getContainersNames(){
		try {
			Scanner sc = new Scanner(new File("scripts/containers_list.txt"));

			containers_list = new ArrayList<String>();

			while (sc.hasNextLine())	
				containers_list.add(sc.nextLine());


		} catch (Exception e) {
			System.out.println("[INFO] --- Error abriendo el listado de containers.");
		}
	}

	// Para migrar el agente
	protected void move(){
		try {
			ContainerID destino = new ContainerID(containers_list.get(position), null);
			position = position + 1;
			System.out.println("Migrando el agente a " + destino.getID());
			doMove(destino);
		} catch (Exception e) {
			System.out.println("\n\n\nNo fue posible migrar el agente\n\n\n");
		}
	}

	// Ejecutado al llegar a un contenedor como resultado de una migracion
	protected void afterMove()
	{
		Location ubicacion = here();
		int numero = 0;

		if(!ubicacion.getName().equals(origen.getName())){
			
			suma = 0;

			try {
				Scanner sc = new Scanner(new File("ejercicio2/" + containers_list.get(position - 1) + "/" + filename));
				while (sc.hasNextLine()){
					numero = Integer.parseInt(sc.nextLine());
					suma = numero + suma;
				}	

			} catch (Exception e) {
				System.out.println("[INFO] --- Error abriendo el archivo pasado como parametro: " + filename + ", en el agente " + ubicacion.getName() +"\n\n");
			}

			doMove(origen);
	
		}else{

			System.out.println("\n\nLa suma total de los numeros del archivo: " + suma + ".\n\n");

			if(position == containers_list.size()){
				System.out.println("[INFO] --- Termino de recorrer los containers.\n\n");
				doDelete();
			}else{
				move();
			}
		}
	}
	
}
