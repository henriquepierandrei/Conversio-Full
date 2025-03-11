package com.tech.pierandrei.Conversio.api.v1.services.email;

import com.tech.pierandrei.Conversio.api.v1.dtos.smtpDto.SmtpConfigResponseDto;
import com.tech.pierandrei.Conversio.api.v1.dtos.smtpDto.SmtpRequestDto;
import com.tech.pierandrei.Conversio.api.v1.dtos.smtpDto.DefaultResponseDto;
import com.tech.pierandrei.Conversio.api.v1.entities.emails.SmtpConfig;
import com.tech.pierandrei.Conversio.api.v1.repositories.SmtpConfigRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SmtpAccountService {
    @Autowired
    private SmtpConfigRepository smtpConfigRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private SmtpConfig getSmtp(long id){
        Optional<SmtpConfig> smtpConfigOptional = smtpConfigRepository.findById(id);
        if (smtpConfigOptional.isEmpty()){
            return null;
        }
        return smtpConfigOptional.get();
    }

    @Transactional
    public DefaultResponseDto createSmtpAccount(@RequestBody SmtpRequestDto smtpRequestDto){
        try {
            var smtpAccount = new SmtpConfig();
            smtpAccount.setHost(smtpRequestDto.getHost());
            smtpAccount.setAuth(smtpRequestDto.getAuth());
            smtpAccount.setStarttls(smtpRequestDto.getStarttls());
            smtpAccount.setPort(smtpRequestDto.getPort());
            smtpAccount.setUsername(smtpRequestDto.getUsername());
            smtpAccount.setPassword(smtpRequestDto.getPassword());
            smtpAccount.setSslTrust(smtpRequestDto.getSslTrust());
            smtpConfigRepository.save(smtpAccount);
            return new DefaultResponseDto("Conta SMTP adicionada com sucesso!", 200);
        }catch (Exception e){
            return new DefaultResponseDto("Erro ao adicionar a conta SMTP! ", 400);
        }
    }

    public List<SmtpConfigResponseDto> smtpConfigResponseDto(){
        try {
            List<SmtpConfig> smtpConfigs = smtpConfigRepository.findAll();
            List<SmtpConfigResponseDto> smtpConfigsDto = new ArrayList<>();

            for (SmtpConfig smtpConfig : smtpConfigs){
                var smtpConfigDto = new SmtpConfigResponseDto();
                smtpConfigDto.setId(smtpConfig.getId());
                smtpConfigDto.setHost(smtpConfig.getHost());
                smtpConfigDto.setAuth(smtpConfig.isAuth());
                smtpConfigDto.setPassword(smtpConfig.getPassword());
                smtpConfigDto.setStarttls(smtpConfig.isStarttls());
                smtpConfigDto.setUsername(smtpConfig.getUsername());
                smtpConfigDto.setPort(smtpConfig.getPort());
                smtpConfigDto.setSslTrust(smtpConfig.getSslTrust());
                smtpConfigsDto.add(smtpConfigDto);
            }
            return smtpConfigsDto;
        }catch (Exception e){
            return Collections.emptyList();
        }
    }

    @Transactional
    public DefaultResponseDto deleteSmtp(long id){
        try {
            var smtp = getSmtp(id);
            smtpConfigRepository.delete((SmtpConfig) smtp);
            return new DefaultResponseDto("Conta deletada com sucesso", 200);
        }catch (Exception e){
            return null;
        }
    }

    @Transactional
    public DefaultResponseDto updateSmtp(long id, SmtpRequestDto smtpRequestDto) throws Exception {
        try {
            var smtp = getSmtp(id);
            if (smtp == null){
                return new DefaultResponseDto("Conta SMTP inexistente!", 400);
            }
            if (smtpRequestDto.getHost() != null){
                smtp.setHost(smtpRequestDto.getHost());
            }
            if (smtpRequestDto.getAuth() != null){
                smtp.setAuth(smtpRequestDto.getAuth());
            }
            if (smtpRequestDto.getStarttls() != null){
                smtp.setStarttls(smtpRequestDto.getStarttls());
            }
            if (smtpRequestDto.getAuth() != null){
                smtp.setStarttls(smtpRequestDto.getStarttls());
            }
            if (smtpRequestDto.getPort() != 0){
                smtp.setPort(smtpRequestDto.getPort());
            }
            if (smtpRequestDto.getUsername() != null){
                smtp.setUsername(smtpRequestDto.getUsername());
            }
            if (smtpRequestDto.getPassword() != null){
                smtp.setPassword(smtpRequestDto.getPassword());
            }
            if (smtpRequestDto.getSslTrust() != null){
                smtp.setSslTrust(smtpRequestDto.getSslTrust());
            }
            smtpConfigRepository.save(smtp);
            return new DefaultResponseDto("Configurações SMTP atualizadas com sucesso!", 200);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public void updateEmailsLogs(String username, int quantitySents){
        var optionalSmtpAccount = smtpConfigRepository.findByUsername(username);
        var smtp = optionalSmtpAccount.get();
        smtp.setTotalQuantityEmailsSents(smtp.getTotalQuantityEmailsSents() + quantitySents);
        smtpConfigRepository.save(smtp);
    }


    public int getTotalEmailsSents(){
        int totalSents = 0;
        List<SmtpConfig> smtpConfigsList = smtpConfigRepository.findAll();
        if (smtpConfigsList.isEmpty()){
            return 0;
        }
        for(SmtpConfig smtp : smtpConfigsList){
            totalSents += smtp.getTotalQuantityEmailsSents();
        }
        return totalSents;
    }




}

