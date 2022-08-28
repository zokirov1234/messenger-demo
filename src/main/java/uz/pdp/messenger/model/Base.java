package uz.pdp.messenger.model;


import java.sql.ResultSet;

public abstract class Base {
    private long id;


    protected void get(ResultSet resultSet){
        return;
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Base(long id) {
        this.id = id;
    }

}
