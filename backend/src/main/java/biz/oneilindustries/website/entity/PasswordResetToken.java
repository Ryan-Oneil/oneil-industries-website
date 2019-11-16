package biz.oneilindustries.website.entity;

import biz.oneilindustries.website.entity.supers.Token;
import javax.persistence.Entity;

@Entity(name = "password_reset_token")
public class PasswordResetToken extends Token {

    public PasswordResetToken() {
    }

    public PasswordResetToken(String token, User username) {
        super(token, username);
    }
}
