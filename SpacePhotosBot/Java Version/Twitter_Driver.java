//Name:  R5-06 
//Date:  12/15/18
//The Twitter API is at http://twitter4j.org

import twitter4j.*;       //set the classpath to lib\twitter4j-core-4.0.7.jar
import java.util.*;
import java.io.*;

public class Twitter_Driver{
   private static PrintStream consolePrint;
   public static void main (String []args) throws TwitterException, IOException{
      consolePrint = System.out; // this preserves the standard output so we can get to it later
      TJTwitter bot = new TJTwitter(consolePrint);
      bot.investigate();
   }        
}      
      
class TJTwitter {
   private Twitter twitter;
   private PrintStream consolePrint;
   private List<Status> statuses;
   private int numberOfTweets; 
   private List<String> terms;
  
   public TJTwitter(PrintStream console){
      // Makes an instance of Twitter - this is re-useable and thread safe.
      // Connects to Twitter and performs authorizations.
      twitter = TwitterFactory.getSingleton(); 
      consolePrint = console;
      statuses = new ArrayList<Status>();
      terms = new ArrayList<String>();
   }
   public List<String> getTerms()
   {
      return terms;
   }
   public int getNumberOfTweets()
   {
      return numberOfTweets;
   }
  
  
  
   public void tweetOut(String message) throws TwitterException, IOException
   {
      twitter.updateStatus(message);
   }

   public void fetchTweets(String handle) throws TwitterException, IOException
   {
      // Creates file for dedebugging purposes
      PrintStream fileout = new PrintStream(new FileOutputStream("tweets.txt")); 
      Paging page = new Paging (1,200);
      int p = 1;
      while (p <= 10)
      {
         page.setPage(p);
         statuses.addAll(twitter.getUserTimeline(handle,page)); 
         p++;        
      }
      numberOfTweets = statuses.size();
      fileout.println("Number of tweets = " + numberOfTweets);
   
   }   

   public void investigate() throws TwitterException
   {
      ImageGetter ig = new ImageGetter();
      try{
      ig.main(null);
      }
      catch(Exception e){
      
      }
      String imageName = ig.getImageLoc();
      String caption = ig.getImageCaption();
      File image = new File(imageName);
      StatusUpdate status = new StatusUpdate(caption);
      status.setMedia(image); // set the image to be uploaded here.
      twitter.updateStatus(status);
   }
}  


