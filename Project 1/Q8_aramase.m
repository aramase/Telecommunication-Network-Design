clc;
close all;

prompt='Enter the value of number of trials: ';
N=input(prompt);
myarray=zeros(N,1);

myvariable=1;
success=0;
count=1;

while myvariable<=N
    success=0;
    while success~=1
        x=rand();
        if x<=0.1
            myarray(myvariable)=0.1*(power(0.9,(count-1)));
            %myarray(myvariable)=1-power(0.5,count);
            success=1;
        end
        count=count+1;
    end
    myvariable=myvariable+1;
end

x=1:1:N;
xmarkers=1:1:N;
ymarkers=myarray(xmarkers);
plot(x,myarray,xmarkers,ymarkers,'b*')
grid on
title('Q.8 - PMF of X with p=0.1');