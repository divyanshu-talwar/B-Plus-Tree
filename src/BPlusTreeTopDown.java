import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

class BPlusTreeTopDown {

    private static Node tree;
    private static int order;
    private static int insertions;
    public static int height;
    public static int nodes;
    
    private BPlusTreeTopDown(int x, int y) {
    	order = x;
    	insertions = y;
    	height = 0;
    	nodes = 0;    	
    	tree = new LeafNode(order);
    	
    }
    
    
    private static void insertIntoTree(DataNode dnode) {
    	tree = tree.insert(dnode);
	}

	@SuppressWarnings("unchecked")
    private static void printTree(BufferedWriter output) throws IOException {
        Vector<Node> nodeList = new Vector();
        
        nodeList.add(tree);
        boolean done = false;
        nodes++;
        while(! done) {
            Vector<Node> nextLevelList = new Vector();
            String toprint = "";
            
            for(int i=0; i < nodeList.size(); i++) {
                Node node = (Node)nodeList.elementAt(i);
                
                toprint += node.toString() + " ";
                
                if(node.isLeafNode()) {
                    done = true;
                }
                else
                {
                    for(int j=0; j < node.size()+1 ; j++) {
                    	nodes++;
                        nextLevelList.add( ((TreeNode)node).getPointerAt(j) );
                    }
                }
            }
            height++;
            output.write(toprint + System.getProperty("line.separator"));
            
            nodeList = nextLevelList;
        }
	}

	private static void readorder(BufferedReader in) {
		try {
        	int x = Integer.parseInt(in.readLine().trim());
        	int y = Integer.parseInt(in.readLine().trim());
        	
        	new BPlusTreeTopDown(x,y);
        	
        } catch (Exception e1) {
            System.err.println("order could not be read");
            System.exit(0);
        }
    }
    
