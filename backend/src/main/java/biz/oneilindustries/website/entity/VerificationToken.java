package biz.oneilindustries.website.entity;

import biz.oneilindustries.website.entity.supers.Token;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "verificationtoken")
public class VerificationToken extends Token {

    public VerificationToken() {
    }

    public VerificationToken(String token, User username) {
        super(token, username);
    }
}
