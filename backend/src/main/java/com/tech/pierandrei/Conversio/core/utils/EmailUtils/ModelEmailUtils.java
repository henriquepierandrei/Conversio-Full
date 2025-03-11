package com.tech.pierandrei.Conversio.core.utils.EmailUtils;

import com.tech.pierandrei.Conversio.api.v1.dtos.emailDto.EmailRequestDto;
import com.tech.pierandrei.Conversio.api.v1.entities.emails.Clients;
import com.tech.pierandrei.Conversio.api.v1.entities.emails.SmtpConfig;
import com.tech.pierandrei.Conversio.api.v1.entities.User;
import com.tech.pierandrei.Conversio.api.v1.entities.emails.Type;
import com.tech.pierandrei.Conversio.api.v1.repositories.SmtpConfigRepository;
import com.tech.pierandrei.Conversio.core.exceptions.exceptionsCreated.SmtpConfigurationException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

@Component
public class ModelEmailUtils {
    private static final Logger log = LoggerFactory.getLogger(ModelEmailUtils.class);
    private final SmtpConfigRepository smtpConfigRepository;
    private final LoadTemplatesUtils loadTemplates;
    private final LoadComponentsUtils loadComponents;



    public ModelEmailUtils(SmtpConfigRepository smtpConfigRepository, LoadTemplatesUtils loadTemplates, LoadComponentsUtils loadComponents, BCryptPasswordEncoder bCryptPasswordEncoder ) {
        this.smtpConfigRepository = smtpConfigRepository;
        this.loadTemplates = loadTemplates;
        this.loadComponents = loadComponents;
    }


    public String getEmailTemplateWithType(Type type) throws IOException {
        if (type == null) {
            throw new IllegalArgumentException("Invalid Email");
        }
        switch (type.toString()){
            case "ALERT": return loadTemplates.loadAlertTemplate();
            case "PROMOTION": return loadTemplates.loadPromoTemplate();
            case "CHARGE": return loadTemplates.loadChargeTemplate();
        }
        return null;
    }
    public String getFooterComponents() throws IOException{
        return loadComponents.loadFooterOneTemplate();
    }

    private String verificationCredentials(String personalizedTemplate, EmailRequestDto emailRequest, User user){
        // Verifica se o botão está disponível e ajusta a visibilidade
        if (!emailRequest.getUrlButton().isBlank()){
            // Substitui o link do botão
            personalizedTemplate = personalizedTemplate.replace("{link-button}", emailRequest.getUrlButton());
            // Se o link não estiver vazio, garante que a visibilidade do botão seja normal
            personalizedTemplate = personalizedTemplate.replace("{visibility-button}", "");
        } else {
            // Caso contrário, esconde o botão
            personalizedTemplate = personalizedTemplate.replace("{visibility-button}", "display: none; max-height: 0px; overflow: hidden;");
            // Limpa o link do botão, tornando-o vazio
            personalizedTemplate = personalizedTemplate.replace("{link-button}", "");
        }

        // Substitui o banner, caso haja
        if (!emailRequest.getUrlBanner().isBlank()){
            personalizedTemplate = personalizedTemplate.replace("{link-banner}", emailRequest.getUrlBanner());
        } else {
            personalizedTemplate = personalizedTemplate.replace("{link-banner}", "");
        }


        personalizedTemplate = personalizedTemplate.replace("{company-name}", user.getCompanyName());

        return personalizedTemplate;
    }


    public JavaMailSender getMailSender(String smtpName) {
        SmtpConfig smtpConfig = smtpConfigRepository.findByUsername(smtpName)
                .orElseThrow(() -> new SmtpConfigurationException("Configuration SMTP not found: " + smtpName));

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtpConfig.getHost());
        mailSender.setPort(smtpConfig.getPort());
        mailSender.setUsername(smtpConfig.getUsername());
        mailSender.setPassword(smtpConfig.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", smtpConfig.isAuth());
        props.put("mail.smtp.starttls.enable", smtpConfig.isStarttls());
        props.put("mail.smtp.ssl.trust", smtpConfig.getSslTrust());
        props.put("mail.default-encoding", "UTF-8");

        System.out.println(props);

        return mailSender;
    }

    public void emailModelForClient(EmailRequestDto emailRequest, int clientIndex, String emailTemplate, User user) throws MessagingException {
        try {
            JavaMailSender javaMailSender = getMailSender(emailRequest.getFrom());

            Clients client = emailRequest.getClients().get(clientIndex);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setSubject(emailRequest.getSubject());
            helper.setTo(client.getEmail());

            String personalizedTemplate;

            personalizedTemplate = emailTemplate.replace("{name}", client.getName())
                    .replace("{p-01}", emailRequest.getpOne())
                    .replace("{p-02}", emailRequest.getpTwo());

            // Substituindo os parâmetros do template
            personalizedTemplate = verificationCredentials(personalizedTemplate, emailRequest, user);


            // adding footer in template
            personalizedTemplate = personalizedTemplate.replace("{footer-component}", getFooterComponents());


            // Definindo o corpo do e-mail
            helper.setText(personalizedTemplate, true);

            // Enviando o e-mail para o cliente
            javaMailSender.send(message);
            log.info("Email Sent to: " + client.getEmail());
        } catch (MessagingException e) {
            log.error("Error sending emailDto to client", e);
            throw new MessagingException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
