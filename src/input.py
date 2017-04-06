from __future__ import print_function
import random

def main():
	a = random.sample(range(1, 100000), 1000)
	for i in a:
		print(i)

main()