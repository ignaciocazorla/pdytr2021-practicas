import java.io.*;
import java.nio.ByteBuffer;


public class Lector implements Serializable{

	private int position = 0;
	private int read_size = 1024;
	private String fuente = "";
	private String destino = "";
	private byte[] data = null;

    private int totalBytesLeidos = 0;
    private int bytesLeidos = 0;

    private boolean done = false;

    public Lector(String fuente, String destino){
        this.fuente = fuente;
        this.destino = destino;
    }

    public void leer(){

        InputStream inStream = null;

        bytesLeidos = 0;

        ByteBuffer buffer = null;

        try{
            
            inStream = new FileInputStream(this.fuente);

            data = new byte[read_size];
            // skip se saltea la cantidad de bytes indicada
            inStream.skip(position);

            bytesLeidos = inStream.read(data,0, read_size);

            //Aloca espacio para los bytes del archivo
            buffer = ByteBuffer.allocate(read_size);

            //Almacena los bytes leidos desde el archivo.
            buffer.put(data);

            inStream.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void escribir(){

        try{
            FileOutputStream outStream = null;
            File file = null;

            byte [] inStream = this.data;

            file = new File(destino);

            if(file.exists()){
                outStream = new FileOutputStream(file,true);
            }else{
                outStream = new FileOutputStream(file);
            }

            if(!this.isEmpty()){
                ByteBuffer buffer = ByteBuffer.wrap(inStream);

                if (bytesLeidos > 0) {
                    // Se leen los bytes restantes -> Contenido del archivo
                    inStream = new byte[bytesLeidos];
                    buffer.get(inStream);

                    // Escribe en el archivo destino el contenido recibido
                    outStream.write(inStream);
                    totalBytesLeidos = totalBytesLeidos + bytesLeidos;
                    position = position + bytesLeidos;
                }else{
                    this.done = true;
                }
            }

            this.data = null;

            outStream.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean isEmpty(){
        return (this.data == null);
    }

    public boolean operationDone(){
        return this.done;
    }

}
