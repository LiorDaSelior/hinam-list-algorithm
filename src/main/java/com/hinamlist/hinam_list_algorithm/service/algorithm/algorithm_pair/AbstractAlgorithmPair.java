package com.hinamlist.hinam_list_algorithm.service.algorithm.algorithm_pair;

import com.hinamlist.hinam_list_algorithm.service.common.OutputCalculator;

public abstract class AbstractAlgorithmPair implements IAlgorithmPair{
    protected final OutputCalculator outputCalculator;
    public AbstractAlgorithmPair(OutputCalculator outputCalculator)
    {
        this.outputCalculator=outputCalculator;
    }
}
