package com.perchtech.humraz.blind;


public class book {
    private String post;
private String time;

long stackId;

        public book() {
      /*Blank default constructor essential for Firebase*/
        }
        public book(String a)
        {

        }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    //Getters and setters

}