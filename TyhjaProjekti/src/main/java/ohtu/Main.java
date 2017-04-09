package ohtu;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import org.apache.http.client.fluent.Request;

public class Main {

    public static void main(String[] args) throws IOException {
        // vaihda oma opiskelijanumerosi seuraavaan, ÄLÄ kuitenkaan laita githubiin omaa opiskelijanumeroasi
        String studentNr = "123456789";
        if ( args.length>0) {
            studentNr = args[0];
        }

        String url = "http://ohtustats2017.herokuapp.com/students/"+studentNr+"/submissions";
        String courseUrl = "https://ohtustats2017.herokuapp.com/courses/1.json";

        String bodyText = Request.Get(url).execute().returnContent().asString();
        String courseBodyText = Request.Get(courseUrl).execute().returnContent().asString();
        
        System.out.println("json-muotoinen data:");
        System.out.println( bodyText );

        Gson mapper = new Gson();
        Submission[] subs = mapper.fromJson(bodyText, Submission[].class);
        Course c = mapper.fromJson(courseBodyText, Course.class);
        
        System.out.println("Oliot:");
        for (Submission submission : subs) {
            System.out.println("Viikko " + submission.getWeek() + ": tehtyjä tehtäviä yhteensä: " + submission.done().size()
                    + "(maksimi " + getAmountOfAssignments(submission.getWeek(), c) + ") " + 
                    " Tehdyt tehtävät: " + printDone(submission.done()) + " aikaa kului " + submission.getHours() + " tuntia ");
        }
        
        int totalhours = 0;
        int totaltasks = 0;
        
        for (Submission s : subs) {
            totalhours += s.getHours();
            totaltasks += s.done().size();
        }
        
        System.out.println("Yhteensä " + totaltasks + " tehtävää, " + totalhours + " tuntia.");
        


    }
    
    public static String printDone(List<Integer> list) {
        StringBuilder sb = new StringBuilder();
        for (Integer i : list) {
            sb.append(i);
            sb.append(" ");
        }
        return sb.toString();
    }
    
    public static String getAmountOfAssignments(Integer i, Course c) {
        switch (i) {
            case 1 : return c.getWeek1();
            case 2 : return c.getWeek2();
            case 3 : return c.getWeek3();
            case 4 : return c.getWeek4();
            case 5 : return c.getWeek5();
            case 6 : return c.getWeek6();
        }
        return null;
    }
    
}