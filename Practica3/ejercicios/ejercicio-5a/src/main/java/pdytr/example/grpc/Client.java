package pdytr.example.grpc;
import io.grpc.*;

public class Client
{
    public static void main( String[] args ) throws Exception
    {
      // Channel is the abstraction to connect to a service endpoint
      // Let's use plaintext communication because we don't have certs
      final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8085")
        .usePlaintext(true)
        .build();

      // It is up to the client to determine whether to block the call
      // Here we create a blocking stub, but an async stub,
      // or an async stub with Future are always possible.
      GreetingServiceGrpc.GreetingServiceBlockingStub stub = GreetingServiceGrpc.newBlockingStub(channel);
      GreetingServiceOuterClass.HelloRequest request =
        GreetingServiceOuterClass.HelloRequest.newBuilder()
          .setName("Ray")
          .build();

      if (args.length != 1){
            System.out.println("Se necesita un argumento: times_to_repeat");
            System.exit(1); 
        }

        // parse del argumento times_repeat
        int times = Integer.parseInt(args[0]);

        long total = 0, actual = 0, media = 0;
    
        long[] nums = new long[times];

        double suma = 0;


        for(int i = 0; i < times; i++){
            long startTime = System.nanoTime();
            stub.greeting(request);
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

      // A Channel should be shutdown before stopping the process.
      channel.shutdownNow();
    }
}