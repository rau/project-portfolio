// Name:  R5-06
// Date:  12/15/18

import java.util.List;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Date;
import java.util.*;

public class Twitter_Warmup
{
   public static void main (String []args) throws IOException
   {      
      TJTwitter2 twitter = new TJTwitter2();
      
      //  testing remove punctuation
      String s1 = "abcd?";
      String s2 = "!abc$d";
      String s3 = "ab:cd..";
      String s4 = "abc'd";
      System.out.println( s1 + " without puncutation is " + twitter.removePunctuation(s1) );
      System.out.println( s2 + " without puncutation is " + twitter.removePunctuation(s2) );
      System.out.println( s3 + " without puncutation is " + twitter.removePunctuation(s3) );
      System.out.println( s4 + " without puncutation is " + twitter.removePunctuation(s4) );
      
      System.out.println();
      
      String f1 = "story.txt";
      String f2 = "test.txt";
      System.out.println("For the file: " + f1);
      twitter.queryHandle("story.txt");
      System.out.println("Most popular word: " + twitter.mostPopularWord());
      System.out.println("Frequency: " + twitter.getFrequencyMax());
      System.out.println();
      
      System.out.println("For the file: " + f2);
      twitter.queryHandle("test.txt");
      System.out.println("Most popular word: " + twitter.mostPopularWord());
      System.out.println("Frequency: " + twitter.getFrequencyMax());     
   }             
}        

class TJ_Status2
{
   private String text;
   
   public TJ_Status2(String s)
   {
      text = s;
   }
   public String getText()
   {
      return text;
   }   
}
      
class TJTwitter2 
{
   private List<TJ_Status2> statuses;
   private int numberOfTweets; 
   private List<String> terms;
   private String popularWord;
   private int frequencyMax;
   
