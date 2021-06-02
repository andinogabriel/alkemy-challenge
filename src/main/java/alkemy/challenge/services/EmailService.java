package alkemy.challenge.services;

import alkemy.challenge.entities.ConfirmationTokenEntity;
import alkemy.challenge.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final ConfirmationTokenService confirmationTokenService;
    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(ConfirmationTokenService confirmationTokenService, TemplateEngine templateEngine, JavaMailSender javaMailSender) {
        this.confirmationTokenService = confirmationTokenService;
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }

    public void sendHtmlMail(UserEntity userEntity) throws MessagingException {
        ConfirmationTokenEntity confirmationTokenEntity = confirmationTokenService.findByUser(userEntity);

        //Check if the user has a token
        if(confirmationTokenEntity != null) {
            String token = confirmationTokenEntity.getToken();
            Context context = new Context();
            context.setVariable("title", "Java Alkemy Challenge - Confirm your email");
            context.setVariable("link", "http://localhost:8082/api/v1/auth/activation?token=" + token);
            //html template
            String body = templateEngine.process("confirmation", context); //html template name

            //Send the confirmation email
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(userEntity.getEmail());
            helper.setSubject("email address confirmation");
            helper.setText(body, true);
            javaMailSender.send(message);
        }


    }

}
