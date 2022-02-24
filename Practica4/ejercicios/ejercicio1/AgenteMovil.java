import java.io.File;
import java.lang.Runtime;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Scanner;

import jade.core.*;

public class AgenteMovil extends Agent
{

	private Location origen = null;
	private ArrayList<String> containers_list = null;
	private int position = 0;
	private String report = "";
	private long startTime = 0;

	// Ejecutado por unica vez en la creacion
	public void setup()
	{
		this.origen = here();
		System.out.println("\n\nHola, agente con nombre local " + getLocalName());
		System.out.println("Y nombre completo... " + getName());
		System.out.println("Y en location " + this.origen.getID() + "\n\n");

		this.getContainersNames();

		startTime = System.nanoTime();			// Empieza a contar tiempo transcurrido

		move();
	}

	// Llena una lista con los nombres de los containers y agrega el nombre del origen al final
	protected void getContainersNames(){
		try {
			Scanner sc = new Scanner(new File("scripts/containers_list.txt"));

			containers_list = new ArrayList<String>();

			while (sc.hasNextLine())	
				containers_list.add(sc.nextLine());

			containers_list.add(origen.getName());

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
		if(!ubicacion.getName().equals(origen.getName())){
		
			report = (report 
			+ "Carga de la maquina:          " + ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage() + "\n"
			+ "Memoria total disponible:     " + Runtime.getRuntime().freeMemory() + " bytes.\n"
			+ "Nombre de la maquina:         " + ubicacion.getID()
			+ "\n\n----------------------------------------------------\n\n");

		}else{
			System.out.println("\nTiempo demorado para tomar la muestra (microseg.): "  + (System.nanoTime() - startTime) / 1000 + "\n");    // Tiempo en microsegundos.
			System.out.println(report);
			position = 0;
			report = "";
			try {
				Thread.sleep(8000);		// Vuelve a realizar el recorrido cada 8 segundos
			} catch (Exception e) {
				System.out.println(e);
			}
			startTime = System.nanoTime();

		}

		move();
	}
	
}
