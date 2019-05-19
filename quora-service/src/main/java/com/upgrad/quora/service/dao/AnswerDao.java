package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnswerDao {


    @PersistenceContext
    private EntityManager entityManager;

    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    public AnswerEntity getAnswerById(String uuid) {
        try {
            return entityManager.createNamedQuery("answerById",AnswerEntity.class).setParameter("uuid",uuid).getSingleResult();
        } catch (NoResultException nre) { return null;}
    }

    public void updateAnswer(final AnswerEntity updatedAnswerEntity) {
        entityManager.merge(updatedAnswerEntity);

    }

    public AnswerEntity deleteAnswer(AnswerEntity answerEntity) {
        entityManager.remove(answerEntity);
        return answerEntity;
    }


    public List<AnswerEntity> getAnswersToQuestion(QuestionEntity questionEntity) {

        try {
            return entityManager.createNamedQuery("answerByQuestion",AnswerEntity.class).setParameter("question",questionEntity).getResultList();
        } catch (NoResultException nre) { return null;}
    }
}
