# B Plus Tree 
## Objective :
* To use External Merge Sort for sorting the data.  
* To implement Bulk Loading for building the B+ Tree in two ways:  
	* Top-down Insertion Algorithm  
	* Bottom-Up Insertion Algorithm  
* To compare the performance of the above mentioned algorithms with themselves and with the insertion without sorting algorithm by analyzing the quality of indices created from each of the algorithms (height, number of nodes in the B+ Tree).
* Contributor - Divyanshu Talwar

## Installation and Execution
* Run `./<script_name>` and it would first generate 1000 distinct random numbers, run `External Merge Sort` on it, and finally insert it into the B+ Tree using the `<script_name>` algorithm.
* The output of the quality of indices created from the algorithm (height, number of nodes in the B+ Tree) will be generated on STD out.
* The final B+ tree's configuration would be saved into a file with name `<script_name>.out`, when the `<script_name>` algorithm is used for insertion.
* The consolidated results are in the pdf named `BPlusTreeBulkLoadingTopDownVsBottomUp.pdf`.