package com.example.file_upload_demo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PredictionRepository implements IPredictionRepository{

    private final JdbcTemplate jdbc;

    @Autowired
    public PredictionRepository(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }

    @Override
    public int insert(String path, int prediction){
        var sql = "insert into file_classification values (?,?)";
        var n = jdbc.update(sql, path, prediction);
        return n;
    }
}
