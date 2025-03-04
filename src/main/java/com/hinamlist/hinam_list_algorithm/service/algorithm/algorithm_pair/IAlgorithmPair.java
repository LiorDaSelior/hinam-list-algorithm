package com.hinamlist.hinam_list_algorithm.service.algorithm.algorithm_pair;

import com.hinamlist.hinam_list_algorithm.model.AlgorithmInput;

import java.util.List;

public interface IAlgorithmPair {
    public List<Integer> execute(AlgorithmInput algorithmInput, int storeNum1, int storeNum2);
}
