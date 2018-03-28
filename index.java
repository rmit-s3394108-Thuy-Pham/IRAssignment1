import java.util.ArrayList;
import java.util.InputMismatchException;
import java.io.*;
import java.util.*;
import java.util.List;

public class index
{
  public static void main(String[] args){

try{

  int k = 0;
  ArrayList<String> wordsList = new ArrayList<String>();
  String sCurrentLine;
  String[] stopwords = new String[2000];
  FileReader fr=new FileReader("stoplist");
  BufferedReader br= new BufferedReader(fr);
  while ((sCurrentLine = br.readLine()) != null){
            stopwords[k]=sCurrentLine.toUpperCase();
            k++;
        }

    String s="The onset of the new Gorbachev policy of glasnost, commonly mistranslated as openness but closer in connotation to candor or publicizing, has complicated the task of Soviet secret-keepers and has allowed substantial new Western insights into Soviet society.";
    String[] words = s.split(" ");


    for(String word : words)
    {
        String wordCompare = word.toUpperCase();

        List<String> list = Arrays.asList(stopwords);
        if(!list.contains(wordCompare))
        {
            wordsList.add(word);

        }
    }

    for (String str : wordsList){
        System.out.print(str+" ");
    }

}

catch(Exception ex){
        System.out.println(ex);
      }

}
}
