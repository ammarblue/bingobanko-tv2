import net.sourceforge.javaocr.ocrPlugins.mseOCR.CharacterRange;
import net.sourceforge.javaocr.ocrPlugins.mseOCR.OCRScanner;
import net.sourceforge.javaocr.ocrPlugins.mseOCR.TrainingImage;
import net.sourceforge.javaocr.ocrPlugins.mseOCR.TrainingImageLoader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * @author michael@familientoft.net
 */
public class BoardDownloader {
   private static JFrame frame = new JFrame();
   private static OCRScanner ocrScanner = new OCRScanner();

   public static void main(String[] args) throws Exception {
      frame.setVisible(true);
      initTraining(ocrScanner);
      Random random = new Random();

      BoardDownloader stripper = new BoardDownloader();
      for(int i=0;i<5000;i++) {
         try {
            stripper.http();
            long millis = (long)(random.nextDouble() * 10000) + 23000;
            System.out.println("Sleeping " + millis + " milliseconds");
            Thread.sleep(millis);
         } catch (Exception e) {
            System.out.println("Ignoring error from server : " + e.getMessage());
            Thread.sleep(60000);
         }
      }

      System.out.println("FINISHED");

      System.exit(0);
   }

   public BoardDownloader() {
   }

   private static BufferedImage stripImageForColors(BufferedImage image) {
      BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
      for (int x = 0; x < image.getWidth(); x++) {
         for (int y = 0; y < image.getHeight(); y++) {
            int rgb = image.getRGB(x, y);
            if (rgb == Color.BLACK.getRGB()) {
               resultImage.setRGB(x, y, Color.BLACK.getRGB());
            } else {
               resultImage.setRGB(x, y, Color.WHITE.getRGB());
            }
         }
      }
      return resultImage;
   }

   private void http() throws Exception {
      File rootDir = new File(GameApp.BINGO_DATA_DIR);
      URL url = new URL("http://bingobanko.tv2.dk/print/");
      URLConnection urlConnection = url.openConnection();
      urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.2.3) Gecko/20100401");

      InputStream urlIs = urlConnection.getInputStream();
      BufferedReader rd = new BufferedReader(new InputStreamReader(urlIs));
      StringBuffer sb = new StringBuffer();
      String line;
      while ((line = rd.readLine()) != null) {
         sb.append(line);
      }
      rd.close();
      urlIs.close();
      String s = sb.toString();

      int nextStart = 0;
      while(true) {
         String searchConstant = "<img src=\"/board/";
         int startPosition = s.indexOf(searchConstant, nextStart);
         if(startPosition==-1) {
            break;
         }
         int endPosition = s.indexOf("\"", startPosition+searchConstant.length());

         int realStart = startPosition + searchConstant.length();
         String picName = s.substring(realStart, endPosition);
         System.out.println("picName = " + picName);

         URL pictureURL = new URL("http://bingobanko.tv2.dk/board/" + picName);
         URLConnection connection = pictureURL.openConnection();
         BufferedImage image = null;
         try {
            InputStream inputStream = connection.getInputStream();
            image = ImageIO.read(inputStream);
            inputStream.close();
         } catch (IOException e) {
            image = null;
            // Ignore
         }

         if(image!=null) {
            BufferedImage resultImage = stripImageForColors(image);

            File targetDir = new File(rootDir, picName);
            targetDir.mkdirs();
            File targetFile = new File(targetDir, picName);
            ImageIO.write(image, "PNG", targetFile);

            Plade plade = execute(targetFile.getPath(), resultImage);
            File targetOcr = new File(targetDir, "data.txt");
            FileWriter writer = new FileWriter(targetOcr, false);

            for(int i=0;i<plade.getLineCount();i++) {
               ArrayList<Integer> list = plade.getLine(i);
               for (Integer integer : list) {
                  writer.append(" " + integer);
               }
               writer.append("\r\n");
            }
            writer.close();

            File targetOcrDat = new File(targetDir, "data.dat");
            FileOutputStream fileOutputStream = new FileOutputStream(targetOcrDat);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(plade);
            objectOutputStream.close();
            fileOutputStream.close();
         }

         nextStart = endPosition;
      }
   }

   private Plade execute(String fileTitle, BufferedImage resultImage) throws Exception {
      String text = ocrScanner.scan(resultImage, 0, 52, 0, 0, null);
      String[] lines = text.split("\n");
      Plade plade = new Plade(fileTitle);
      for (String line : lines) {
         ArrayList<Integer> numbers = new ArrayList<Integer>();
         String[] numberStrings = line.split(" ");
         for (String numberString : numberStrings) {
            String resultnumber = numberString.trim();
            if (resultnumber.length() != 0) {
               numbers.add(Integer.parseInt(resultnumber));
            }
         }
         if (numbers.size() != 0) {
            plade.addLine(numbers);
         }
      }

      return plade;
   }

   private static void initTraining(OCRScanner scanner) throws Exception {
      TrainingImageLoader loader = new TrainingImageLoader();
      HashMap<Character, ArrayList<TrainingImage>> hashMap = new HashMap<Character, ArrayList<TrainingImage>>();
      for (int i = 0; i < 10; i++) {
         String filename = GameApp.BINGO_TRAINING_DIR + "char" + i + ".png";
         loader.load(frame, filename, new CharacterRange('0' + i, '0' + i), hashMap);
      }
      scanner.addTrainingImages(hashMap);
   }
}
