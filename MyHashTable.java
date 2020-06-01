import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;


public class MyHashTable<K,V> implements Iterable<HashPair<K,V>>{
    // num of entries to the table
    private int numEntries;
    // num of buckets 
    private int numBuckets;
    // load factor needed to check for rehashing 
    private static final double MAX_LOAD_FACTOR = 0.75;
    // ArrayList of buckets. Each bucket is a LinkedList of HashPair
    private ArrayList<LinkedList<HashPair<K,V>>> buckets; 
    
    // constructor
    public MyHashTable(int initialCapacity) {
        // ADD YOUR CODE BELOW THIS
    	this.numBuckets = initialCapacity;
        this.numEntries = 0;
        this.buckets = new ArrayList<LinkedList<HashPair<K,V>>>();
        for (int i=0; i<numBuckets;i++) {
        	this.buckets.add(new LinkedList<HashPair<K,V>>());
        }
        //ADD YOUR CODE ABOVE THIS
    }
    
    public int size() {
        return this.numEntries;
    }
    
    public boolean isEmpty() {
        return this.numEntries == 0;
    }
    
    public int numBuckets() {
        return this.numBuckets;
    }
    
    /**
     * Returns the buckets variable. Useful for testing  purposes.
     */
    public ArrayList<LinkedList< HashPair<K,V> > > getBuckets(){
        return this.buckets;
    }
    
    /**
     * Given a key, return the bucket position for the key. 
     */
    public int hashFunction(K key) {
        int hashValue = Math.abs(key.hashCode())%this.numBuckets;
        return hashValue;
    }
    
    /**
     * Takes a key and a value as input and adds the corresponding HashPair
     * to this HashTable. Expected average run time  O(1)
     */
    public V put(K key, V value) {
        //  ADD YOUR CODE BELOW HERE
    	if ((double) this.numEntries+1 <= MyHashTable.MAX_LOAD_FACTOR*this.numBuckets) {
    	int index = hashFunction(key);
    	LinkedList<HashPair<K,V>> l = this.buckets.get(index);
    	if (l != null ) {
    		for (HashPair<K,V> pair: l) {
    			if (pair.getKey().equals(key)) {
    				V val = pair.getValue();
    				pair.setValue(value);
    				return val;
    			}
    		}
    	}
    	this.buckets.get(index).add(new HashPair<K,V>(key, value));
    	this.numEntries++;
    	return null;
    	}
    	else {
    		this.rehash();
    		return this.put(key, value);
    	}
        //  ADD YOUR CODE ABOVE HERE
    }
    
    
    /**
     * Get the value corresponding to key. Expected average runtime O(1)
     */
    
    public V get(K key) {
        //ADD YOUR CODE BELOW HERE
    	int index = hashFunction(key);
    	for (HashPair<K,V> pair: this.buckets.get(index)) {
				if (key.equals(pair.getKey())) {
					return pair.getValue();
				}
		}
    	return null;
    	
        //ADD YOUR CODE ABOVE HERE
    }
    
    /**
     * Remove the HashPair corresponding to key . Expected average runtime O(1) 
     */
    public V remove(K key) {
        //ADD YOUR CODE BELOW HERE
    	int index = hashFunction(key);
    	for (int j=0;j<this.buckets.get(index).size();j++) {
			if (key.equals(this.buckets.get(index).get(j).getKey())) {
				this.numEntries--;
				return this.buckets.get(index).remove(j).getValue();
			}
		}
    	return null;
    	
        //ADD YOUR CODE ABOVE HERE
    }
    
    
    /** 
     * Method to double the size of the hashtable if load factor increases
     * beyond MAX_LOAD_FACTOR.
     * Made public for ease of testing.
     * Expected average runtime is O(m), where m is the number of buckets
     */
    public void rehash() {
        //ADD YOUR CODE BELOW HERE
    	this.numBuckets *= 2;
    	ArrayList<LinkedList<HashPair<K,V>>> buck= new ArrayList<LinkedList<HashPair<K,V>>>();
    	for (int i=0;i<this.numBuckets;i++) {
    		buck.add(new LinkedList<>());
    	}
    	for (LinkedList<HashPair<K,V>> l:this.buckets) {
			for (HashPair<K,V> h: l) {
				buck.get(hashFunction((K)h.getKey())).add(h);
			}
		}
    	this.buckets = buck;
    	
        //ADD YOUR CODE ABOVE HERE
    }
    
    
    /**
     * Return a list of all the keys present in this hashtable.
     * Expected average runtime is O(m), where m is the number of buckets
     */
    
    public ArrayList<K> keys() {
        //ADD YOUR CODE BELOW HERE
    	
    	ArrayList<K> keys = new ArrayList<>();
    	for (int i=0;i<this.numBuckets;i++) {
			for (int j=0;j<this.buckets.get(i).size();j++) {
				keys.add(this.buckets.get(i).get(j).getKey());
			}
    	}
    	return keys;
    	
        //ADD YOUR CODE ABOVE HERE
    }
    
