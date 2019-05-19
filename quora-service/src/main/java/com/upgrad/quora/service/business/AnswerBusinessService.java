package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnswerBusinessService {

    @Autowired
    private AnswerDao answerDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        answerDao.createAnswer(answerEntity);
        return answerEntity;
    }

    @Transactional
    public AnswerEntity getAnswerById(String answer_id) throws AnswerNotFoundException {

        AnswerEntity answerEntity = answerDao.getAnswerById(answer_id);
        if(answerEntity == null){
            throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
        }
        return answerEntity;
    }


    @Transactional
    public void updateAnswer(UserEntity signedinUser, AnswerEntity answerEntity) throws AuthorizationFailedException, AnswerNotFoundException {

        if (answerEntity == null) {
            throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
        }
        UserEntity answerOwner = answerEntity.getUser();
        if (!(answerOwner.getUuid().equals(signedinUser.getUuid()))) {
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner can edit the answer");
        }
        answerDao.updateAnswer(answerEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAnswer(UserEntity signedinUser, AnswerEntity answerEntity) throws AnswerNotFoundException,AuthorizationFailedException {
        if (answerEntity == null) {
            throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
        }
        UserEntity answerOwner = answerEntity.getUser();
        System.out.println(answerOwner.getUuid());
        System.out.println(signedinUser.getUuid());
        if (!(answerOwner.getUuid().equals(signedinUser.getUuid()) || signedinUser.getRole().equals("admin"))){
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");
        }
        answerDao.deleteAnswer(answerEntity);

    }

    @Transactional
    public List<AnswerEntity> getAnswersToQuestion(QuestionEntity questionEntity) {
        return answerDao.getAnswersToQuestion(questionEntity);
    }
}
