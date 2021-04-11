package umn.useit.model;

public class Room {

    boolean status;
    private final String user1;
    private final String user2;
    private final String problemTitle;

    public Room(String user1, String user2, String problemTitle, boolean status) {
        this.user1 = user1;
        this.user2 = user2;
        this.problemTitle = problemTitle;
        this.status = status;
    }

    public String getUser1() {
        return user1;
    }

    public String getUser2() {
        return user2;
    }

    public boolean isStatus() {
        return status;
    }

    public String getProblemTitle() {
        return problemTitle;
    }
}
