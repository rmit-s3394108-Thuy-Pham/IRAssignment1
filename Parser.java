import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class Parser {

    private String input;
    private String docs[];
    private String words[];
    private String terms[];
    Map <Integer, String> map = new HashMap <Integer, String>(); //create a map for values
    private int docid; //added variable for id
    public String[] splitDocs()
    {
        String Str = new String(input);
        docs = Str.split("<DOC>");
        return docs;
    }

    public String [] splitString(){
        for (int i =0; i <docs.length; i++){
         String Str = new String(docs[i]);
         Str = removePunctuation(Str);
         Str = caseFolder(Str);

         words = Str.split(" ");
         for(String token : words)
             { System.out.println(token);}
         }
        return words;
    }

    public String caseFolder(String s){
        input = s.toLowerCase();
        return input;
    }

    public String removePunctuation(String s){
        String text;
        text = s.replaceAll("[^a-zA-z]", " ");
        input  = text;
        return input;
    }

       public Map findDocNo(){
        String test = input;
       
        Pattern p = Pattern.compile("<DOCNO> (\\S+) </DOCNO>", Pattern.MULTILINE);
        Matcher m = p.matcher(test);
           while (m.find()) {
              String docno = m.group(1);
              docid++;
             map.put(docid, docno); //adding the number and ID value into map
            }
           return map;
    }


    public String removeTags(){
        input = input.replaceAll("<.*>", "");
        return input;
    }

    public String ReadFile() throws IOException{
        FileReader instream = new FileReader("latimes-100");
        BufferedReader bufRead = new BufferedReader(instream);
        StringBuilder sb = new StringBuilder();
        String text = null;
        while ( (text = bufRead.readLine()) != null){
            input = text;
            findDocNo();
            splitDocs();
            splitString();

        }
        return input;
    }

}
