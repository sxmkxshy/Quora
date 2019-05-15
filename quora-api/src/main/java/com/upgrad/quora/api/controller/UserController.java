package com.upgrad.quora.api.controller;

import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.SignupBusinessService;
import com.upgrad.quora.service.entity.UserEntity;


@RestController
@RequestMapping("/")
public class UserController {

        @Autowired
        private SignupBusinessService signupBusinessService;

        @RequestMapping(method = RequestMethod.POST, path = "/usersignup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
        public ResponseEntity<SignupUserResponse> userSignup(final SignupUserRequest signupUserRequest) throws SignUpRestrictedException {

            final UserEntity userEntity = new UserEntity();

            userEntity.setUuid(UUID.randomUUID().toString());
            userEntity.setFirstName(signupUserRequest.getFirstName());
            userEntity.setLastName(signupUserRequest.getLastName());
            userEntity.setEmail(signupUserRequest.getEmailAddress());
            userEntity.setPassword(signupUserRequest.getPassword());
            userEntity.setMobilePhone(signupUserRequest.getContactNumber());
            userEntity.setUsername(signupUserRequest.getUserName());
            userEntity.setDob(signupUserRequest.getDob());
            userEntity.setCountry(signupUserRequest.getCountry());
            userEntity.setAboutme(signupUserRequest.getAboutMe());
            userEntity.setSalt("1234abc");
            userEntity.setRole("nonadmin");
            signupBusinessService.emailValidation(signupUserRequest.getEmailAddress());
            signupBusinessService.unameValidation(signupUserRequest.getUserName());
            final UserEntity createdUserEntity = signupBusinessService.signup(userEntity);
            SignupUserResponse userResponse = new SignupUserResponse().id(createdUserEntity.getUuid()).status("USER SUCCESSFULLY REGISTERED");
            return new ResponseEntity<SignupUserResponse>(userResponse, HttpStatus.CREATED);
        }
    }

