package biz.oneilindustries.website.validation;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class ContactForm {

    @NotEmpty(message = "Please enter your name")
    private String name;
    @Email
    @NotEmpty(message = "Please enter an email")
    private String email;
    @NotEmpty(message = "Please enter a subject")
    private String subject;
    @NotEmpty(message = "Please enter your message")
    private String message;

    public ContactForm(@NotEmpty(message = "Please enter your name") String name,
                       @Email @NotEmpty(message = "Please enter an email") String email,
                       @NotEmpty(message = "Please enter a subject") String subject,
                       @NotEmpty(message = "Please enter your message") String message) {
        this.name = name;
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    public ContactForm() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
