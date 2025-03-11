package com.tech.pierandrei.Conversio.api.v1.services;

import com.tech.pierandrei.Conversio.api.v1.dtos.smtpDto.DefaultResponseDto;
import com.tech.pierandrei.Conversio.api.v1.dtos.userDto.UserResponseDto;
import com.tech.pierandrei.Conversio.api.v1.repositories.UserRepository;
import com.tech.pierandrei.Conversio.core.utils.GetUserOrClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GetUserOrClientUtils getUserOrClientUtils;

    public DefaultResponseDto changeCompanyName(String companyName, Jwt jwt) throws Exception {
        try {
            var user = getUserOrClientUtils.getUserByJwt(jwt);
            user.setCompanyName(companyName);
            userRepository.save(user);
            return new DefaultResponseDto("Nome alterado para: " + companyName, 200);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public UserResponseDto getUserData(Jwt jwt) throws Exception {
        try {
            var user = getUserOrClientUtils.getUserByJwt(jwt);
            UserResponseDto userResponseDto = new UserResponseDto();
            userResponseDto.setEmail(user.getEmail());
            userResponseDto.setCreatedAt(user.getCreatedAt());
            userResponseDto.setCompanyName(user.getCompanyName());
            userResponseDto.setAccountId(user.getUuid().toString());
            return userResponseDto;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

}
