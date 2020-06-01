import java.util.ArrayList;

public class Twitter {
	
	//ADD YOUR CODE BELOW HERE
	private MyHashTable<String, ArrayList<Tweet>> byAuthor;
	private MyHashTable<String, ArrayList<Tweet>> byDate;
	private MyHashTable<String, String> stops;
	private ArrayList<Tweet> tweets;
	private ArrayList<String> stopWords;
	//ADD CODE ABOVE HERE 
	
	// O(n+m) where n is the number of tweets, and m the number of stopWords
	public Twitter(ArrayList<Tweet> tweets, ArrayList<String> stopWords) {
		//ADD YOUR CODE BELOW HERE
		this.tweets = tweets;
		this.byAuthor = new MyHashTable<String, ArrayList<Tweet>>(tweets.size()+1);
		this.byDate = new MyHashTable<String, ArrayList<Tweet>>(tweets.size()+1);
		this.stops = new MyHashTable<String, String>(stopWords.size()+1);
		for (Tweet t: tweets) {
			addTweet(t);
		}
		for (int i=0;i<stopWords.size();i++) {
			stops.put(stopWords.get(i).toLowerCase(), stopWords.get(i).toLowerCase());
		}
		//ADD CODE ABOVE HERE 
		}
	
	
    /**
     * Add Tweet t to this Twitter
     * O(1)
     */
	public void addTweet(Tweet t) {
		//ADD CODE BELOW HERE
		if (this.byAuthor.get(t.getAuthor()) == null) {
			ArrayList<Tweet> toAdd = new ArrayList<Tweet>();
			toAdd.add(t);
			this.byAuthor.put(t.getAuthor(), toAdd);
		}
		else {
			this.byAuthor.get(t.getAuthor()).add(t);
		}
		if (this.byDate.get(t.getDateAndTime().split(" ")[0]) == null) {
			ArrayList<Tweet> toAdd = new ArrayList<Tweet>();
			toAdd.add(t);
			this.byDate.put(t.getDateAndTime().split(" ")[0], toAdd);
		}
		else {
			this.byDate.get(t.getDateAndTime().split(" ")[0]).add(t);
		}
		//ADD CODE ABOVE HERE 
	}
	

    /**
     * Search this Twitter for the latest Tweet of a given author.
     * If there are no tweets from the given author, then the 
     * method returns null. 
     * O(1)  
     */
    public Tweet latestTweetByAuthor(String author) {
        //ADD CODE BELOW HERE
    	if (this.byAuthor.get(author) != null) {
    		Tweet cur = this.byAuthor.get(author).get(0);
    		for (int i = 1; i< this.byAuthor.get(author).size();i++) {
    			if (this.byAuthor.get(author).get(i).getDateAndTime().compareTo(cur.getDateAndTime()) > 0 ) {
    				cur = this.byAuthor.get(author).get(i);
    			}
    		}
    		return cur;
    	}
    	return null;
    	
        //ADD CODE ABOVE HERE 
    }


    /**
     * Search this Twitter for Tweets by `date' and return an 
     * ArrayList of all such Tweets. If there are no tweets on 
     * the given date, then the method returns null.
     * O(1)
     */
    public ArrayList<Tweet> tweetsByDate(String date) {
        //ADD CODE BELOW HERE
    	if (this.byDate.get(date) != null) {
    		return this.byDate.get(date);
    	}
    	return null;
    	
        //ADD CODE ABOVE HERE
    }
    
	/**
	 * Returns an ArrayList of words (that are not stop words!) that
	 * appear in the tweets. The words should be ordered from most 
	 * frequent to least frequent by counting in how many tweet messages
	 * the words appear. Note that if a word appears more than once
	 * in the same tweet, it should be counted only once. 
	 */
    public ArrayList<String> trendingTopics() {
        //ADD CODE BELOW HERE
    	/* the keys should be the words and the values should be the count. size is gonna be number of tweets */
    	MyHashTable<String, Integer> count = new MyHashTable<String, Integer>(this.tweets.size()+1);
    	for (Tweet t: tweets) {
    		ArrayList<String> words = getWords(t.getMessage());
    		for (int i=0;i <words.size();i++) {
    			String word = words.get(i).toLowerCase();
    			if (containsIgnoreCase(words, word, i) || this.stops.get(word) != null) {
    				continue;
    			}
    			Integer val = count.get(word);
    			if ( val != null ) {
    				count.put(word, val+1);
    			}
    			else {
    				count.put(word, 1);
    			}
    		}
    	}
    	ArrayList<String> toRet = MyHashTable.fastSort(count);
    	return toRet;
    	
        //ADD CODE ABOVE HERE    	
    }
    
    
    
    /**
     * An helper method you can use to obtain an ArrayList of words from a 
     * String, separating them based on apostrophes and space characters. 
     * All character that are not letters from the English alphabet are ignored. 
     */
    private static ArrayList<String> getWords(String msg) {
    	msg = msg.replace('\'', ' ');
    	String[] words = msg.split(" ");
    	ArrayList<String> wordsList = new ArrayList<String>(words.length);
    	for (int i=0; i<words.length; i++) {
    		String w = "";
    		for (int j=0; j< words[i].length(); j++) {
    			char c = words[i].charAt(j);
    			if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))
    				w += c;
    			
    		}
    		wordsList.add(w);
    	}
    	return wordsList;
    }
    private static boolean containsIgnoreCase(ArrayList<String> l, String s, int d) {
        for (int j=0;j<d;j++) {
              if (l.get(d).equalsIgnoreCase(l.get(j))) {
                return true;
              }
          }
          return false;
        }
}
