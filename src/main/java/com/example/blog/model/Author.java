package com.example.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
public class Author extends AuditModel{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @Column(length = 45)
    private String username;
    @Column(length = 150)
    private String password;
    @Column(length = 45)
    private String first_name;
    @Column(length = 45)
    private String last_name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    /* Gets password.
     *
     * @return the password
     */
    // @JsonIgnore
    // @JsonProperty(value = "password")
    public String getPassword() {
        return password;
    }
    /* Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    
}