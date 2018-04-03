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
    /* based on the number of command-line arguments
      to decide whether or not the program should print all content terms */
      //case1: no stopping no prinitng
      if (args.length == 1) // invocation has the format as: java index latimes
        {
          sourcefile = args[0];
          parser(readFileandsplitDoc(sourcefile));
        }
      //case2: no stopping and printing enable
      else if (args.length == 2) // invocation has the format as: java index -p latimes
        {
          sourcefile = args[1];
          ArrayList<String> parsedText = parser(readFileandsplitDoc(sourcefile));
          for (int i = 0; i < parsedText.size(); i++)
          {
            String s = parsedText.get(i).toString();
            if (!(s.isEmpty()))
              {System.out.println(s);}
          }

        }
        //case3: both stopping and printing enable
      else if (args.length == 4)
      {
        sourcefile = args[3];
        stoplistname = args[1];
        ArrayList<String> parsedandstoppedText =  removeStopWords(stoplistname, parser(readFileandsplitDoc(sourcefile)));
        for (int i = 0; i < parsedandstoppedText.size(); i++)
        {
          String s = parsedandstoppedText.get(i).toString();
          if (!(s.isEmpty()))
          {
          System.out.println(s);
          }
        }
      }
      //case4: stopping enable, no printing
      else if (args.length ==3)
      {
        sourcefile = args[2];
        stoplistname = args[1];
        removeStopWords(stoplistname, parser(readFileandsplitDoc(sourcefile)));
      }
      else
      System.out.println("The invocation might be in a wrong format");

      /* INDEXING MODULE*/
      String lexicon = "lexicon";
      String invlists = "invlists";
      String map = "map";
      PrintWriter pwL = new PrintWriter(new BufferedWriter(new FileWriter(lexicon)));
      PrintWriter pwI = new PrintWriter(new BufferedWriter(new FileWriter(invlists)));
      PrintWriter pwM = new PrintWriter(new BufferedWriter(new FileWriter(map)));
      ArrayList<String> parsedandstoppedTerms = removeStopWords(stoplistname, parser(readFileandsplitDoc(sourcefile)));
      Hashtable<String, Integer> lexiconTable = createLexicon(parsedandstoppedTerms);

      int fileoffsetpostion =0;
      for (Object key : lexiconTable.keySet())
      {
        String eachPosting = new String();
        String docFre = Integer.toBinaryString(countDocFre((key.toString()), readFileandsplitDoc(sourcefile)));
        int numBits = 32;
        docFre = docFre.substring((docFre.length() - numBits) >= 0 ? (docFre.length() - numBits) : 0);
        eachPosting = eachPosting.concat(docFre);
        Hashtable<Integer, Integer> docIDandInDocFreTable = returnDocIDandInDocFre(key.toString(), readFileandsplitDoc(sourcefile));
        ArrayList<String> listOfDocIdandInDocFre = new ArrayList<>();
        for (Object docID : docIDandInDocFreTable.keySet())
          {
          int eachdocIDinInt = (int)docID;
          String withinDocFrequen = Integer.toBinaryString((int)(docIDandInDocFreTable.get(eachdocIDinInt)));
          withinDocFrequen = withinDocFrequen.substring((withinDocFrequen.length() - numBits) >= 0 ? withinDocFrequen.length() - numBits : 0);
          String eachdocID = Integer.toBinaryString(eachdocIDinInt);
          eachdocID = eachdocID.substring((eachdocID.length() - numBits) >= 0 ? eachdocID.length() - numBits : 0);
          listOfDocIdandInDocFre.add(eachdocID);
          listOfDocIdandInDocFre.add(withinDocFrequen);
          //pwI.print( Integer.toBinaryString(eachdocID));
          //pwI.print(withinDocFrequen);
          }
        for (int i =0; i < listOfDocIdandInDocFre.size(); i++)
        {
          eachPosting = eachPosting.concat(listOfDocIdandInDocFre.get(i).toString());
        }
        lexiconTable.put(key.toString(), fileoffsetpostion);
        fileoffsetpostion = fileoffsetpostion + 1 + listOfDocIdandInDocFre.size();
        pwI.print(eachPosting); // print to the invlist file
      }

      // print to the lexicon file
      for (Object key : lexiconTable.keySet())
      {
        pwL.println(key + "\t" + lexiconTable.get(key));
      }
      // print to the map file
      pwM.println(returnMap(readFileandsplitDoc(sourcefile)));


      pwL.close();
      pwI.close();
      pwM.close();


}
      //method to create lexicon
      public static Hashtable<String, Integer> createLexicon(ArrayList<String> arrayStr)
      {
        Hashtable<String, Integer> lexicon = new Hashtable<>();
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
      // method check if a term appear in a doc or not and return the document frequency (ft)
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
      public static Hashtable<Integer, Integer> returnDocIDandInDocFre(String termcompare, String[] docs)
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


    //method removeStopwords
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





  //method to create invertedlist


}
