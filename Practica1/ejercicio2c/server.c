/* A simple server in the internet domain using TCP
   The port number is passed as an argument */
#include <stdio.h>
#include <sys/types.h> 
#include <sys/socket.h>
#include <netinet/in.h>
#include <math.h>
#include <stdlib.h>
#include "hash.h"

void error(char *msg)
{
    perror(msg);
    exit(1);
}

int main(int argc, char *argv[])
{
     int sockfd, newsockfd, portno, clilen;

     printf("Enter exponent n, to express data size of the buffer as 10^n: ");
     int exp = 0;
     scanf("%d", &exp);

     //compilar usando la opcion -lm para usar pow()
     size_t arr_size = pow(10,exp);

     char * buffer = calloc(arr_size,1);
     int total_data_recv = 0;
     int data_size = 0;

     struct sockaddr_in serv_addr, cli_addr;
     int n;
     if (argc < 2) {
         fprintf(stderr,"ERROR, no port provided\n");
         exit(1);
     }
	 //CREA EL FILE DESCRIPTOR DEL SOCKET PARA LA CONEXION
     sockfd = socket(AF_INET, SOCK_STREAM, 0);
	 //AF_INET - FAMILIA DEL PROTOCOLO - IPV4 PROTOCOLS INTERNET
	//SOCK_STREAM - TIPO DE SOCKET 
	
     if (sockfd < 0) 
        error("ERROR opening socket");
     bzero((char *) &serv_addr, sizeof(serv_addr));
     //ASIGNA EL PUERTO PASADO POR ARGUMENTO
	 //ASIGNA LA IP EN DONDE ESCUCHA (SU PROPIA IP)
	 portno = atoi(argv[1]);
     serv_addr.sin_family = AF_INET;
     serv_addr.sin_addr.s_addr = INADDR_ANY;
     serv_addr.sin_port = htons(portno);
	 
	 //VINCULA EL FILE DESCRIPTOR CON LA DIRECCION Y EL PUERTO
     if (bind(sockfd, (struct sockaddr *) &serv_addr,
              sizeof(serv_addr)) < 0) 
              error("ERROR on binding");
			  
	 //SETEA LA CANTIDAD QUE PUEDEN ESPERAR MIENTRAS SE MANEJA UNA CONEXION		  
     listen(sockfd,5);
	 
	 // SE BLOQUEA A ESPERAR UNA CONEXION
     clilen = sizeof(cli_addr);
     newsockfd = accept(sockfd, 
                 (struct sockaddr *) &cli_addr, 
                 &clilen);
				 
     //DEVUELVE UN NUEVO DESCRIPTOR POR EL CUAL SE VAN A REALIZAR LAS COMUNICACIONES
	 if (newsockfd < 0) 
          error("ERROR on accept");
     bzero(buffer,arr_size);

     // LEE EL TAMAÑO DEL DATO A RECIBIR
     n = read(newsockfd, buffer, sizeof(buffer));
     data_size = atoi(buffer);
     printf("Size of arriving data: %d\n", data_size);
     bzero(buffer,arr_size);

     // LEE EL HASH CONTRA EL QUE VA A VALIDAR
     n = read(newsockfd, buffer, sizeof(buffer));
     int recv_hash = atoi(buffer);
     printf("Received HASH: %d\n", recv_hash);
     bzero(buffer,arr_size);

	//LEE EL MENSAJE DEL CLIENTE
     char rcv_data[data_size];
     while(total_data_recv < data_size){
          n = read(newsockfd,buffer,65000);
          if (n < 0) error("ERROR reading from socket");
          total_data_recv = total_data_recv + n;
          strcat(rcv_data,buffer);
          bzero(buffer,arr_size);
          printf("Total data received: %d\n ", total_data_recv);
     }
	 
	//RESPONDE AL CLIENTE
     if(hash(rcv_data) == recv_hash){
          printf("Data OK!\n");
          n = write(newsockfd,"I got your message",18);
          if (n < 0) error("ERROR writing to socket");
          return 0; 
     }else{
          error("ERROR: corrupted message.");
     }
     
     free(buffer);

     return 0;

}
