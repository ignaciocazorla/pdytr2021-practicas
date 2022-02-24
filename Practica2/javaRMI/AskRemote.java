/*
* AskRemote.java
* a) Looks up for the remote object
* b) "Makes" the RMI
*/
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.rmi.Naming; /* lookup */
import java.rmi.registry.Registry; /* REGISTRY_PORT */
public class AskRemote{

    private static IfaceRemoteClass remote;
    private static int bufferLength = 1024;

    private static void leer(String fuente, String destino){

        int totalBytesLeidos = 0;
        int bytesLeidos = 0;

        try{
            FileOutputStream outStream = new FileOutputStream(destino);
            
            do{
                byte [] inStream = remote.leer(fuente,totalBytesLeidos,bufferLength);
                ByteBuffer buffer = ByteBuffer.wrap(inStream);

                bytesLeidos = buffer.getInt();

                if (bytesLeidos > 0) {
                    // Se leen los bytes restantes -> Contenido del archivo
                    inStream = new byte[bytesLeidos];
                    buffer.get(inStream);

                    // Escribe en el archivo destino el contenido recibido
                    outStream.write(inStream);
                    totalBytesLeidos = totalBytesLeidos + bytesLeidos;
                }
            }while(bytesLeidos > 0);

            outStream.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }


    private static void escribir(String fuente, String destino){
        long totalBytesEscritos = 0;

        try{
            byte[] buffer = new byte[bufferLength];

            File file = new File(fuente);
            long file_size = file.length();
            int bytesLeidos = 0;

            FileInputStream inStream = new FileInputStream(fuente);

            do{
                bytesLeidos = inStream.read(buffer,0,bufferLength);
                totalBytesEscritos = remote.escribir(destino,bytesLeidos,buffer);

            }while(totalBytesEscritos < file_size);

            inStream.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }


public static void main(String[] args){


    try {
        String rname = "//localhost:" + Registry.REGISTRY_PORT + "/remote";
        remote = (IfaceRemoteClass) Naming.lookup(rname);
        String operation = "0";

        if(args.length != 0){
            operation = args[0];
        }

        switch(operation){
        case "-3":

            if (args.length != 4)
            {
            System.out.println("4 argument needed: exercise_num source_file client_filename server_filename");
            System.exit(1);
            }

            String source = args[1];
            String client_filename = args[2];
            String server_filename = args[3];

            leer(source, client_filename);
            escribir(client_filename, server_filename);

            System.out.println("Done");
            break;

        case "-4":
            bufferLength = 2;
            escribir(args[1], args[2]);
            break;

        case "-5a":
            if (args.length != 2){
                System.out.println("2 argument needed: exercise_num times_repeat");
                System.exit(1); 
            }

            // parse del argumento times_repeat
            int times = Integer.parseInt(args[1]);

            long total = 0, actual = 0, media = 0;
        
            long[] nums = new long[times];

            double suma = 0;


            for(int i = 0; i < times; i++){
                long startTime = System.nanoTime();
                remote.invocacion();
                actual = (System.nanoTime() - startTime) / 1000;    // Tiempo en microsegundos.
                nums[i] = actual;
                total = total + actual;
                System.out.println(actual);
            }

            media = total / times;

            for(int i = 0; i < times; i++){
                suma = suma + Math.pow((nums[i] - media),2);
            }

            System.out.println("Promedio: " + media);
            System.out.println("Desviacion: " + Math.sqrt(suma/times));
            
            break;

        case "-5b":

            if (args.length != 1){
                System.out.println("1 argument needed: exercise_num ");
                System.exit(1); 
            }

            remote.waitTimeoutInfiniteLoop();

            break;

        default:
            System.out.println("At least 2 argument needed: exercise_num [-3,-4,-5a,-5b]");
        }


    } catch (Exception e) {
        e.printStackTrace();
    }
    }
}