package com.neo.userAPI.repo;

import com.neo.userAPI.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

    public Optional<UserEntity> findByUserName(String userName);

/*    public Optional<UserEntity> findByEmail(String email);*/
}
