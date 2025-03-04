package com.hinamlist.hinam_list_algorithm.service.algorithm_runner.algorithm_pair;

import com.hinamlist.hinam_list_algorithm.model.AlgorithmInput;

import java.util.List;

public interface AlgorithmPairOutputFunction {
    List<Integer> output(AlgorithmInput algorithmInput, int i, int j);
}
