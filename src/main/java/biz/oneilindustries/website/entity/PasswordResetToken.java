package biz.oneilindustries.website.entity;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Entity(name = "password_reset_token")
public class PasswordResetToken {

    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "username")
    private User username;

    @Column(name = "expiry_date")
    private Date expiryDate;

    public PasswordResetToken(String token, User username) {
        this.token = token;
        this.username = username;
        this.expiryDate = calculateExpiryDate();
    }

    public PasswordResetToken() {
    }

    private Date calculateExpiryDate() {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, EXPIRATION);
        return new Date(cal.getTime().getTime());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUsername() {
        return username;
    }

    public void setUsername(User username) {
        this.username = username;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PasswordResetToken that = (PasswordResetToken) o;
        return getId() == that.getId() &&
                getToken().equals(that.getToken()) &&
                getUsername().equals(that.getUsername()) &&
                getExpiryDate().equals(that.getExpiryDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getToken(), getUsername(), getExpiryDate());
    }
}
