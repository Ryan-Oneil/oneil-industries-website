package biz.oneilindustries.website.service;

import biz.oneilindustries.website.dao.FeedBackDAO;
import biz.oneilindustries.website.entity.FeedBack;
import biz.oneilindustries.website.validation.ContactForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ContactService {

    private static final String RECEIVER_EMAIL = "blackielifegfgaming@gmail.com";

    @Autowired
    private FeedBackDAO feedBackDAO;

    @Autowired
    private EmailSender emailSender;

    @Transactional
    public List<FeedBack> getFeedBack() {
        return feedBackDAO.getFeedbacks();
    }

    @Transactional
    public List<FeedBack> getRecentFeedbacks(int amount) {
        return feedBackDAO.getRecentFeedbacks(amount);
    }

    @Transactional
    public List<FeedBack> getFeedbackByIP(String ip) {
        return feedBackDAO.getFeedbacksByIP(ip);
    }

    @Transactional
    public List<FeedBack> getIPFeedbackPastDay(String ip) {

        List<FeedBack> feedBacks = getFeedbackByIP(ip);
        List<FeedBack> within24hours = new ArrayList<>();

        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        Calendar feedbackCreationDate = Calendar.getInstance();

        for (FeedBack feedBack : feedBacks) {

            feedbackCreationDate.setTime(feedBack.getTime());
            feedbackCreationDate.add(Calendar.HOUR_OF_DAY,24);

            if (feedbackCreationDate.getTime().after(today.getTime())) {
                within24hours.add(feedBack);
            }
        }
        return within24hours;
    }

    @Transactional
    public FeedBack getFeedbackByID(int id) {
        return feedBackDAO.getFeedbackByID(id);
    }

    @Transactional
    public void saveFeedBack(FeedBack feedBack) {
        feedBackDAO.saveFeedback(feedBack);
    }

    @Transactional
    public void deleteFeedBack(FeedBack feedBack) {
        feedBackDAO.deleteFeedback(feedBack);
    }

    @Transactional
    public void registerFeedback(ContactForm contactForm, String ip) {
        FeedBack feedBack = new FeedBack(contactForm.getName(), contactForm.getEmail(), contactForm.getSubject(),
                contactForm.getMessage(), ip);

        saveFeedBack(feedBack);

        emailSender.sendSimpleEmail(RECEIVER_EMAIL,contactForm.getSubject(),contactForm.getMessage() + "\n From " + contactForm.getName(),contactForm.getName(), contactForm.getEmail());
    }
}
