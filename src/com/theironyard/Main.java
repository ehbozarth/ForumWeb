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

        Spark.get(
                "/",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    return new ModelAndView(m,"threads.html");
                }),
                new MustacheTemplateEngine()
        );

    }//End of Main Method

}//End of Main Class
