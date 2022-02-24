#include "hash.h"


int hash(char * str){
    printf("Calculating hash...\n");
    int hash = 0;
    for(int i = 0; i<strlen(str); i++){
        hash = hash + str[i];
    }

    printf("HASH calculated!\n");

    return hash;
}