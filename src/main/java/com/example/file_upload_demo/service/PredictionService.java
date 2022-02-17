package com.example.file_upload_demo.service;

import com.example.file_upload_demo.repository.IPredictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PredictionService implements IPredictionService{

    private IPredictionRepository predictionRepos;

    @Autowired
    public PredictionService(IPredictionRepository predictionRepos){
        this.predictionRepos = predictionRepos;
    }

    @Override
    public void registerPre(String path, int prediction){
        int n = predictionRepos.insert(path, prediction);
        System.out.println("記録行数" + n);
    }
}
