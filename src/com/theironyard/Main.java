package com.theironyard;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        HashMap<String, User> userHashMap = new HashMap<>();
        ArrayList<Message> messageArrayList = new ArrayList<>();
        addTestUsers(userHashMap);
        addTestMessages(messageArrayList);

        Spark.get(
                "/",
                ((request, response) -> {
                    ArrayList<Message> threadsList = new ArrayList();
                    for(Message message : messageArrayList){
                        if(message.replyID == -1){
                            threadsList.add(message);
                        }
                    }//End of for loop
                    HashMap m = new HashMap();
                    m.put("threads", threadsList);
                    return new ModelAndView(m,"threads.html");
                }),
                new MustacheTemplateEngine()
        );

    }//End of Main Method

    static void addTestUsers(HashMap<String, User> userHashMap ){
        userHashMap.put("Alice", new User());
        userHashMap.put("Bob", new User());
        userHashMap.put("Charlie", new User());

    }//End of addTestUsers

    static void addTestMessages(ArrayList<Message> messageArrayList){
        messageArrayList.add(new Message(0,-1, "Alice", "This is a test thread!"));
        messageArrayList.add(new Message(1,-1, "Bob", "This is a test thread!"));
        messageArrayList.add(new Message(2,0, "Charlie", "Cool thread Alice!"));
        messageArrayList.add(new Message(3,2, "Alice", "Thanks Charlie!"));


    }//End of addTestMessages

}//End of Main Class
