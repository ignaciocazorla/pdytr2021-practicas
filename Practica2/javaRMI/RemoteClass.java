/*
* RemoteClass.java
* Just implements the RemoteMethod interface as an extension to
* UnicastRemoteObject
*
*/
/* Needed for implementing remote method/s */
import java.io.*;
import java.nio.ByteBuffer;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
/* This class implements the interface with remote methods */
public class RemoteClass extends UnicastRemoteObject implements IfaceRemoteClass{

    protected RemoteClass() throws RemoteException{
        super();
    }

    /* Remote method implementation */
    public byte[] leer(String fileName, int position, int data_rcv_size) throws RemoteException{

        InputStream inStream = null;
        byte [] data = null;

        int bytesLeidos = 0;

        ByteBuffer buffer = null;

        try{
            
            inStream = new FileInputStream(fileName);

            data = new byte[data_rcv_size];
            // skip se saltea la cantidad de bytes indicada
            inStream.skip(position);

            bytesLeidos = inStream.read(data,0, data_rcv_size);

            //Aloca espacio para los bytes del archivo y para el número de bytes leidos.
            buffer = ByteBuffer.allocate(data_rcv_size + 4);

            //Almacena el tamaño de los bytes que se van a enviar.
            buffer.putInt(bytesLeidos);

            //Almacena los bytes leidos desde el archivo.
            buffer.put(data);

            inStream.close();

            return buffer.array();

        }catch(IOException e){
            e.printStackTrace();
        }

        return data;
    }

    public long escribir(String fileName, int data_size, byte[] data)throws RemoteException{
        FileOutputStream outStream = null;
        File file = null;
        try{
            file = new File(fileName);

            if(file.exists()){
                outStream = new FileOutputStream(file,true);
            }else{
                outStream = new FileOutputStream(file);
            }
            
            outStream.write(data,0,data_size);

            outStream.close();

        } catch (Exception e){
            e.printStackTrace();
        }

        return file.length();
    }

    public void invocacion() throws RemoteException{}

    public void waitTimeoutInfiniteLoop() throws RemoteException{
        try {
            System.out.println("Se conecta el cliente");
            do{ }while(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}