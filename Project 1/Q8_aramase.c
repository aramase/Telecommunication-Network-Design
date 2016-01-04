#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int main(){

	int n=1,success=0;
	float x,count=1.0,myarray[100];

	while(n<=100){
		success=0;
		while(success!=1){
			x=rand()/(double)RAND_MAX;//generate random number uniformly between 0 and 1
			if(x<=0.1){
				myarray[n-1]=count;
				success=1;//setting success flag to 1, exit out of loop
			}
			count+=1;
		}
		n++;
	}

	for(n=0;n<100;n++){
		printf("%.100f\n",(0.1*pow(0.9,myarray[n]-1)));
	}
}