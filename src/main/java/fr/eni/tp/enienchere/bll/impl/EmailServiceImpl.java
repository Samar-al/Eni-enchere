package fr.eni.tp.enienchere.bll.impl;

import fr.eni.tp.enienchere.bll.EmailService;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

//    @Autowired
//    private JavaMailSender emailSender;
//
    @Override
    public void sendPasswordResetEmail(String receiver, String token) {

    }
}
