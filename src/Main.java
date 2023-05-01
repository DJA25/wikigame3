import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
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
    static int maxPrev = 5;
    static int maxForward = 5;
    static HashSet<String> toss = new HashSet<String>();
    static JTextArea bottom;

    static LinkedList<String> links = new LinkedList<>();
    static LinkedList<Integer> depths = new LinkedList<>();

    static LinkedList<String> backlinks = new LinkedList<>();
    static LinkedList<Integer> backdepths = new LinkedList<>();
//    static String start, search;

    public static void main(String[] args) {

        JFrame mf = new JFrame("WikiGame");
        mf.setSize(1000, 800);
        mf.setLayout(new GridLayout(2, 1));
        JPanel top = new JPanel(new BorderLayout());
//        JPanel south = new JPanel(new GridLayout(2, 1));

        JButton start = new JButton("Go!");
//        JPanel splitter = new JPanel(new GridLayout(1, 2));
//        JButton label = new JButton("Select Random Wikipedia Links");
//        JTextField depth = new JTextField();
//        depth.setText("1");
//        splitter.add(label);
//        splitter.add(depth);
//        south.add(label);
//        south.add(start);

        top.add(start, BorderLayout.SOUTH);
        top.add(new JLabel("Start Link:", SwingConstants.CENTER), BorderLayout.NORTH);
        JPanel split = new JPanel(new GridLayout(2,1));
        JTextArea link = new JTextArea();
        link.setLineWrap(true);
        JPanel searcharea = new JPanel(new BorderLayout());
        JTextArea searc = new JTextArea();
        searcharea.add(searc, BorderLayout.CENTER);
        searcharea.add(new JLabel("Searching For:", SwingConstants.CENTER), BorderLayout.NORTH);
        split.add(link);
        split.add(searcharea);
        top.add(split, BorderLayout.CENTER);
        mf.add(top);

         bottom = new JTextArea();
        bottom.setEditable(false);
        bottom.setLineWrap(true);

        JScrollPane scroll = new JScrollPane(bottom);
//        scroll.setVerticalScrollBar(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//        scroll.add(bottom);
        mf.add(scroll);

        mf.setVisible(true);

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                run = true;
                bottom.setText("");
                String start = link.getText();
                String search = searc.getText();
                if(!start.contains("/wiki/") || !search.contains("/wiki/") || !start.contains("wikipedia.org") || !search.contains("wikipedia.org")) {
                    bottom.setText("Invalid wikipedia link. Try again.");
                }
                try {
                    URL url = new URL(start);
                    url = new URL(search);
                    bottom.setText("Please Wait. Calculating Result.");
                    search(start, search);
                }
                catch(Exception e2) {
                    bottom.setText("Invalid wikipedia link. Try again.");
                }
            }
        });




        toss.add("/wiki/Main_Page");
        toss.add("/wiki/help");
//        toss.add("Special:");

//         String start = "https://en.wikipedia.org/wiki/Maria_Pechnikova";
//         String search = "https://en.wikipedia.org/wiki/Crested_honey_buzzard";



//         search(start, search);


    }

    public static void search(String start, String search) {
        links.clear();
        depths.clear();
        backlinks.clear();
        backdepths.clear();

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
                bottom.setText("");
                run = false;
                System.out.println("From Forward Search Only");
                ArrayList<String> result = new ArrayList<String>();
                String cur = search;
                while (!cur.equals(start)) {
                    result.add(cur);
                    cur = path.get(cur);
                }
                result.add(cur);
                Collections.reverse(result);
                for(String s: result) bottom.append(s);
                break;
            }
            for(String s: path.keySet()) {
                if(path2.keySet().contains(s)) {
                    bottom.setText("");
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
                    for(String s2: result) bottom.append(s2 + "\n");
                    cur = path2.get(s);
                    while (!cur.equals(search)) {
                        bottom.append(cur + "\n");
                        cur = path2.get(cur);
                    }
                    bottom.append(search);
//                    for(String s2: result) System.out.println(s2);
                    break;
                }
            }
            if(!run) break;
            while (!backdepths.isEmpty() && backdepths.peek() <= curD) {
//                 System.out.println("Ran");
                HtmlRead(backlinks.poll(), backdepths.poll(), start, true);
//                 System.out.println(path2.size());
            }

            if(path2.containsKey(start)) {
                bottom.setText("");
                run = false;
                System.out.println("From Backward Search Only");
                ArrayList<String> result = new ArrayList<String>();
                String cur = start;
                while (!cur.equals(search)) {
                    result.add(cur);
                    cur = path.get(cur);
                }
                result.add(cur);
                for(String s: result) bottom.append(s);
                break;
            }




            curD ++;

            for(String s: path.keySet()) {
                bottom.setText("");
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
                    for(String s2: result) bottom.append(s2 + "\n");
                    cur = path2.get(s);
                    while (!cur.equals(search)) {
                        bottom.append(cur + "\n");
                        cur = path2.get(cur);
                    }
                    bottom.append(search);
//                    for(String s2: result) System.out.println(s2);
                    break;
                }
            }
            if(!run) break;
            if(curD>MAXDEPTH) {
                maxForward*=5;
                maxPrev*=5;
                search(start, search);
                System.out.println("Program Taking A While to Excecute Since Links Are Far Apart. Make Sure You Only Use Wikipedia Links. Please Wait Or Try Again Later.");
            }
            System.out.println("path: " + depths.size());
            System.out.println("path2: " + backdepths.size());
        }
    }

    public static HashMap<String, String> path = new HashMap<>(); // curlink to past link
    public static HashMap<String, String> path2 = new HashMap<>();
    public static int MAXDEPTH = 4;


    public static void HtmlRead(String link, int depth, String search, boolean back) {
        int count = 1;
        try {
            String link2;
            URL url;
            if(back) {
                link2 = "https://en.wikipedia.org/w/index.php?title=Special:WhatLinksHere/" + link.substring(link.indexOf("/wiki/")+6) + "&hideredirs=1" + "&limit=" + (maxPrev*2);
//                System.out.println(link2);
                url = new URL(link2);
            }
            else url = new URL(link);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(url.openStream())
            );
            String line;
            while ((line = reader.readLine()) != null) {
                if(!back && count>=maxForward) break;
                if(back && count>=maxPrev) break;
                count+=parseLine(line, link, depth, search, back);
            }
        }
        catch(Exception e) {
        }

    }
    public static boolean run = true;
    static String[] starters = new String[]{"href='","href=\""};

    public static int parseLine(String line, String pastlink, int depth, String search, boolean back) {
        if(!run) return 0;
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
                    return 0;
                }
                if(toss.contains(link)) return 0;
                if(link.contains(":")) return 0;
                link = "https://en.wikipedia.org" + link;
                if(link.equals(pastlink)) return 0;

                if(link.equals(search)) {
//                    System.out.println("FOUND");
                    if(back) path2.put(link, pastlink);
                    else path.put(link, pastlink);
                    run = false;
                    return 1;
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

                if(end==x.length) return 1;
                line = line.substring(end+1);
                return 1 + parseLine(line, pastlink, depth, search, back);
            }

        }
        return 0;

    }


}