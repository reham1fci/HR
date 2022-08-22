package com.max.rm.hr.ChatPkg;


public class Message {
    String mTxt;
    String mSender;
    String date;
    String time;
    String message_id;
    String senderName;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Message{" +
                "mTxt='" + mTxt + '\'' +
                ", mSender='" + mSender + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", message_id='" + message_id + '\'' +
                '}';
    }

    public Message() {
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }



    public String getmTxt() {
        return mTxt;
    }

    public void setmTxt(String mTxt) {
        this.mTxt = mTxt;
    }

    public String getmSender() {
        return mSender;
    }

    public void setmSender(String mSender) {
        this.mSender = mSender;
    }

    public Message(String mTxt, String mSender, String date, String time, String message_id,String emp_name) {
        this.mTxt = mTxt;
        this.mSender = mSender;
        this.date = date;
        this.time = time;
        this.message_id = message_id;
        this.senderName=emp_name;
    }
}
