package com.theironyard;

/**
 * Created by earlbozarth on 10/28/15.
 */
public class Message {
    int id;
    int replyID;
    String userName;
    String text;

    public Message(int id, int replyID, String userName, String text) {
        this.id = id;
        this.replyID = replyID;
        this.userName = userName;
        this.text = text;
    }//End of Constructor

}//Enf of Message Class