    /**
     * Returns an ArrayList of unique values present in this hashtable.
     * Expected average runtime is O(m) where m is the number of buckets
     */
    public ArrayList<V> values() {
        //ADD CODE BELOW HERE
        
    	MyHashTable<K,V> vals = new MyHashTable<K,V>(this.numEntries+1);
    	ArrayList<V> values = new ArrayList<>();
    	for (LinkedList<HashPair<K,V>> l:this.buckets) {
			for (HashPair<K,V>h:l) {
				V val = h.getValue();
				V already = vals.put((K)val, val);
				if (already == null) {
					values.add(val);
				}
			}
		}
    	return values;
    	
        //ADD YOUR CODE ABOVE HERE
    }
    
    
	/**
	 * This method takes as input an object of type MyHashTable with values that 
	 * are Comparable. It returns an ArrayList containing all the keys from the map, 
	 * ordered in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n^2), where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable<V>> ArrayList<K> slowSort (MyHashTable<K, V> results) {
        ArrayList<K> sortedResults = new ArrayList<>();
        for (HashPair<K, V> entry : results) {
			V element = entry.getValue();
			K toAdd = entry.getKey();
			int i = sortedResults.size() - 1;
			V toCompare = null;
        	while (i >= 0) {
        		toCompare = results.get(sortedResults.get(i));
        		if (element.compareTo(toCompare) <= 0 )
        			break;
        		i--;
        	}
        	sortedResults.add(i+1, toAdd);
        }
        return sortedResults;
    }
    
    
	/**
	 * This method takes as input an object of type MyHashTable with values that 
	 * are Comparable. It returns an ArrayList containing all the keys from the map, 
	 * ordered in descending order based on the values they mapped to.
	 * 
	 * The time complexity for this method is O(n*log(n)), where n is the number 
	 * of pairs in the map. 
	 */
    
    public static <K, V extends Comparable<V>> ArrayList<K> fastSort(MyHashTable<K, V> results) {
        //ADD CODE BELOW HERE
    	ArrayList <HashPair<K,V>> list = new ArrayList<>();
    	ArrayList<K> keys = new ArrayList<>();
    	for (HashPair<K, V> entry: results) {
    		list.add(entry);
    	}
    	ArrayList<HashPair<K,V>> sorted = mergeSort(list);
    	for (HashPair<K, V> h:sorted) {
    		keys.add((K)h.getKey());
    	}
    	return keys;
        //ADD CODE ABOVE HERE
    }
    
    private static <K, V extends Comparable<V>> ArrayList<HashPair<K,V>> mergeSort(ArrayList<HashPair<K,V>> list) {
    	ArrayList<HashPair<K,V>> left = new ArrayList<>();
    	ArrayList<HashPair<K,V>> right = new ArrayList<>();
    	if (list.size() == 1) {
    		return list;
    	}
    	else {
    		int mid = (list.size() -1) / 2;
    		for (int i=0;i<list.size();i++) {
    			if (i <= mid) {
    				left.add(list.get(i));
    			}
    			else {
    				right.add(list.get(i));
    			}
    		}
    		left = mergeSort(left);
    		right = mergeSort(right);
    		return merge(left, right);
    	}
    }

    private static <K, V extends Comparable<V>> ArrayList<HashPair<K,V>> merge(ArrayList<HashPair<K,V>> left, ArrayList<HashPair<K,V>> right) {
    	ArrayList<HashPair<K,V>> toRet = new ArrayList<>();
    	while (!left.isEmpty() && !right.isEmpty()) {
    		if (left.get(0).getValue().compareTo(right.get(0).getValue()) > 0) {
    			toRet.add(left.remove(0));
    		}
    		else {
    			toRet.add(right.remove(0));
    		}
    	}
    	while (!left.isEmpty()) {
    		toRet.add(left.remove(0));
    	}
    	while (!right.isEmpty()) {
    		toRet.add(right.remove(0));
    	}
    	return toRet;
    }

    
    
    
    @Override
    public MyHashIterator iterator() {
        return new MyHashIterator();
    }   
    
    private class MyHashIterator implements Iterator<HashPair<K,V>> {
        //ADD YOUR CODE BELOW HERE
    	ArrayList<HashPair<K,V>> pairs = new ArrayList<HashPair<K,V>>();
    	int i = 0;
        //ADD YOUR CODE ABOVE HERE
    	
    	/**
    	 * Expected average runtime is O(m) where m is the number of buckets
    	 */
        private MyHashIterator() {
            //ADD YOUR CODE BELOW HERE
        	for (LinkedList <HashPair<K,V>> buck: buckets) {
    			for (HashPair<K,V> h: buck) {
    				pairs.add(h);
    			}
        	}
            //ADD YOUR CODE ABOVE HERE
        }
        
        @Override
        /**
         * Expected average runtime is O(1)
         */
        public boolean hasNext() {
            //ADD YOUR CODE BELOW HERE
        	return (i < pairs.size());
            //ADD YOUR CODE ABOVE HERE
        }
        
        @Override
        /**
         * Expected average runtime is O(1)
         */
        public HashPair<K,V> next() {
            //ADD YOUR CODE BELOW HERE
        	if (!hasNext()) {
        		return null;
        	}
        	i += 1;
        	return pairs.get(i-1);
            //ADD YOUR CODE ABOVE HERE
        }
        
    }
}
