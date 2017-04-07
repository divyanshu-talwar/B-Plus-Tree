#!/bin/bash
# echo "4" > bplustree.inp
# echo "1000" >> bplustree.inp
echo "Reusing the previous generated 1000 random numbers to feed to the B + Tree"
# python input.py > in
# echo "Running External Merge Sort on the Random data...."
# javac ExternalMergeSort.java
# java ExternalMergeSort in out
# cat out >> bplustree.inp
# echo "Done sorting the data!"
echo "Doing insertion..."
javac BPlusTreeNormal.java
java BPlusTreeNormal
echo "Completed! See the bplustreeNormal.out file to check the output."

exit 0