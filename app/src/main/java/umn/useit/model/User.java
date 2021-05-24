package umn.useit.model;

public class User {

    private String firstname, lastname, email, profImg;
    private int asked, solved;

    User(){}

    public User(String firstname, String lastname, String email, int asked, int solved, String profImg) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.asked = asked;
        this.solved = solved;
        this.profImg = profImg;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        email = email.substring(0, email.indexOf('@'));
        return email;
    }

    public int getAsked() {
        return asked;
    }

    public int getSolved() {
        return solved;
    }

    public String getImg(){
        return profImg;
    }
}
