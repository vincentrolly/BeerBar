package netgloo.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by vro on 04/11/16.
 */
@Entity
@Table(name = "User")
public class User {

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String token;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}

