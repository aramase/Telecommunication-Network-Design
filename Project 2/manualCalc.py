from math import factorial

print "Enter the inter-arrival rate in seconds "
arrival=float(raw_input())
erlangs=(60/arrival)*3

numerator=pow(erlangs,50)/factorial(50)

denominator=0

for i in range(0,51):
	denominator+=pow(erlangs,i)/factorial(i)

result=numerator*100/denominator
print "Blocking rate is " + str(result)