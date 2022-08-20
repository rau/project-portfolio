import java.util.*;
import java.lang.*;
import java.io.*;
import java.awt.*;
import java.net.*;
import org.jsoup.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class ImageGetter{
   public static void main(String[] args) throws Exception {
      String imageUrl = getImageUrl();
      String destinationFile = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
      saveImage(imageUrl, destinationFile);
   }

   public static void saveImage(String imageUrl, String destinationFile) throws IOException {
      URL url = new URL(imageUrl);
      InputStream is = url.openStream();
      OutputStream os = new FileOutputStream(destinationFile);
   
      byte[] b = new byte[2048];
      int length;
   
      while ((length = is.read(b)) != -1) {
         os.write(b, 0, length);
      }
      
      is.close();
      os.close();
   }
   
   public static String getImageUrl(){
      Document document;
		try {
			//Get Document object after parsing the html from given url.
			document = Jsoup.connect("https://apod.nasa.gov/apod/astropix.html").get();
         
         Element image = document.select("img").first();
         String url = image.absUrl("src");
         return url;

		} 
      catch (IOException e) {
			e.printStackTrace();
		}
      
      return "";
   }
   
   public static String getImageCaption(){
      Document document;
		try {
			//Get Document object after parsing the html from given url.
			document = Jsoup.connect("https://apod.nasa.gov/apod/astropix.html").get();
         
         Element cap = document.select("b").first();
         String caption = cap.text();
         caption = caption + " @NASA @NASAHQPHOTO";
         return caption;
         
		} 
      catch (IOException e) {
			e.printStackTrace();
		}
      
      return "";
   }
   
   public String getImageLoc(){
      String imageUrl = getImageUrl();
      String destFileLast = imageUrl.substring(imageUrl.lastIndexOf("/")+1);

      return destFileLast;
   }
}