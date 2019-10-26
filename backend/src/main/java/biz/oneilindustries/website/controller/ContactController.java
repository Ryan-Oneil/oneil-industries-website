package biz.oneilindustries.website.controller;

import biz.oneilindustries.website.entity.FeedBack;
import biz.oneilindustries.website.service.ContactService;
import biz.oneilindustries.website.validation.ContactForm;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactController {

    private static final int MAX_FORMS_PER_DAY = 3;
    private static final String UNKNOWN = "unknown";

    @Autowired
    private ContactService contactService;

    @PostMapping("/contact")
    public ResponseEntity sendContact(@ModelAttribute("ContactForm") @Valid ContactForm contactForm, BindingResult result, HttpServletRequest request) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().toString());
        }

        List<FeedBack> feedBacks = contactService.getIPFeedbackPastDay(getClientIpAddr(request).toString());

        if (feedBacks.size() >= MAX_FORMS_PER_DAY) {
            return ResponseEntity.status(403).body("Reached maximum contact form count within 24 hours");
        }
        contactService.registerFeedback(contactForm, getClientIpAddr(request).toString());

        return ResponseEntity.ok(HttpStatus.OK);
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
