import java.util.ArrayList;
import java.util.InputMismatchException;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class index
{
  public static void main(String[] args)throws IOException  {
    String sourcefile = "abc";
    String stoplistname = "stoplist";

    /* based on the number of command-line arguments to decide whether or not the program should print all content terms */
      if (args.length == 1)
        {
          sourcefile = args[0];
          parser(readFileandsplitDoc(sourcefile));
        }
      else if (args.length == 2)
        {
          sourcefile = args[1];
          for (int i = 0; i < (parser(readFileandsplitDoc(sourcefile))).size(); i++)
          {
            System.out.println((parser(readFileandsplitDoc(sourcefile))).get(i).toString());
          }
          System.out.println(returnMap(readFileandsplitDoc(sourcefile)));


        }
      else if (args.length == 4)
      {
        sourcefile = args[3];
        stoplistname = args[1];
        for (int i = 0; i < (removeStopWords(stoplistname, parser(readFileandsplitDoc(sourcefile)))).size(); i++)
        {
          System.out.println((removeStopWords(stoplistname, parser(readFileandsplitDoc(sourcefile)))).get(i).toString());
        }
        System.out.println(returnMap(readFileandsplitDoc(sourcefile)));
      }
      else if (args.length ==3)
      {
        sourcefile = args[2];
        stoplistname = args[1];
      }
      else
      System.out.println("The invocation might be in a wrong format");


}

    //method parser()
    public static ArrayList<String> parser(String[] docs)
    {
        ArrayList<String> words = new ArrayList<>();
        for  (String eachDoc : docs)
        {
          Pattern p = Pattern.compile("(?<=<HEADLINE>)([^\r]*)(?=</TEXT>)" );
          Matcher m = p.matcher(eachDoc);
          if (m.find())
          {
            String word = (((m.group(1).toString()).replaceAll("</HEADLINE>", "")).replaceAll("[^a-zA-z]", " ")).toLowerCase();
            String[] term = word.split(" ");
            List<String> temp = Arrays.asList(term);
            words.addAll(temp);
            //words.add(word);
          }
        }
        return words;
    }

    //method readfileandsplitDoc()
    public static String[] readFileandsplitDoc(String sourcefile)
    {
          String text;
          String[] docs = null;
          try
          {
          FileReader instream = new FileReader(sourcefile);
          BufferedReader bufRead = new BufferedReader(instream);
          StringBuilder sb = new StringBuilder();
          try{
          String inputLine = bufRead.readLine();

            while (inputLine != null)
            {
              sb.append(inputLine);
              sb.append("\n");
              inputLine = bufRead.readLine();
            }
            text = sb.toString();
            docs = text.split("<DOC>");
          }
          catch(IOException e)
          {
            System.out.println(e.getMessage());
          }
          }
         catch(FileNotFoundException fnfe)
          {
            System.out.println(fnfe.getMessage());
          }


        return docs;
    }

    //method return docnos
    public static Hashtable returnMap(String[] docs)
    {
          Hashtable<Integer, String> docnos = new Hashtable<Integer, String>();
          ArrayList<String> docnoslist = new ArrayList<>();
          for (String eachDoc : docs)
          {
            Pattern p = Pattern.compile("<DOCNO> (\\S+) </DOCNO>", Pattern.MULTILINE);
            Matcher m = p.matcher(eachDoc);
              if (m.find())
              {
                 String docno = m.group(1);
                 docnoslist.add(docno);
               }

          }


          //mapping the <DOCNO> into "map" file
          for (int i = 0; i < docnoslist.size(); i++)
          {
            docnos.put(i, (docnoslist.get(i)).toString());
          }
        return docnos;
    }

          //System.out.println(docnos);


    public static ArrayList<String> removeStopWords(String stoplistname, ArrayList<String> words)
    {
      ArrayList<String> textremovedstopwords = new ArrayList<>();
      String sCurrentLine;
      Hashtable<String, Integer> stoplist = new Hashtable<String, Integer>();

      try
      {
      FileReader fr = new FileReader(stoplistname);
      BufferedReader br = new BufferedReader(fr);
      int i = 0;
      try
      {
        while ((sCurrentLine = br.readLine()) != null)
        {
          stoplist.put(sCurrentLine.toLowerCase(), i);
          i++;
        }

        for (String term : words)
        {
          if (!(stoplist.containsKey(term)))
          {
              textremovedstopwords.add(term);
          }
        }
      }
      catch(IOException e)
      {
        System.out.println(e);
      }
    }


      catch(FileNotFoundException fnfe)
       {
         System.out.println(fnfe.getMessage());
       }


      return textremovedstopwords;

    }

    /*
    for (int i = 0; i < words.size(); i++)
    {
      System.out.println(words.get(i).toString());
    }
    */

    /*
    ArrayList<String> wordsList = new ArrayList<String>();
    String sCurrentLine;
    String[] stopwords = new String[2000];


    FileReader fr=new FileReader(stoplistname);
    BufferedReader br= new BufferedReader(fr);
    int k = 0;
    while ((sCurrentLine = br.readLine()) != null)
        {
            stopwords[k]=sCurrentLine.toLowerCase();
            k++;
        }



    for(String term : words)
    {
        String wordcompare = term.toLowerCase();

        if(!(stopwords.contains(wordcompare)))
        {
            wordsList.add(wordcompare);
            if (pFlag == true)
              {
                for (String str : wordsList)
                {
                  System.out.println(str + " ");
                }
              }

        }
    }
    /*

    for (String str : wordsList)
    {
        System.out.print(str+" ");
    }
    */

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
