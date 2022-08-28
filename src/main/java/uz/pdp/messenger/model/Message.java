package uz.pdp.messenger.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Message extends Base{

    private long sender;
    private long consumer;
    private String text;
    private Timestamp time;


    public Message(long id) {
        super(id);
    }

    public long getSender() {
        return sender;
    }

    public void setSender(long sender) {
        this.sender = sender;
    }

    public long getConsumer() {
        return consumer;
    }

    public void setConsumer(long consumer) {
        this.consumer = consumer;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Override
    protected void get(ResultSet resultSet) {
        try{

            this.sender = resultSet.getLong("sender");
            this.consumer = resultSet.getLong("consumer");
            this.text = resultSet.getString("text");
            this.time = resultSet.getTimestamp("time");
        }catch (SQLException e){
            e.printStackTrace();
        }



    }
}
