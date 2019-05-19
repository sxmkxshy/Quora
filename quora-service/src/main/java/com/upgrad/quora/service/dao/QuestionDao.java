package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    public QuestionEntity createQuestion(QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    public List<QuestionEntity> getAllQuestions() {
        try {
            return entityManager.createNamedQuery("allQuestions",QuestionEntity.class).getResultList();
        } catch (NoResultException nre) { return null;}
    }

    public QuestionEntity getQuestionById(String uuid) {
        try {
            return entityManager.createNamedQuery("questionById",QuestionEntity.class).setParameter("uuid",uuid).getSingleResult();
        } catch (NoResultException nre) { return null;}
    }

    public void updateQuestion(final QuestionEntity updatedQuestionEntity) {
        entityManager.merge(updatedQuestionEntity);

    }

    public QuestionEntity deleteQuestion(QuestionEntity questionEntity) {
        entityManager.remove(questionEntity);
        return questionEntity;
    }

    public List<QuestionEntity> getQuestionsByUser(UserEntity userEntity) {

        try {
            return entityManager.createNamedQuery("questionsByUser",QuestionEntity.class).setParameter("user",userEntity).getResultList();
        } catch (NoResultException nre) { return null;}
    }

}

