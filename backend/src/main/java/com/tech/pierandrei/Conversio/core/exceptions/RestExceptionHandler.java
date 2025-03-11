package com.tech.pierandrei.Conversio.core.exceptions;

import com.tech.pierandrei.Conversio.core.exceptions.exceptionsCreated.*;
import jakarta.mail.MessagingException;
import org.eclipse.angus.mail.smtp.SMTPSendFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    // ======= LOGIN EXCEPTIONS ======= //
    @ExceptionHandler(InvalidLoginException.class)
    private ResponseEntity<RestErrorMessage> InvalidLoginExceptionHandler(InvalidLoginException invalidLoginException) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, invalidLoginException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restErrorMessage);
    }

    @ExceptionHandler(RolesNotFoundException.class)
    private ResponseEntity<RestErrorMessage> RolesNotFoundExceptionHandler(RolesNotFoundException rolesNotFoundException) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.CONFLICT, rolesNotFoundException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restErrorMessage);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    private ResponseEntity<RestErrorMessage> UserAlreadyExistsExceptionHandler(UserAlreadyExistsException userAlreadyExistsException) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.CONFLICT, userAlreadyExistsException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restErrorMessage);
    }

    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<RestErrorMessage> UserNotFoundExceptionHandler(UserNotFoundException userNotFoundException) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.CONFLICT, userNotFoundException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restErrorMessage);
    }

    @ExceptionHandler(NullPointerException.class)
    private ResponseEntity<RestErrorMessage> NullPointerExceptionHandler(NullPointerException nullPointerException) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, "Some type of error occurred while making the request.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restErrorMessage);
    }

    @ExceptionHandler(JwtException.class)
    private ResponseEntity<RestErrorMessage> JwtExceptionHandler(JwtException jwtException) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, "Some type of error occurred while obtaining the authentication token.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restErrorMessage);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<RestErrorMessage> IllegalArgumentExceptionHandler(IllegalArgumentException illegalArgumentException) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, illegalArgumentException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restErrorMessage);
    }

    @ExceptionHandler(TypeNotPresentException.class)
    private ResponseEntity<RestErrorMessage> TypeNotPresentExceptionHandler(TypeNotPresentException typeNotPresentException) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, typeNotPresentException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restErrorMessage);
    }

    @ExceptionHandler(MessagingException.class)
    private ResponseEntity<RestErrorMessage> MessagingExceptionHandler(MessagingException messagingException) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, messagingException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restErrorMessage);
    }

    @ExceptionHandler(SmtpConfigurationException.class)
    private ResponseEntity<RestErrorMessage> SmtpConfigurationExceptionHandler(SmtpConfigurationException smtpConfigurationException) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, smtpConfigurationException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restErrorMessage);
    }

    @ExceptionHandler(SMTPSendFailedException.class)
    private ResponseEntity<RestErrorMessage> SMTPSendFailedExceptionHandler(SMTPSendFailedException sMTPSendFailedException) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, sMTPSendFailedException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restErrorMessage);
    }

    @ExceptionHandler(MailSendException.class)
    private ResponseEntity<RestErrorMessage> SMTPSendFailedExceptionHandler(MailSendException mailSendException) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, mailSendException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restErrorMessage);
    }

    @ExceptionHandler(ClientCreateException.class)
    private ResponseEntity<RestErrorMessage> ClientCreateExceptionHandler(ClientCreateException clientCreateException) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.CONFLICT, clientCreateException.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(restErrorMessage);
    }






}