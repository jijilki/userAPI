package com.neo.userAPI.repo;

import com.neo.userAPI.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

    public List<UserEntity> findByUserName(String userName);
}
