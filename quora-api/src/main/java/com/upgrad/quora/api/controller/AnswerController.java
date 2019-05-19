package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerBusinessService;
import com.upgrad.quora.service.business.CommonBusinessService;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class AnswerController {
    @Autowired
    private CommonBusinessService commonBusinessService;

    @Autowired
    private QuestionBusinessService questionBusinessService;

    @Autowired
    private AnswerBusinessService answerBusinessService;

    @RequestMapping(method = RequestMethod.POST, path = "/question/{questionId}/answer/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(final AnswerRequest answerRequest, @PathVariable("questionId") final String question_id, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        String[] bearerToken = authorization.split("Bearer ");
        UserEntity signedinUser = commonBusinessService.authorizeUser(bearerToken[1]);
        QuestionEntity questionEntity = questionBusinessService.getQuestionById(question_id);
        final AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setUuid(UUID.randomUUID().toString());
        answerEntity.setAns(answerRequest.getAnswer());
        final ZonedDateTime now = ZonedDateTime.now();
        answerEntity.setDate(now);
        answerEntity.setUser(signedinUser);
        answerEntity.setQuestion(questionEntity);
        final AnswerEntity createdAnswerEntity = answerBusinessService.createAnswer(answerEntity);
        AnswerResponse answerResponse = new AnswerResponse().id(createdAnswerEntity.getUuid()).status("ANSWER CREATED");
        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/answer/edit/{answerId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswer(final AnswerEditRequest answerEditRequest, @PathVariable("answerId") final String answer_id, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, AnswerNotFoundException {
        String[] bearerToken = authorization.split("Bearer ");
        UserEntity signedinUser = commonBusinessService.authorizeUser(bearerToken[1]);
        AnswerEntity answerEntity = answerBusinessService.getAnswerById(answer_id);
        answerEntity.setAns(answerEditRequest.getContent());
        //checks if the user is authorized to edit an answer before allowing him to do so
        answerBusinessService.updateAnswer(signedinUser, answerEntity);
        AnswerEditResponse answerEditResponse = new AnswerEditResponse().id(answerEntity.getUuid()).status("ANSWER EDITED");
        return new ResponseEntity<AnswerEditResponse>(answerEditResponse, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/answer/delete/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@PathVariable("answerId") final String answer_id, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, AnswerNotFoundException {
        String[] bearerToken = authorization.split("Bearer ");
        UserEntity signedinUser = commonBusinessService.authorizeUser(bearerToken[1]);
        AnswerEntity answerEntity = answerBusinessService.getAnswerById(answer_id);
        //checks if the user is authorized to delete an answer before allowing him to do so
        answerBusinessService.deleteAnswer(signedinUser, answerEntity);
        AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse().id(answerEntity.getUuid()).status("ANSWER DELETED");
        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "answer/all/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<AnswerDetailsResponse>> getQuestionsByUser(@PathVariable("questionId")final String questionId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        String[] bearerToken = authorization.split("Bearer ");
        commonBusinessService.authorizeUser(bearerToken[1]);
        QuestionEntity questionEntity = questionBusinessService.getQuestionById(questionId);
        String question =questionEntity.getContent();
        Iterator<AnswerEntity> itrAnswers = answerBusinessService.getAnswersToQuestion(questionEntity).iterator();
        List<AnswerDetailsResponse> answerResponseList = new ArrayList<>();
        while (itrAnswers.hasNext()) {
            AnswerEntity answerEntity = itrAnswers.next();
            /*questionResponseList.add(new QuestionDetailsResponse().id(itrQuestions.next().getUuid()).
                    content(itrQuestions.next().getContent()));*/
            answerResponseList.add(new AnswerDetailsResponse().id(answerEntity.getUuid()).
                    answerContent(answerEntity.getAns()));

            }
        answerResponseList.add(0,new AnswerDetailsResponse().questionContent(question));

        return new ResponseEntity<List<AnswerDetailsResponse>>(answerResponseList, HttpStatus.OK);

     }
}
