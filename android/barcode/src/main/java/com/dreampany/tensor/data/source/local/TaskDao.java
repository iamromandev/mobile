package com.dreampany.tensor.data.source.local;

import androidx.room.Dao;
import androidx.room.Query;

import com.dreampany.framework.data.source.room.dao.BaseDao;
import com.dreampany.tensor.data.model.Task;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface TaskDao extends BaseDao<Task> {

    @Query("select count(*) from task")
    int count();

    @Query("select * from task")
    Flowable<List<Task>> getTasks();

    @Query("select * from task where id = :id limit 1")
    Flowable<Task> getTaskById(String id);

    @Query("update task set completed = :completed WHERE id = :id")
    void updateCompleted(String id, boolean completed);

    @Query("delete from task where id = :id")
    int deleteById(String id);

    @Query("delete from task")
    void deleteAll();

    @Query("delete from task where completed = 1")
    int deleteCompleted();

}