    public static void main(String[] args) throws IOException {
        if(args.length > 1) {
            System.err.println("Syntax error in call sequence, use:\n\tjava BplusTreeTopDown");
        }
        else {
            
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            
            BufferedWriter output = new BufferedWriter( new FileWriter(new File("bplustreetopdown.out")) );            
            try {
                in = new BufferedReader(new InputStreamReader(new FileInputStream("bplustree.inp")));
            } catch (FileNotFoundException e) {
                System.err.println("Error: specified file not found (defaulting to standard input)");
            }
            
            readorder(in);
        	
            int i = 0;
            while(i < insertions) {
                try {
                	int xvalue = Integer.parseInt(in.readLine().trim());
                	insertIntoTree(new DataNode(xvalue));
                	i = i+1;
                } 
                catch (IOException e) {
                    e.printStackTrace();
                }
                catch (NumberFormatException e) {
                	System.err.println("This type of command requires a integer operand");
                    System.out.println("Valid Query-Modes:\n\ti x - insert x into tree\n\ts x - find x in tree\n\tp   - print tree\n\tq   - quit");
                }
                catch (Exception e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
            printTree(output);
            System.out.println("Top-down approach done!");
            System.out.println("Total number of nodes : " + nodes);
            System.out.println("Height of the tree : " + height);
            output.close();
            in.close();
            System.exit(0);
        }
    }
}

abstract class Node {
	protected Vector<DataNode> data;
	protected Node parent;
	protected int maxsize;

	public boolean isLeafNode() {
	    return this.getClass().getName().trim().equals("LeafNode");
	}

	abstract Node insert(DataNode dnode);

	protected boolean isFull() {
		return data.size() == maxsize-1;
	}
	
	public DataNode getDataAt(int index) {
		return (DataNode) data.elementAt(index);
	}
	
	protected void propagate(DataNode dnode, Node right) {

		if(parent == null) {
			
			TreeNode newparent = new TreeNode(maxsize);
			
			newparent.data.add(dnode);
			newparent.pointer.add(this);
			newparent.pointer.add(right);
			
			this.setParent(newparent);
			right.setParent(newparent);
		}
		else {
			if( ! parent.isFull() ) {
				boolean dnodeinserted = false;
				for(int i = 0; !dnodeinserted && i < parent.data.size(); i++) {
					if( ((DataNode)parent.data.elementAt(i)).inOrder(dnode) ) {
						parent.data.add(i,dnode);
						((TreeNode)parent).pointer.add(i+1, right);
						dnodeinserted = true;
					}
				}
				if(!dnodeinserted) {
					parent.data.add(dnode);
					((TreeNode)parent).pointer.add(right);
				}
				
				right.setParent(this.parent);
			}
			else {
                ((TreeNode)parent).split(dnode, this, right);

			}
		}
	}
	
	public int size() {
		return data.size();
	}

	@SuppressWarnings("unchecked") Node(int order) {
	    parent = null;
	    
	    data = new Vector();
	    maxsize = order;
	}
	
	public String toString() {
		String s = "";
		for(int i=0; i < data.size(); i++) {
			s += ((DataNode)data.elementAt(i)).toString() + " ";
		}
		return s + "#";
	}

	protected Node findRoot() {
		Node node = this;
		
		while(node.parent != null) {
			node = node.parent;
		}
		
		return node;
	}

	protected void setParent(Node newparent) {
		this.parent = newparent;
	}
} 

class LeafNode extends Node {
	private LeafNode nextNode;
	
	LeafNode(int order) {
		super(order);
		
		nextNode = null;
	}
	
	private void setNextNode(LeafNode next) {
		nextNode = next;
	}
	
	protected LeafNode getNextNode() {
		return nextNode;
	}

	protected void split(DataNode dnode) {
		boolean dnodeinserted = false;
		for(int i=0; !dnodeinserted && i < data.size(); i++) {
			if( ((DataNode)data.elementAt(i)).inOrder(dnode) ) {
				data.add(i,dnode);
				dnodeinserted = true;
			}
		}
		if(!dnodeinserted) {
			data.add(data.size(), dnode);
		}
		
		int splitlocation;
		if(maxsize%2 == 0) {
			splitlocation = (maxsize)/2;
		}
		else {
			splitlocation = (maxsize+1)/2;
		}
				
		LeafNode right = new LeafNode(maxsize);
		for(int i = data.size()-splitlocation; i > 0; i--) {
			right.data.add(data.remove(splitlocation));
		}
		
		right.setNextNode(this.getNextNode());
		this.setNextNode(right);
		
		DataNode mid =  (DataNode) right.data.elementAt(0);

		this.propagate(mid, right);
	}

	public Node insert(DataNode dnode) {
		if(data.size() < maxsize-1) {
			boolean dnodeinserted = false;
			int i = 0;
			while(!dnodeinserted && i < data.size()) {
				if( ((DataNode)data.elementAt(i)).inOrder(dnode) ) {
					data.add(i,dnode);
					dnodeinserted = true;
				}
				i++;
			}
			if(!dnodeinserted) {
				data.add(data.size(), dnode);
			}
		}
		
		else {
			this.split(dnode);
		}
		
		return this.findRoot();
	}
}

class TreeNode extends Node {
	protected Vector<Node> pointer;
	
	@SuppressWarnings("unchecked") TreeNode(int x) {
		super(x);
		pointer = new Vector();
	}

	public Node getPointerTo(DataNode x) {
		int i = 0;
		boolean xptrfound = false;
		while(!xptrfound && i < data.size()) {
			if( ((DataNode)data.elementAt(i)).inOrder(x ) ) {
				xptrfound = true;
			}
			else {
				i++;				
			}

		}	
		
		return (Node) pointer.elementAt(i);
		
	}

	public Node getPointerAt(int index) {
		return (Node) pointer.elementAt(index);
	}

	protected void split(DataNode dnode, Node left, Node right) {
		int splitlocation, insertlocation = 0; 
		if(maxsize%2 == 0) {
			splitlocation = maxsize/2;
		}
		else {
			splitlocation = (maxsize+1)/2 -1;
		}
		
		boolean dnodeinserted = false;
		for(int i=0; !dnodeinserted && i < data.size(); i++) {
			if( ((DataNode)data.elementAt(i)).inOrder(dnode) ) {
				data.add(i,dnode);
				((TreeNode)this).pointer.remove(i);
				((TreeNode)this).pointer.add(i, left);
				((TreeNode)this).pointer.add(i+1, right);
				dnodeinserted = true;
                
				insertlocation = i;
			}
		}
		if(!dnodeinserted) {
            insertlocation = data.size();
			data.add(dnode);
			((TreeNode)this).pointer.remove(((TreeNode)this).pointer.size()-1);
			((TreeNode)this).pointer.add(left);
			((TreeNode)this).pointer.add(right);
            
		}
		
		DataNode mid = (DataNode) data.remove(splitlocation);
		
		TreeNode newright = new TreeNode(maxsize);
		
		for(int i=data.size()-splitlocation; i > 0; i--) {
			newright.data.add(this.data.remove(splitlocation));
			newright.pointer.add(this.pointer.remove(splitlocation+1));
		}
		newright.pointer.add(this.pointer.remove(splitlocation+1));		

        if(insertlocation < splitlocation) {
            left.setParent(this);
            right.setParent(this);
        }
        else if(insertlocation == splitlocation) {
            left.setParent(this);
            right.setParent(newright);
        }
        else {
            left.setParent(newright);
            right.setParent(newright);
        }
        
		this.propagate(mid, newright);
	}

	Node insert(DataNode dnode) {
		Node next = this.getPointerTo(dnode);
		
		return next.insert(dnode);
	}
}

class DataNode {
    private Integer data;
    
    DataNode() {
        data = null;
    }   
    public String toString() {
		return data.toString();
	}
	public DataNode(int x) {
        data = x;
    }
    public int getData() {
        return data.intValue();
    }   
    public boolean inOrder(DataNode dnode) {
        return (dnode.getData() <= this.data.intValue());
    }
}
