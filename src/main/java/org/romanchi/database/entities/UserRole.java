package org.romanchi.database.entities;

public class UserRole {
    private long userRoleId;
    private String userRoleName;

    public UserRole() {
    }

    public UserRole(long userRoleId, String userRoleName) {
        this.userRoleId = userRoleId;
        this.userRoleName = userRoleName;
    }

    public long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public String getUserRoleName() {
        return userRoleName;
    }

    public void setUserRoleName(String userRoleName) {
        this.userRoleName = userRoleName;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "userRoleId=" + userRoleId +
                ", userRoleName='" + userRoleName + '\'' +
                '}';
    }
}
