package com.tech.pierandrei.Conversio.api.v1.services.email;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.tech.pierandrei.Conversio.api.v1.dtos.clientDto.ClientRequestDto;
import com.tech.pierandrei.Conversio.api.v1.dtos.emailDto.DashboardResponseDto;
import com.tech.pierandrei.Conversio.api.v1.dtos.emailDto.EmailResponseDto;
import com.tech.pierandrei.Conversio.api.v1.dtos.smtpDto.DefaultResponseDto;
import com.tech.pierandrei.Conversio.api.v1.entities.emails.*;
import com.tech.pierandrei.Conversio.api.v1.entities.User;
import com.tech.pierandrei.Conversio.api.v1.repositories.ClientRepository;
import com.tech.pierandrei.Conversio.api.v1.repositories.EmailRepository;
import com.tech.pierandrei.Conversio.api.v1.repositories.LogEmailsRepository;
import com.tech.pierandrei.Conversio.api.v1.repositories.SmtpConfigRepository;
import com.tech.pierandrei.Conversio.core.utils.EmailUtils.ModelEmailUtils;
import com.tech.pierandrei.Conversio.api.v1.dtos.emailDto.EmailRequestDto;
import com.tech.pierandrei.Conversio.core.utils.GetUserOrClientUtils;
import jakarta.transaction.Transactional;
import org.apache.commons.collections4.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final ModelEmailUtils modelEmailUtils;
    private final GetUserOrClientUtils getUserUtils;
    private final ExecutorService executorService;


    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private LogService logService;

    @Autowired
    private SmtpAccountService smtpAccountService;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private SmtpConfigRepository smtpConfigRepository;

    @Autowired
    private LogEmailsRepository logEmailsRepository;

    @Autowired
    private GetUserOrClientUtils getUserOrClientUtils;

    private int getQuantityIn24h(){
        Instant twentyFourHoursAgo = Instant.now().minus(24, ChronoUnit.HOURS);
        List<LogEmails> logEmailsList = logEmailsRepository.findByCreatedAtAfter(twentyFourHoursAgo);
        int quantity = 0;
        for (LogEmails logEmails : logEmailsList){
            quantity += logEmails.getQuantityEmails();
        }
        return quantity;
    }


    public EmailService(ModelEmailUtils modelEmailUtils, GetUserOrClientUtils getUserOrClientUtils) {
        this.modelEmailUtils = modelEmailUtils;
        this.getUserUtils = getUserOrClientUtils;
        this.executorService = Executors.newFixedThreadPool(30);
    }

    // Save emailDto details to the database
    public void saveEmailInDb(EmailRequestDto emailRequest, User user, HashMap<String, StatusEnum> emailAndStatus, int quantity) throws Exception {
        try {
            Email email = new Email();
            email.setpOne(emailRequest.getpOne());
            email.setpTwo(emailRequest.getpTwo());
            email.setFromEmail(emailRequest.getFrom());
            email.setType(emailRequest.getType());
            email.setToEmailsAndStatus(emailAndStatus);
            email.setSubject(emailRequest.getSubject());
            email.setUserOwner(user);
            email.setQuantitySent(quantity);
            email.setHasButton(emailRequest.isHasButton());
            email.setUrlButton(emailRequest.getUrlButton());
            email.setHasBanner(emailRequest.isHasBanner());
            email.setUrlBanner(emailRequest.getUrlBanner());
            emailRepository.save(email);
        } catch (Exception e) {
            log.error("Email log not saved in the database, reason: ", e);
            throw new Exception("Erro ao salvar log de emailDto no banco de dados");
        }
    }

    // ===================================================================================================== //
    // Asynchronously send emails
    @Async
    public CompletableFuture<EmailResponseDto> sendEmailsAsync(EmailRequestDto emailRequest, User user) throws Exception {
        String emailTemplate;
        HashMap<String, StatusEnum> emailStatusMap = new HashMap<>();

        try {
            emailTemplate = modelEmailUtils.getEmailTemplateWithType(emailRequest.getType());
        } catch (IOException e) {
            log.error("Error loading emailDto template", e);
            return CompletableFuture.failedFuture(e);
        }

        // Email counters: [0] - Selected, [1] - Sent, [2] - Failed
        int[] emailStats = {0, 0, 0};
        long startTime = System.nanoTime();

        // Send emails asynchronously
        List<CompletableFuture<Void>> futures = emailRequest.getClients().stream()
                .map(client -> CompletableFuture.runAsync(() ->
                        sendEmailToClient(emailRequest, client, emailTemplate, user, emailStats, emailStatusMap), executorService))
                .collect(Collectors.toList());

        // Wait for all threads to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        long duration = System.nanoTime() - startTime;

        // Prepare response
        EmailResponseDto emailResponse = new EmailResponseDto();
        emailResponse.setTime(String.format("%.3f seconds", duration / 1_000_000_000.0));
        emailResponse.setQuantityEmailsSelected(emailStats[0]);
        emailResponse.setTotalEmailsSended(emailStats[1]);
        emailResponse.setQuantityEmailsFailed(emailStats[2]);
        emailResponse.setToEmailsAndStatus(emailStatusMap);

        saveEmailInDb(emailRequest, user, emailStatusMap, emailStats[1]);
        smtpAccountService.updateEmailsLogs(emailRequest.getFrom(),emailStats[1]);
        logService.createLog(false, null, emailRequest.getType().toString().toLowerCase(), emailResponse.getTime(), emailRequest.getFrom(), emailStatusMap, emailStats[1]);

        return CompletableFuture.completedFuture(emailResponse);
    }

    // Send emailDto to a single client
    private void sendEmailToClient(EmailRequestDto emailRequest, Clients client, String emailTemplate, User user, int[] emailStats, HashMap<String, StatusEnum> emailStatusMap) {
        try {

            if (client.getUuid() != null && getUserUtils.clientExists(emailRequest, user)) {
                emailStatusMap.put(client.getEmail(), StatusEnum.FAILED);
                emailStats[2]++;
            } else {
                emailStats[0]++; // Increment total selected emails
                String clientEmail = client.getEmail();
                log.info("Sending emailDto to: " + clientEmail);

                modelEmailUtils.emailModelForClient(emailRequest, emailRequest.getClients().indexOf(client), emailTemplate, user);
                log.info("Email sent successfully to: " + clientEmail);
                emailStatusMap.put(clientEmail, StatusEnum.SENT);
                emailStats[1]++;
            }
        } catch (Exception e) {
            log.error("Error sending emailDto to: " + client.getEmail(), e);
            emailStatusMap.put(client.getEmail(), StatusEnum.FAILED);
            emailStats[2]++;
        }
    }
    // ===================================================================================================== //

    public DashboardResponseDto getDashboardData(Jwt jwt) {
        try {
            String subClaim = jwt.getClaim("sub");
            UUID userId = UUID.fromString(subClaim);
            User user = getUserOrClientUtils.getUserById(userId);

            DashboardResponseDto responseDto = new DashboardResponseDto(0, 0, 0, 0);
            List<SmtpConfig> smtpConfigsList = smtpConfigRepository.findAll();
            List<Clients> clientsList = clientRepository.findByUserOwner(user);


            for (SmtpConfig smtpConfig : smtpConfigsList) {
                responseDto.setQuantityEmailsIn24h(getQuantityIn24h());
                responseDto.setTotalEmailsSended(smtpAccountService.getTotalEmailsSents());
            }
            responseDto.setFullEmailCapicity(smtpConfigsList.size() * 500);
            responseDto.setTotalClients(clientsList.size());
            return responseDto;

        } catch (IllegalArgumentException e) {
            log.error("Erro ao converter UUID do usuário: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token inválido.");
        } catch (NoSuchElementException e) {
            log.error("Usuário não encontrado: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado.");
        } catch (Exception e) {
            log.error("Erro inesperado em getDashboardData: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao obter dados do dashboard.");
        }

    }

    @Transactional
    public List<Clients> processCsvEmailSend(MultipartFile file) throws Exception {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            // Configura o OpenCSV para converter cada linha do CSV em um objeto Usuario
            CsvToBean<ClientRequestDto> csvToBean = new CsvToBeanBuilder<ClientRequestDto>(reader)
                    .withType(ClientRequestDto.class) // Define a classe de destino
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            // Converte o CSV para uma lista de objetos Usuario
            List<ClientRequestDto> clientsRequestList = csvToBean.parse();

            List<Clients> clients = new ArrayList<>();

            for(ClientRequestDto clientRequestDto : clientsRequestList){
                Clients client = new Clients();
                client.setEmail(clientRequestDto.getEmail());
                client.setName(clientRequestDto.getName());
                clients.add(client);
            }

            return clients;
        }
    }
}

