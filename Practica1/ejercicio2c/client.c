#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h> 
#include <math.h>
#include <stdlib.h>
#include "hash.h"

void error(char *msg)
{
    perror(msg);
    exit(0);
}

void initialize_buffer(char ** buffer, int arr_size){
    printf("Initializing buffer...\n");
    *buffer = malloc(1);
    strcat(*buffer,"\0");

    for(int i = 0; i< arr_size ; i++){
        *buffer = (char *) realloc(*buffer, i + 2);
        strcat(*buffer,"a");
    }
}

int main(int argc, char *argv[])
{
    int sockfd, portno, n;
    struct sockaddr_in serv_addr;
    struct hostent *server;

    printf("Enter exponent n, to express data size of the buffer as 10^n: ");
    int exp = 0;
    scanf("%d", &exp);

    //compilar usando la opcion -lm para usar pow
    size_t arr_size = pow(10,exp);
    char * buffer, data_size[10], hash_value[10];;
    initialize_buffer(&buffer, arr_size);

    printf("Buffer data size to send: %d\n",strlen(buffer));

    if (argc < 3) {
       fprintf(stderr,"usage %s hostname port\n", argv[0]);
       exit(0);
    }
	//TOMA EL NUMERO DE PUERTO DE LOS ARGUMENTOS
    portno = atoi(argv[2]);
	
	//CREA EL FILE DESCRIPTOR DEL SOCKET PARA LA CONEXION
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
	//AF_INET - FAMILIA DEL PROTOCOLO - IPV4 PROTOCOLS INTERNET
	//SOCK_STREAM - TIPO DE SOCKET 
	
    if (sockfd < 0) 
        error("ERROR opening socket");
	
	//TOMA LA DIRECCION DEL SERVER DE LOS ARGUMENTOS
    server = gethostbyname(argv[1]);
    if (server == NULL) {
        fprintf(stderr,"ERROR, no such host\n");
        exit(0);
    }
    bzero((char *) &serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
	
	//COPIA LA DIRECCION IP Y EL PUERTO DEL SERVIDOR A LA ESTRUCTURA DEL SOCKET
    bcopy((char *)server->h_addr, 
         (char *)&serv_addr.sin_addr.s_addr,
         server->h_length);
     serv_addr.sin_port = htons(portno);
	
	//DESCRIPTOR - DIRECCION - TAMAÑO DIRECCION
    if (connect(sockfd,&serv_addr,sizeof(serv_addr)) < 0) 
        error("ERROR connecting");

    // Enviar tamanio del mensaje.
    printf("Sending message size.\n");
    sprintf(data_size, "%d", strlen(buffer));
    n = write(sockfd, data_size, strlen(data_size));

    // Enviar hash del mensaje.
    printf("Sending HASH of message.\n");
    sprintf(hash_value, "%d", hash(buffer));
    n = write(sockfd, hash_value, strlen(hash_value));

    //ENVIA UN MENSAJE AL SOCKET
    n = write(sockfd,buffer,strlen(buffer));
    if (n < 0) 
         error("ERROR writing to socket");
    bzero(buffer,arr_size);

    //ESPERA RECIBIR UNA RESPUESTA
	n = read(sockfd,buffer,255);
    if (n < 0) 
         error("ERROR reading from socket");
    
	printf("%s\n",buffer);
    free(buffer);
    return 0;
}
