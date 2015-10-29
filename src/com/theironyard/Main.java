package com.theironyard;

import com.sun.tools.internal.ws.processor.model.Model;
import spark.ModelAndView;
import spark.Session;
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
                    Session session = request.session();
                    String user_name = session.attribute("username");

                    ArrayList<Message> threadsList = new ArrayList();
                    for (Message message : messageArrayList) {
                        if (message.replyID == -1) {
                            threadsList.add(message);
                        }
                    }//End of for loop
                    HashMap m = new HashMap();
                    m.put("threads", threadsList);
                    m.put("loginUsername", user_name);
                    m.put("replyId", -1);
                    return new ModelAndView(m, "threads.html");
                }),
                new MustacheTemplateEngine()
        );//End of Spark.get()

        Spark.get(
                "/replies",
                ((request1, response1) -> {
                    Session session = request1.session();
                    String username = session.attribute("username");

                    HashMap m = new HashMap();
                    m.put("loginUsername", username);
                    String id = request1.queryParams("id");
                    try{
                        int idNum = Integer.valueOf(id);
                        Message message = messageArrayList.get(idNum);
                        m.put("message", message);
                        m.put("replyId", message.id);


                        ArrayList<Message> repliesArrayList = new ArrayList();
                        for(Message message1 : messageArrayList){
                            if(message1.replyID == message.id){
                                repliesArrayList.add(message1);
                            }//End of If
                        }//End of For Loop
                        m.put("replies", repliesArrayList);
                    }//End of Try
                    catch(Exception e){
                    }//End of Catch
                    return new ModelAndView(m,"replies.html");
                }),
                new MustacheTemplateEngine()
        );


        Spark.post(
                "/create-message",
                ((request1, response1) -> {
                    Session session = request1.session();
                    String username = session.attribute("username");
                    if(username == null){
                        Spark.halt(403);
                    }
                    String replyId = request1.queryParams("replyId");
                    String text = request1.queryParams("text");

                    try{
                        int replyIdNum = Integer.valueOf(replyId);
                        Message message = new Message(messageArrayList.size(), replyIdNum, username, text);
                        messageArrayList.add(message);
                    }catch (Exception e){

                    }
                    response1.redirect(request1.headers("Referer"));
                    return "";
                })
        );//End of Spark.post() "/create-message"

        Spark.post(
                "/login",
                ((request, response) -> {
                    String username = request.queryParams("username");
                    String password = request.queryParams("password");
                    if(username.isEmpty() || password.isEmpty()){
                        Spark.halt(403);
                    }

                    User user = userHashMap.get(username);
                    if(user == null){
                        user = new User();
                        user.password = password;
                        userHashMap.put(username,user);
                    }//end of if user does NOT exist
                    else if (!password.equals(user.password)){
                        Spark.halt(403);
                    }

                    Session session = request.session();
                    session.attribute("username", username);

                    response.redirect(request.headers("Referer"));
                    return "";
                })
        );//End of Spark.post "/login"

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
