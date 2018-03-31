import java.util.ArrayList;
import java.util.InputMismatchException;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class index
{

//main method
  public static void main(String[] args)throws IOException  {
    String sourcefile = "abc";
    Boolean pFlag = false;
    String stoplistname = "stoplist";
    String text;
    String[] docs;



    if (args.length == 1)
      {
        sourcefile = args[0];
      }
    else if (args.length == 2)
      {
        sourcefile = args[1];
        pFlag = true;
      }
    else if (args.length == 4)
    {
      sourcefile = args[3];
      pFlag = true;
    }
    else if (args.length ==3)
    {
      sourcefile = args[2];
      stoplistname = args[1];
    }


    FileReader instream = new FileReader(sourcefile);
    BufferedReader bufRead = new BufferedReader(instream);
    StringBuilder sb = new StringBuilder();
    String inputLine = bufRead.readLine();
    while (inputLine != null)
    {
      sb.append(inputLine);
      sb.append("\n");
      inputLine = bufRead.readLine();
    }

    text = sb.toString();
    docs = text.split("<DOC>");

    ArrayList<String> docnos = new ArrayList<>();
    for (String eachDoc : docs)
    {

      Map<Integer, String> map = new TreeMap<Integer, String>();
      Pattern p = Pattern.compile("<DOCNO> (\\S+) </DOCNO>", Pattern.MULTILINE);
      Matcher m = p.matcher(eachDoc);
      if (m.find()) {
         String docno = m.group(1);
         docnos.add(docno);
       }


    }

    for (int i = 0; i < docnos.size(); i++)
    {
      System.out.println(docnos.get(i).toString());

    }


    ArrayList<String> words = new ArrayList<>();
    for  (String eachDoc : docs)
    {
      String pattern1 = "<DOCNO>";
      String pattern2 = "</DOCNO>";
      Pattern p = Pattern.compile("(?<=<HEADLINE>)([^\r]*)(?=</TEXT>)" );
      Matcher m = p.matcher(eachDoc);
      if (m.find())
      {
        String word = ((m.group(1).toString()).replaceAll("[^a-zA-z]", " ")).toLowerCase();
        words.add(word);
      }
    }
    for (int i = 0; i < words.size(); i++)
    {
      System.out.println(words.get(i).toString());
    }

    int k = 0;
    ArrayList<String> wordsList = new ArrayList<String>();
    String sCurrentLine;
    String[] stopwords = new String[2000];


    FileReader fr=new FileReader(stoplistname);
    BufferedReader br= new BufferedReader(fr);
    while ((sCurrentLine = br.readLine()) != null){
            stopwords[k]=sCurrentLine.toLowerCase();
            k++;
        }


    for(String term : words)
    {
        String wordCompare = term.toLowerCase();
        List<String> list = Arrays.asList(stopwords);
        if(!list.contains(wordCompare))
        {
            wordsList.add(wordCompare);

        }
    }

    for (String str : wordsList)
    {
        System.out.print(str+" ");
    }
  }
}




/*    for (int i = 0; i < words.size(); i++)
    {
      String preparedwords =((words.get(i).toString()).replaceAll("[^a-zA-z]", " ")).toLowerCase();
    }

      System.out.println(preparedwords);

/*    //docnos is an Array of Strings atm
    for (int i = 0; i < docnos.size(); i++)
    {
      int docID = i;
      map.put(docID, (docnos.get(i)).toString());
    }

    for (Map<Integer, String> eachDocNo : map)
    {
      System.out.println((eachDocNo.getKey())+ "\t" + (eachDocNo.getValue()));
    }

*/








//method to remove stopping
