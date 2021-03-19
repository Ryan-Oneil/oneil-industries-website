package biz.oneilenterprise.website.entity;

import biz.oneilenterprise.website.entity.supers.Token;
import javax.persistence.Entity;

@Entity(name = "password_reset_token")
public class PasswordResetToken extends Token {

    public PasswordResetToken() {
    }

    public PasswordResetToken(String token, User username) {
        super(token, username);
    }
}
