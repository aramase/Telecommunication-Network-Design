/*(a) p = 0.4 : j = 2, j = 10, j = 50, j = 100, j = 200.
(b) p = 0.2 : j = 2, j = 10, j = 50, j = 100, j = 200.
(c) p = 0.02 : j = 2, j = 10, j = 50, j = 100, j = 200.*/


#include <stdio.h>
#include <stdlib.h>
#include <math.h>

void calcMean(double);

int main(){
	calcMean(0.4);//(a)
	calcMean(0.2);//(b)
	calcMean(0.02);//(c)
}

void calcMean(double p){
	int j[5]={2,10,50,100,200};//array of j-th term
	int x,i,k;
	float sumOfSeries[5]={0};
	for(i=0;i<5;i++){
		for(k=1;k<=j[i];k++){
			sumOfSeries[i]+=(k*(p*pow((1-p),k-1)));
		}
	}

	printf("\nOutput for p=%f\n",p);
	for(i=0;i<5;i++){
		printf("j=%d\t%.20f\n",j[i],sumOfSeries[i]);
	}
}