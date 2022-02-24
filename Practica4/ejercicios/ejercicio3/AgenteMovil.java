import jade.core.*;

public class AgenteMovil extends Agent{

	private Location origen = null;
	private String operation = "";
	private Lector lector;

	// Ejecutado por unica vez en la creacion
	public void setup(){
		this.origen = here();
		System.out.println("\n\nHola, agente con nombre local " + getLocalName());
		System.out.println("Y nombre completo... " + getName());
		System.out.println("Y en location " + origen.getID() + "\n\n");

		String path1 = "", path2 = "";
		Object[] args = getArguments();
		if (args != null) {
			if(args.length == 3){
				operation = args[0].toString();
				path1 = args[1].toString();
				path2 = args[2].toString();
			}else{
				System.out.println("Uso : w (write) | r (read) , path to source file, path to destiny file.\n\n");
				System.exit(0);
			}
		}

		lector = new Lector(path1, path2);

		if(operation.equals("w")){
			lector.leer();
		}

		// Para migrar el agente
		try {
			ContainerID destino = new ContainerID("Main-Container", null);
			System.out.println("Migrando el agente a " + destino.getID());
			doMove(destino);
		} catch (Exception e) {
			System.out.println("\n\n\nNo fue posible migrar el agente\n\n\n");
		}
	}

	// Ejecutado al llegar a un contenedor como resultado de una migracion
	protected void afterMove(){
		Location actual = here();

		if(!lector.operationDone()){

			switch (operation) {
				case "r":
						if (!this.origen.getName().equals(actual.getName())){
							lector.leer();
						}else{
							lector.escribir();
						}
					break;
	
				case "w":
						if(this.origen.getName().equals(actual.getName())){
							lector.leer();
						}else{
							lector.escribir();
						}
					break;
			
				default:
						System.out.println("Uso : w (write) | r (read) , path to source file, path to destiny file.\n\n");
						System.exit(0);
					break;
			}

			if(actual.getName().equals(origen.getName())){
				ContainerID destino = new ContainerID("Main-Container", null);
				doMove(destino);
			}else{
				doMove(origen);
			}
		}else{
			if(actual.getName().equals(origen.getName())){
				System.out.println("\n\n[INFO] -- Se termino de transferir el archivo.\n\n");
				doDelete();
			}else{
				doMove(origen);
			}
		}
	}

}
