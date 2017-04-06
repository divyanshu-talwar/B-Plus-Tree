#!/bin/bash
echo "4" > bplustree.inp
echo "1000" >> bplustree.inp
echo "Generating 1000 random numbers to feed to the B + Tree"
python input.py > in
echo "Running External Merge Sort on the Random data generated...."
javac ExternalMergeSort.java
java ExternalMergeSort in out
cat out >> bplustree.inp
echo "Done sorting the data!"
echo "Doing top down insertion..."
javac BPlusTreeTopDown.java
java BPlusTreeTopDown
echo "Completed! See the bplustree.out file to check the output."

exit 0