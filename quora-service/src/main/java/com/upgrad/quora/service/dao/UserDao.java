package com.upgrad.quora.service.dao;


//import com.upgrad.quora.service.entity.UserAuthTokenEntity;

import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

//import javax.persistence.NoResultException;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public UserEntity createUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }

   public UserEntity emailValidation(final String email) {
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserEntity userNameValidation(final String username) {
        try {
            return entityManager.createNamedQuery("userByUserName", UserEntity.class).setParameter("username", username).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserEntity getUser(final String uuid) {
        try {
            return entityManager.createNamedQuery("userById", UserEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }


    public UserEntity getUserByUsername(final String username) {
        try {
            return entityManager.createNamedQuery("userByUserName", UserEntity.class).setParameter("username", username).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
    public UserAuthTokenEntity createAuthToken(final UserAuthTokenEntity userAuthTokenEntity) {
        entityManager.persist(userAuthTokenEntity);
        return userAuthTokenEntity;
    }

    public void updateUser(final UserEntity updatedUserEntity) {
        entityManager.merge(updatedUserEntity);
    }

    public UserAuthTokenEntity getUserAuthToken(final String accessToken) {
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthTokenEntity.class).setParameter("accessToken", accessToken).getSingleResult();
        } catch (NoResultException nre) { return null;}

    }

    @Transactional
    public void updateUserToken(final UserAuthTokenEntity updatedUserTokenEntity) {
        entityManager.merge(updatedUserTokenEntity);
    }

    public UserEntity deleteUser(UserEntity userEntity) {
        entityManager.remove(userEntity);
        return userEntity;
    }
}
