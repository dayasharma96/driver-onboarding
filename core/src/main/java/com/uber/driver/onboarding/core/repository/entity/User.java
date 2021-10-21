package com.uber.driver.onboarding.core.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uber.driver.onboarding.core.util.SecurityUtil;
import com.uber.driver.onboarding.model.enums.UserType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 464806493213082210L;

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(updatable = false, length = 36)
    private String id;

    @Column(name = "email", unique = true, insertable = true, nullable = false, updatable = false)
    private String email;

    @Column(name = "name", length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    @Column(name = "pwd_salt", insertable = true, nullable = false, length = 5000)
    private String pwdSalt;

    @Column(name = "pwd_hash", insertable = true, nullable = false, length = 5000)
    private String pwdHash;

    @Column(name = "creation_date", nullable = true)
    private Date creationDate;

    @Column(name = "last_update_date", nullable = true)
    private Date lastUpdateDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwdSalt() {
        return pwdSalt;
    }

    public void setPwdSalt(String pwdSalt) {
        this.pwdSalt = pwdSalt;
    }

    public String getPwdHash() {
        return pwdHash;
    }

    public void setPwdHash(String pwdHash) {
        this.pwdHash = pwdHash;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(email, user.email) &&
                Objects.equals(name, user.name) &&
                userType == user.userType &&
                Objects.equals(pwdHash, user.pwdHash) &&
                Objects.equals(creationDate, user.creationDate) &&
                Objects.equals(lastUpdateDate, user.lastUpdateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, name, userType, pwdHash, creationDate, lastUpdateDate);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", userType=" + userType +
                ", pwdHash='" + pwdHash + '\'' +
                ", creationDate=" + creationDate +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }

    @JsonIgnore
    public static User buildNew(UserType type, String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setUserType(type);
        user.setCreationDate(new Date());
        user.setLastUpdateDate(new Date());
        byte[] salt = SecurityUtil.getSecureRandomSalt();
        byte[] passwordHash = SecurityUtil.getPasswordHash(password, salt);
        user.setPwdSalt(SecurityUtil.getBase64EncodedString(salt));
        user.setPwdHash(SecurityUtil.getBase64EncodedString(passwordHash));
        return null;
    }

}
