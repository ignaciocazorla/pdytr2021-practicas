#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h> 
#include <time.h>
#include <stdlib.h>

void error(char *msg)
{
    perror(msg);
    exit(0);
}

double dwalltime(){
        double sec;
        struct timeval tv;

        gettimeofday(&tv,NULL);
        sec = tv.tv_sec + tv.tv_usec;
        return sec;
}

double calcular_promedio(double * tiempos, int cantidad){
    double suma = 0;
    for (int i = 0; i < cantidad; i++){
        suma += tiempos[i];
    }
    return (suma / cantidad);
}

double calcular_desviacion(double * tiempos, double media, int cantidad){
    double suma = 0;
    for (int i = 0; i < cantidad; i++){
        suma += pow((tiempos[i] - media),2);
    }
    return (sqrt(suma / cantidad));
}

int main(int argc, char *argv[])
{
    int sockfd, portno, n;
    struct sockaddr_in serv_addr;
    struct hostent *server;

    char * buffer[256];
    int num_comunicaciones = 100;
    double timetick;

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

    double tiempo_actual = 0;
    double tiempos[num_comunicaciones - 1];

    for (int i = 0; i < num_comunicaciones; i++){
        timetick = dwalltime();
	    n = write(sockfd,"hello",5);
        if (n < 0) error("ERROR writing to socket");
	    n = read(sockfd,buffer,256);
        if (n < 0) error("ERROR reading from socket");

        tiempo_actual = dwalltime() - timetick;

        printf("%.0f\n", tiempo_actual);
        tiempos[i] = tiempo_actual;

    }

    double promedio = calcular_promedio(tiempos, num_comunicaciones);

    printf("El promedio : %.0f\n", promedio);
    printf("La desviacion estandar: %.0f\n", calcular_desviacion(tiempos,promedio, num_comunicaciones));

    return 0;
}
