package org.romanchi.database.entities;

public class User {
    private long userId;
    private String userName;
    private String userSurname;
    private String userEmail;
    private String userPassword;
    private UserRole userUserRole;
    private String userAddress;
    private String userLanguage;

    //todo: add useradress
    public User() {
    }

    public User(long userId, String userName, String userSurname, String userEmail, String userPassword, UserRole userUserRole, String userAddress, String userLanguage) {
        this.userId = userId;
        this.userName = userName;
        this.userSurname = userSurname;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userUserRole = userUserRole;
        this.userAddress = userAddress;
        this.userLanguage = userLanguage;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public UserRole getUserUserRole() {
        return userUserRole;
    }

    public void setUserUserRole(UserRole userUserRole) {
        this.userUserRole = userUserRole;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserLanguage() {
        return userLanguage;
    }

    public void setUserLanguage(String userLanguage) {
        this.userLanguage = userLanguage;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userSurname='" + userSurname + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userUserRole=" + userUserRole +
                ", userAddress='" + userAddress + '\'' +
                ", userLanguage='" + userLanguage + '\'' +
                '}';
    }
}
