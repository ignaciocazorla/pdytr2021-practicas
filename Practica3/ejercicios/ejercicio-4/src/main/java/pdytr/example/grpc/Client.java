package pdytr.example.grpc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.google.protobuf.ByteString;

import io.grpc.*;

public class Client {

    private static int bufferLength = 1024;

    public static void leer(FileTransferServiceGrpc.FileTransferServiceBlockingStub stub,
                String origen, String destino){
        int pos = 0;

        FileTransferServiceOuterClass.ReadRequest request = 
            FileTransferServiceOuterClass.ReadRequest.newBuilder()
            .setFilename(origen)
            .setReadSize(bufferLength)
            .setFilePosition(pos).build();

        FileTransferServiceOuterClass.ReadResponse response = stub.leer(request);

        try{
            FileOutputStream outStream = new FileOutputStream(destino);
            byte [] inStream = null;
            int bytesLeidos = 0;

            while(response.getDataSize() != -1){
                bytesLeidos = response.getDataSize();
                
                inStream = response.getData().toByteArray();

                outStream.write(inStream, 0, bytesLeidos);

                pos += bytesLeidos;

                request = FileTransferServiceOuterClass.ReadRequest.newBuilder()
                .setFilename(origen)
                .setReadSize(bufferLength)
                .setFilePosition(pos).build();
            
                response = stub.leer(request);
            }

            System.out.println("[INFO] Termino la operacion de lectura.");

            outStream.close();

        }catch (Exception e){
                e.printStackTrace();
        }
    }

    public static void escribir(FileTransferServiceGrpc.FileTransferServiceBlockingStub stub,
                String origen, String destino){

        try{
            byte[] buffer = new byte[bufferLength];

            File file = new File(origen);
            long file_size = file.length();
            int bytesLeidos = 0;

            FileInputStream inStream = new FileInputStream(file);

            FileTransferServiceOuterClass.WriteResponse response;

            do{
                bytesLeidos = inStream.read(buffer,0,bufferLength);

                FileTransferServiceOuterClass.WriteRequest request = 
                FileTransferServiceOuterClass.WriteRequest.newBuilder()
                .setFilename(destino)
                .setDataSize(bytesLeidos)
                .setData(ByteString.copyFrom(buffer)).build();
                
                response = stub.escribir(request);

            }while(response.getWrittenBytes() < file_size);

            System.out.println("[INFO] Termino la operacion de escritura.");

            inStream.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public static void main( String[] args ) throws Exception{
        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8084")
        .usePlaintext(true)
        .build();

        FileTransferServiceGrpc.FileTransferServiceBlockingStub stub = FileTransferServiceGrpc.newBlockingStub(channel);

        String operation = "", sourceFilename = "", destFilename = "";
        
        if(args.length != 0){
            operation = args[0];
            sourceFilename = args[1];
            destFilename = args[2];
        }

        if(args.length == 4){
            bufferLength = Integer.parseInt(args[3]);
        }

        switch(operation){
            case "-r":
                leer(stub, sourceFilename, destFilename);
                break;
            
            case "-w":
                escribir(stub, sourceFilename, destFilename);
                break;
            
            default:
                System.out.println("[HELP] Parameters: ./client [-r|-w] source_file dest_file");
            }

        // A Channel should be shutdown before stopping the process.
        channel.shutdownNow();

    }
}
