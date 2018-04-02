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
    String stoplistname = "abc";
    String lexicon = "lexicon";
    String invlists = "invlists";
    String map = "map";
    PrintWriter pwL = new PrintWriter(new BufferedWriter(new FileWriter(lexicon)));
    PrintWriter pwI = new PrintWriter(new BufferedWriter(new FileWriter(invlists)));
    PrintWriter pwM = new PrintWriter(new BufferedWriter(new FileWriter(map)));
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
            String s = (parser(readFileandsplitDoc(sourcefile))).get(i).toString();
            if (!(s.isEmpty()))
              {System.out.println(s);}
          }

        }
      else if (args.length == 4)
      {
        sourcefile = args[3];
        stoplistname = args[1];
        for (int i = 0; i < (removeStopWords(stoplistname, parser(readFileandsplitDoc(sourcefile)))).size(); i++)
        {
          String s = (removeStopWords(stoplistname, parser(readFileandsplitDoc(sourcefile)))).get(i).toString();
          if (!(s.isEmpty()))
          {
          System.out.println(s);
          }
        }
        ArrayList<String> testing = removeStopWords(stoplistname, parser(readFileandsplitDoc(sourcefile)));
        Hashtable lexiconTable = createLexicon(testing);
        for (Object key : lexiconTable.keySet())
        {
          pwL.println(key + "\t" + lexiconTable.get(key));
        }

        for (Object key : lexiconTable.keySet())
        {
          String docFre = Integer.toBinaryString(countDocFre((key.toString()), readFileandsplitDoc(sourcefile)));
          pwI.print(docFre);
          Hashtable invertedlistTable = returnDocIDandInDocFre(key.toString(), readFileandsplitDoc(sourcefile));
          for (Object docID : invertedlistTable.keySet())
            {
            int eachdocID = (int)docID;
            String withinDocFrequen = Integer.toBinaryString((int)(invertedlistTable.get(eachdocID)));
            pwI.print( Integer.toBinaryString(eachdocID));
            pwI.print(withinDocFrequen);
            }
        }

        pwM.println(returnMap(readFileandsplitDoc(sourcefile)));


        pwL.close();
        pwI.close();
        pwM.close();
        //System.out.println(returnMap(readFileandsplitDoc(sourcefile)));

      }
      else if (args.length ==3)
      {
        sourcefile = args[2];
        stoplistname = args[1];
        ArrayList<String> testing = removeStopWords(stoplistname, parser(readFileandsplitDoc(sourcefile)));
        Hashtable lexiconTable = createLexicon(testing);
        for (Object key : lexiconTable.keySet())
        {
          pwL.println(key + "\t" + lexiconTable.get(key));
        }

        for (Object key : lexiconTable.keySet())
        {
          String docFre = Integer.toBinaryString(countDocFre((key.toString()), readFileandsplitDoc(sourcefile)));
          pwI.print(docFre);
          Hashtable invertedlistTable = returnDocIDandInDocFre(key.toString(), readFileandsplitDoc(sourcefile));
          for (Object docID : invertedlistTable.keySet())
            {
            int eachdocID = (int)docID;
            String withinDocFrequen = Integer.toBinaryString((int)(invertedlistTable.get(eachdocID)));
            pwI.print( Integer.toBinaryString(eachdocID));
            pwI.print(withinDocFrequen);
            }
        }

        pwM.println(returnMap(readFileandsplitDoc(sourcefile)));


        pwL.close();
        pwI.close();
        pwM.close();

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
            String word = (((m.group(1).toString()).replaceAll("<.*>", "")).replaceAll("[^a-zA-z]", " ")).toLowerCase();
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
            docnos.put(i+1, (docnoslist.get(i)).toString());
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


  // check if a term appear in a doc or not and return the document frequency (ft)
  public static int countDocFre(String termcompare, String[] docs)
  {
    int count =0;
    for (String eachdoc: docs)
    {
      String[] terms = eachdoc.split(" ");
        for (int i =0; i < terms.length; i++)
        {
          if ((terms[i].toLowerCase().contains(termcompare)))
          {
            count= count + 1;
            break;
          }
        }
    }
    return count;
  }

  //method return docID and the within document frequency
  public static Hashtable returnDocIDandInDocFre(String termcompare, String[] docs)
  {
    Hashtable<Integer, Integer> docIDandInDocFre = new Hashtable<Integer, Integer>();
    for (int m=0; m < docs.length; m++)
    {
      String[] terms = docs[m].split(" ");
      int inDocFre= 0;
      int docID = 0;
        for (int i =0; i< terms.length; i++)
        {
          if ((terms[i].toLowerCase().contains(termcompare)))
          {
            docID = m;
            inDocFre = inDocFre + 1;
          }
        }
      if (docID != 0)
      {
      docIDandInDocFre.put(docID, inDocFre);
      }
    }
    return docIDandInDocFre;
  }

  //method to create lexicon
  public static Hashtable createLexicon(ArrayList<String> arrayStr)
  {
    Hashtable<String, Integer> lexicon = new Hashtable<String, Integer>();
    int n =0;
    for (int i =0; i < arrayStr.size(); i++)
    {
      String key = (arrayStr.get(i)).toString();

      if (key.length() >0 && (!key.isEmpty()))
        {
          if (!(lexicon.containsKey(key)))
          {
            lexicon.put(key, n);
            n ++;
          }
        }
    }
    return lexicon;
  }


  //method to create invertedlist


}
