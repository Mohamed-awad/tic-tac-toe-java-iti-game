package db;

public class UserAccount {

    private Long id;
    private String userName;
    private int active;
    public UserAccount(Long id, String userName, int active) {
        this.id = id;
        this.userName = userName;
        this.active = active;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public int isActive() {
        return active;
    }
    public void setActive(int active) {
        this.active = active;
    }
}
