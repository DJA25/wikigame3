import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
//concat
//contains***
//compareto
//endsWith
//equalsIgnoreCase
//indexOf
//


public class Main  {
    static int maxPrev = 5000;
//    static int maxForward = 500;
    static HashSet<String> toss = new HashSet<String>();


    static LinkedList<String> links = new LinkedList<>();
    static LinkedList<Integer> depths = new LinkedList<>();

    static LinkedList<String> backlinks = new LinkedList<>();
    static LinkedList<Integer> backdepths = new LinkedList<>();

    public static void main(String[] args) {
        toss.add("/wiki/Main_Page");
        toss.add("/wiki/help");
//        toss.add("Special:");

         String start = "https://en.wikipedia.org/wiki/27th_Infantry_Division_Savska";
         String search = "https://en.wikipedia.org/wiki/Zaje%C4%8Dar";

         if(!start.contains("/wiki/") || !search.contains("/wiki/") || !start.contains("wikipedia.org") || !search.contains("wikipedia.org")) {
             //add to button GUI
         }

         links.add(start);
         depths.add(0);

         backlinks.add(search);
         backdepths.add(0);

         int curD = 0;
         while(run) {
             System.out.println(curD);
             while (!depths.isEmpty() && depths.peek() <= curD) {
                 HtmlRead(links.poll(), depths.poll(), search, false);
             }
             if(path.containsKey(search)) {
                 System.out.println("From Forward Search Only");
                 ArrayList<String> result = new ArrayList<String>();
                 String cur = search;
                 while (!cur.equals(start)) {
                     result.add(cur);
                     cur = path.get(cur);
                 }
                 result.add(cur);
                 Collections.reverse(result);
                 for(String s: result) System.out.println(s);
                 break;
             }
             while (!backdepths.isEmpty() && backdepths.peek() <= curD) {
//                 System.out.println("Ran");
                 HtmlRead(backlinks.poll(), backdepths.poll(), start, true);
//                 System.out.println(path2.size());
             }

             if(path2.containsKey(start)) {
                 System.out.println("From Backward Search Only");
                 ArrayList<String> result = new ArrayList<String>();
                 String cur = start;
                 while (!cur.equals(search)) {
                     result.add(cur);
                     cur = path.get(cur);
                 }
                 result.add(cur);
                 for(String s: result) System.out.println(s);
                 break;
             }




             curD ++;

             for(String s: path.keySet()) {
                 if(path2.keySet().contains(s)) {
                     run = false;
                     System.out.println("From Bidirectional Search");
                     String cur = s;
                     ArrayList<String> result = new ArrayList<>();
                     while (!cur.equals(start)) {
                         result.add(cur);
                         cur = path.get(cur);
                     }
                     result.add(cur);
                     Collections.reverse(result);
                     cur = s;
                     while (!cur.equals(search)) {
                         result.add(cur);
                         cur = path2.get(cur);
                     }
                     result.add(search);
                     for(String s2: result) System.out.println(s2);
                     break;
                 }
             }
             if(curD>MAXDEPTH) {
                 System.out.println("Program Taking A While to Excecute Since Links Are Far Apart. Make Sure You Only Use Wikipedia Links. Please Wait Or Try Again Later.");
             }
             System.out.println("path: " + path.size());
             System.out.println("path2: " + path2.size());
         }

//            for(String s: path.keySet()) {
//                if(path2.keySet().contains(s)) {
//                    System.out.println("??????????");
//                    String cur = s;
//                    ArrayList<String> result = new ArrayList<>();
//                    while (!cur.equals(start)) {
//                        result.add(cur);
//                        cur = path.get(cur);
//                    }
//                    result.add(cur);
//                    Collections.reverse(result);
//                    cur = s;
//                    System.out.println("?????????????");
//                    while (!cur.equals(search)) {
//                        result.add(cur);
//                        cur = path2.get(cur);
//                    }
//                    result.add(search);
//                    for(String s2: result) System.out.println(s2);
//                    break;
//                }
//            }
//
//        else {
//            System.out.println("...");
//            ArrayList<String> result = new ArrayList<String>();
//            String cur = search;
//            while (!cur.equals(start)) {
//                result.add(cur);
//                cur = path.get(cur);
//            }
//            result.add(cur);
//            Collections.reverse(result);
//            for(String s: result) System.out.println(s);
//        }
    }
    public static HashMap<String, String> path = new HashMap<>(); // curlink to past link
    public static HashMap<String, String> path2 = new HashMap<>();
    public static int MAXDEPTH = 4;


    public static void HtmlRead(String link, int depth, String search, boolean back) {
        try {
            String link2;
            URL url;
            if(back) {
                link2 = "https://en.wikipedia.org/w/index.php?title=Special:WhatLinksHere/" + link.substring(link.indexOf("/wiki/")+6) + "&limit=" + maxPrev;
                url = new URL(link2);
            }
            else url = new URL(link);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(url.openStream())
            );
            String line;
            while ((line = reader.readLine()) != null) {
                parseLine(line, link, depth, search, back);
            }
        }
        catch(Exception e) {
        }

    }
    public static boolean run = true;
    public static void parseLine(String line, String pastlink, int depth, String search, boolean back) {
        if(!run) return;
        String[] starters = new String[]{"href='","href=\""};
        for(String s: starters) {
            if (line.contains(s)) {
//                System.out.println(line);
                int start = line.indexOf(s);
                char[] x = line.toCharArray();
                int end = x.length;
                int plus = s.length();
//                if(x[start+plus]=='/' || x[start+plus]=='#') continue;
                for(int i = start+plus; i<x.length; i++) {
                    if(x[i]=='\"' || x[i]=='\'' || x[i]==' ') {
                        end = i;
                        break;
                    }
                }
                String link = line.substring(start + plus, end);
                if(link.indexOf("/wiki/")!=0) {
//                    System.out.println(link);
                    return;
                }
                if(toss.contains(link)) return;
                if(link.contains(":")) return;
                link = "https://en.wikipedia.org" + link;
                if(link.equals(pastlink)) return;

                if(link.equals(search)) {
//                    System.out.println("FOUND");
                    if(back) path2.put(link, pastlink);
                    else path.put(link, pastlink);
                    run = false;
                    return;
                }

                if(!back && !path.containsKey(link) || back && !path2.containsKey(link)) {
                    if(back) {
                        path2.put(link, pastlink);
                        backdepths.add(depth+1);
                        backlinks.add(link);
                    }
                    else {
                        path.put(link, pastlink);
                        depths.add(depth+1);
                        links.add(link);
                    }
                }

                if(end==x.length) return;
                line = line.substring(end+1);
                parseLine(line, pastlink, depth, search, back);
                break;
            }

        }

    }


}