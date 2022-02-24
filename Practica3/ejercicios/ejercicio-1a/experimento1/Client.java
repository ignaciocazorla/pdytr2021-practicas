package pdytr.example.grpc;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import io.grpc.*;

public class Client
{
    public static void main( String[] args ) throws Exception
    {
      // Channel is the abstraction to connect to a service endpoint
      // Let's use plaintext communication because we don't have certs
      final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080")
        .usePlaintext(true)
        .build();

      // It is up to the client to determine whether to block the call
      // Here we create a blocking stub, but an async stub,
      // or an async stub with Future are always possible.
      
      GreetingServiceGrpc.GreetingServiceFutureStub stub = GreetingServiceGrpc.newFutureStub(channel);

      GreetingServiceOuterClass.HelloRequest request =
        GreetingServiceOuterClass.HelloRequest.newBuilder()
          .setName("Ray")
          .build();

      // Finally, make the call using the stub
      ListenableFuture<GreetingServiceOuterClass.HelloResponse> response;
      response = stub.greeting(request);

      // Add callback to response
      Futures.addCallback(response, new FutureCallback<GreetingServiceOuterClass.HelloResponse>() {
        public void onSuccess(GreetingServiceOuterClass.HelloResponse r){
          System.out.println(r);
        }

        public void onFailure(Throwable t){
          System.out.println("Error!");
        }
      } );

      System.out.println("El cliente se cierra en dos segundos.");
      Thread.sleep(2000);
  
      // A Channel should be shutdown before stopping the process.
      channel.shutdownNow();

      // Terminate JVM
      System.exit(0);
    }

    
}