package com.neo.userAPI;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends MongoRepository<TodoEntity, String> {

    @Override
    <S extends TodoEntity> S save(S entity);

    List<TodoEntity> findAllByUserId(Long userId);


    /*List<TodoEntity> findAllBy_id(ObjectId id);*/


/*    void deleteBy_id(ObjectId objectId);*/

    @Override
    void delete(TodoEntity entity);
}
