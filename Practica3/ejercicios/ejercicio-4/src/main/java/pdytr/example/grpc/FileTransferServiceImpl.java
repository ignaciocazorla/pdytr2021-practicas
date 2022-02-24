package pdytr.example.grpc;

import java.io.*;
import com.google.protobuf.ByteString;

import io.grpc.stub.StreamObserver;

public class FileTransferServiceImpl extends FileTransferServiceGrpc.FileTransferServiceImplBase{
    @Override
    public void leer(FileTransferServiceOuterClass.ReadRequest request,
            StreamObserver<FileTransferServiceOuterClass.ReadResponse> responseObserver){

        InputStream inStream = null;
        byte [] data = null;
        int bytesLeidos = 0;
        ByteString buffer = null;

        try{
            
            inStream = new FileInputStream(request.getFilename());

            data = new byte[request.getReadSize()];

            // skip se saltea la cantidad de bytes indicada
            inStream.skip(request.getFilePosition());

            bytesLeidos = inStream.read(data,0, request.getReadSize());

            buffer = ByteString.copyFrom(data);

            inStream.close();

        }catch(IOException e){
            e.printStackTrace();
        }


        FileTransferServiceOuterClass.ReadResponse response = FileTransferServiceOuterClass.ReadResponse.newBuilder()
        .setDataSize(bytesLeidos)
        .setData(buffer)
        .build();
        
        // Use responseObserver to send a single response back
        responseObserver.onNext(response);

        // When you are done, you must call onCompleted.
        responseObserver.onCompleted();
    }

    @Override
    public void escribir(FileTransferServiceOuterClass.WriteRequest request,
        StreamObserver<FileTransferServiceOuterClass.WriteResponse> responseObserver){  

        FileOutputStream outStream = null;
        File file = null;
        try{
            file = new File(request.getFilename());

            if(file.exists()){
                outStream = new FileOutputStream(file,true);
            }else{
                outStream = new FileOutputStream(file);
            }
            
            outStream.write(request.getData().toByteArray(),0,request.getDataSize());

            outStream.close();

        } catch (Exception e){
            e.printStackTrace();
        }

        FileTransferServiceOuterClass.WriteResponse response = FileTransferServiceOuterClass.WriteResponse.newBuilder()
        .setWrittenBytes(file.length()).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

}
