package com.hinamlist.hinam_list_algorithm.service.algorithm_runner;

import com.hinamlist.hinam_list_algorithm.model.AlgorithmInput;
import com.hinamlist.hinam_list_algorithm.model.AlgorithmOutput;

import java.util.List;

public interface IAlgorithmRunner {
    public List<Integer> run(AlgorithmInput algorithmInput);
}