   public TJTwitter2() throws IOException
   {
      statuses = new ArrayList<TJ_Status2>();
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
   
   public String getMostPopularWord()
   {
      return popularWord;
   }
   
   public int getFrequencyMax()
   {
      return frequencyMax;
   }
   
   @SuppressWarnings("unchecked")
   public void queryHandle(String handle)throws IOException   
   {
      statuses.clear();
      terms.clear();
      fetchTweets(handle);
      System.out.println("Number of tweets: " + getNumberOfTweets());
      splitIntoWords(); 
      System.out.println("All the words: " + terms);
      removeCommonEnglishWords();
      System.out.println("Remove common words: " +terms);
      sortAndRemoveEmpties();
      System.out.println("Sorted: " + terms);
      mostPopularWord();
   }
    
   /** 
    * This method reads a file of tweets and 
    * stores them in an arrayList of TJ_Status2 objects.  
    * Populates statuses.
    * @param String  the text file
    */
   public void fetchTweets(String handle) throws IOException
   {
      Scanner scan = new Scanner(new File(handle));
      while(scan.hasNext())
         statuses.add(new TJ_Status2(scan.nextLine()));
      numberOfTweets = statuses.size();  
   }   

   /** 
    * This method takes each status and splits them into individual words.   
    * Remove punctuation by calling removePunctuation, then store the word in terms.  
    */
   public void splitIntoWords()
   {
      for(TJ_Status2 yeet: statuses){
         String temp = yeet.getText();
         temp.replaceAll("  ", " ");
         String[] words = temp.split(" ");
         for(String a: words){
            terms.add(removePunctuation(a));
         }
            
      }
      
   }

   /** 
    * This method removes common punctuation (but not apostrophes) from each individual word.
    * This method removes empty strings.
    * This method changes everything to lower case.
    * Consider reusing code you wrote for a previous lab.     
    * Consider if you want to remove the # or @ from your words. Could be interesting to keep (or remove).
    * @ param String  the word you wish to remove punctuation from
    * @ return String the word without any punctuation, all lower case  
    */
   public String removePunctuation( String s )
   {
      
      return s.replaceAll("[^a-zA-Z0-9-’']", "").replaceAll("  ", "").toLowerCase();    
   }

   /** 
    * This method removes common English words from the list of terms.
    * Remove all words found in commonWords.txt  from the argument list.    
    * The count will not be given in commonWords.txt. You must count the number of words in this method.  
    * This method should NOT throw an excpetion.  Use try/catch.   
    */
   @SuppressWarnings("unchecked")
   public void removeCommonEnglishWords()
   {  
      try{
         Scanner s = new Scanner(new File("commonWords.txt"));
         while(s.hasNextLine()){
            terms.removeAll(Collections.singleton(s.next().toLowerCase()));
         } 
      }
      catch(Exception e){
         System.out.println("oops");
      }
      
      
   }

   /** 
    * This method sorts the words in terms in alphabetically (and lexicographic) order.  
    * You should use your sorting code you wrote earlier this year.
    * Remove all empty strings while you are at it.  
    */
   @SuppressWarnings("unchecked")
   public void sortAndRemoveEmpties()
   {
      String[] termsComp = new String[terms.size()];
      for(int i = 0; i < termsComp.length; i++){
         termsComp[i] = terms.get(i);
      }
      
      sort(termsComp);
       
      ArrayList<String> termsTemp = new ArrayList<String>();
      
      for(int i = 0; i < termsComp.length; i++)
         if(!termsComp[i].equals(""))
            termsTemp.add(termsComp[i]);
      
      terms = termsTemp;
      
      
   }
   
   private static void sort(String[] array)
   {
      for(int i = array.length-1; i >= 0; i--){
         swap(array, findMax(array, i+1), i);  
      }
   }
   
   @SuppressWarnings("unchecked")
   public static int findMax(String[] array, int upper)
   {
      int maxInd = 0;
      for(int i = 0; i < upper; i++)
         if(array[i].compareTo(array[maxInd]) > 0)
            maxInd = i;
      return maxInd;
   }
   
   public static void swap(String[] array, int a, int b)
   {
      String temp = array[a];
      array[a] = array[b];
      array[b] = temp;   
   }
  
   /** 
    * This method returns the most common word from terms.    
    * Consider case - should it be case sensitive?  The choice is yours.
    * @return String the word that appears the most times
    * @post will popopulate the frequencyMax variable with the frequency of the most common word 
    */
   @SuppressWarnings("unchecked")
   public String mostPopularWord()
   {
      HashMap<String, Integer> words = new HashMap<>();
      
      for(String yeet:terms){
         if(words.containsKey(yeet))
            words.put(yeet, words.get(yeet)+1);
         else
            words.put(yeet, 1);
      }
       
      int max = 0;
      String maxPartner = "";
      for(String temp:words.keySet()){
         if(words.get(temp) > max){
            max = words.get(temp);
            maxPartner = temp;
         }
      }
      
      frequencyMax = max;
      return maxPartner;    
   }
}  

/******************************** Sample output
 
 abcd
 abcd
 abcd
 abc'd
 
 Number of tweets = 28
 All the words: [as, a, 20-year-old, pfc, in, the, air, force, oct, 27, 1949, was, a, day, , i'll, always, remember, i, was, stationed, at, chanute, field, illinois, after, finishing, basic, training, at, sheppard, air, force, base, in, texas, i, was, transferred, to, chanute, to, attend, aircraft, , engine, and, general, aircraft, training, while, on, barracks, cleanup, duty, i, found, a, copy, of, the, , vancouver, sun, newspaper, from, , british, columbia, the, front-page, article, was, about, the, pacific, national, exhibition, beauty, contest, with, a, photo, of, the, winner, miss, vancouver, the, article, also, listed, the, 11, other, contestants, and, the, cities, they, represented, well, , i, got, the, bright, idea, of, writing, a, letter, to, the, winner, hoping, to, get, some, mail, in, return, since, i, had, been, away, from, home, for, almost, a, year, the, highlight, of, my, day, was, mail, call, i, wrote, to, two, other, contestants, as, well, but, had, only, their, , cities, to, use, for, the, address, i, was, shocked, when, i, got, return, letters, from, all, three, contestants, i, was, very, impressed, with, the, letter, from, miss, port, moody, kay, ronco, and, we, began, writing, regularly, by, this, time, , i, had, finished, the, tech, school, programs, and, was, transferred, to, a, base, in, omaha, nebraska, , kay, and, i, continued, to, write, after, seven, months, i, was, made, a, crew, member, on, a, b-29, bomber, , scheduled, to, fly, to, seattle, washington, for, modification]
 Remove common words: [20-year-old, pfc, air, force, oct, 27, 1949, day, , i'll, always, remember, stationed, chanute, field, illinois, after, finishing, basic, training, sheppard, air, force, base, texas, transferred, chanute, attend, aircraft, , engine, general, aircraft, training, while, barracks, cleanup, duty, found, copy, , vancouver, sun, newspaper, , british, columbia, front-page, article, pacific, national, exhibition, beauty, contest, photo, winner, miss, vancouver, article, also, listed, 11, other, contestants, cities, represented, well, , got, bright, idea, writing, letter, winner, hoping, mail, return, since, away, home, almost, year, highlight, day, mail, call, wrote, other, contestants, well, only, , cities, address, shocked, got, return, letters, three, contestants, very, impressed, letter, miss, port, moody, kay, ronco, began, writing, regularly, time, , finished, tech, school, programs, transferred, base, omaha, nebraska, , kay, continued, write, after, seven, months, made, crew, member, b-29, bomber, , scheduled, fly, seattle, washington, modification]
 Sorted: [11, 1949, 20-year-old, 27, address, after, after, air, air, aircraft, aircraft, almost, also, always, article, article, attend, away, b-29, barracks, base, base, basic, beauty, began, bomber, bright, british, call, chanute, chanute, cities, cities, cleanup, columbia, contest, contestants, contestants, contestants, continued, copy, crew, day, day, duty, engine, exhibition, field, finished, finishing, fly, force, force, found, front-page, general, got, got, highlight, home, hoping, i'll, idea, illinois, impressed, kay, kay, letter, letter, letters, listed, made, mail, mail, member, miss, miss, modification, months, moody, national, nebraska, newspaper, oct, omaha, only, other, other, pacific, pfc, photo, port, programs, regularly, remember, represented, return, return, ronco, scheduled, school, seattle, seven, sheppard, shocked, since, stationed, sun, tech, texas, three, time, training, training, transferred, transferred, vancouver, vancouver, very, washington, well, well, while, winner, winner, write, writing, writing, wrote, year]
 Most popular word: contestants
 Frequency: 3
 
 Number of tweets = 6
 All the words: [this, is, a, test, to, check, mia, if, mia, it's, working, or, a, a, a, not, mia]
 Remove common words: [test, check, mia, mia, it's, working, mia]
 Sorted: [check, it's, mia, mia, mia, test, working]
 Most popular word: mia
 Frequency: 3
 

*********************************************************/
