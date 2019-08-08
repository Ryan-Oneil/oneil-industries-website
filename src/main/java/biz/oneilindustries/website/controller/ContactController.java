package biz.oneilindustries.website.controller;

import biz.oneilindustries.website.entity.FeedBack;
import biz.oneilindustries.website.service.ContactService;
import biz.oneilindustries.website.validation.ContactForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Controller
public class ContactController {

    private static final int MAX_FORMS_PER_DAY = 3;
    private static final String UNKNOWN = "unknown";


    @Autowired
    private ContactService contactService;

    @GetMapping("/contact")
    public String showContact(Model model) {

        model.addAttribute("ContactForm", new ContactForm());

        return "contact";
    }

    @PostMapping("/contact")
    public String sendContact(@ModelAttribute("ContactForm") @Valid ContactForm contactForm, BindingResult result, HttpServletRequest request) {

        if (result.hasErrors()) {
            return "contact";
        }

        List<FeedBack> feedBacks = contactService.getIPFeedbackPastDay(getClientIpAddr(request).toString());

        if (feedBacks.size() >= MAX_FORMS_PER_DAY) {
            result.rejectValue("name","name.count","Reached maximum contact form count within 24 hours");
            return "contact";
        }
        contactService.registerFeedback(contactForm, getClientIpAddr(request).toString());

        return "redirect:/contact";
    }

    private static InetAddress getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        try {
            return InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            return null;
        }
    }
}
