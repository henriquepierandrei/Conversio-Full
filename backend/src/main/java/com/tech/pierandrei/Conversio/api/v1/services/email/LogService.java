package com.tech.pierandrei.Conversio.api.v1.services.email;

import com.tech.pierandrei.Conversio.api.v1.dtos.emailDto.LogResponseDto;
import com.tech.pierandrei.Conversio.api.v1.entities.emails.LogEmails;
import com.tech.pierandrei.Conversio.api.v1.entities.emails.StatusEnum;
import com.tech.pierandrei.Conversio.api.v1.repositories.LogEmailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

@Service
public class LogService {
    private static final Logger log = LoggerFactory.getLogger(LogService.class);
    @Autowired
    private LogEmailsRepository logEmailsRepository;



    public void createLog(boolean hadError, String messageError,
                          String templateName, String timeToSend, String smtpUserOwner, HashMap<String, StatusEnum> report, int quantity){
        LogEmails logEmails = new LogEmails();
        logEmails.setCreatedAt(Instant.now());
        logEmails.setHadError(hadError);
        if (hadError){
            logEmails.setMessageError(messageError);
        }
        logEmails.setTemplateName(templateName);
        logEmails.setTimeToSend(timeToSend);
        logEmails.setSmtpUsernameOwner(smtpUserOwner);
        logEmails.setReportEmailsSended(report);
        logEmails.setQuantityEmails(quantity);

        logEmailsRepository.save(logEmails);
    }

    private Page<LogResponseDto> mapToDto(Page<LogEmails> logs){
        return logs.map(log -> {
            LogResponseDto logResponseDto = new LogResponseDto();
            logResponseDto.setSmtpUsernameOwner(log.getSmtpUsernameOwner());
            logResponseDto.setCreatedAt(log.getCreatedAt());
            logResponseDto.setTemplateName(log.getTemplateName());
            logResponseDto.setReportEmailsSended(log.getReportEmailsSended());
            logResponseDto.setTimeToSend(log.getTimeToSend());
            logResponseDto.setHadError(log.isHadError());
            logResponseDto.setMessageError(log.getMessageError());
            logResponseDto.setQuantityEmails(log.getQuantityEmails());
            return logResponseDto;
        });
    }

    public Page<LogResponseDto> getLogBySmtpAccount(String username, Pageable pageable){
        Page<LogEmails> logEmailsPage = logEmailsRepository.findBySmtpUsernameOwner(username, pageable);
        return mapToDto(logEmailsPage);
    }

    public Page<LogResponseDto> getAll(Pageable pageable){
        Page<LogEmails> logEmailsPage = logEmailsRepository.findAll(pageable);
        return mapToDto(logEmailsPage);

    }




}