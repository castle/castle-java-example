package io.castle.example.model;

public class TestUser {

    private Integer id;
    private String login;
    private String password;
    private String username;
    private String lastname;

    public TestUser() {
    }

    public TestUser(Integer id, String login, String password, String username, String lastname) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.username = username;
        this.lastname = lastname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {return username; }

    public void setUsername(String username) { this.username = username; }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getLastname() { return lastname; }

    public void setLastname(String lastname) { this.lastname = lastname; }
}
