package com.hinamlist.hinam_list_algorithm.service.algorithm_runner;

import com.hinamlist.hinam_list_algorithm.model.AlgorithmInput;

import java.util.Map;

public interface IAlgorithmRunner {
    public Map<String, Integer> run(AlgorithmInput algorithmInput);
}
