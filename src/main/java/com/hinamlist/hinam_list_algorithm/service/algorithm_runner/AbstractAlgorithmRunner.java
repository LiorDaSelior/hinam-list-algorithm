package com.hinamlist.hinam_list_algorithm.service.algorithm_runner;

import com.hinamlist.hinam_list_algorithm.model.AlgorithmInput;
import com.hinamlist.hinam_list_algorithm.service.common.OutputCalculator;

import java.util.*;
import java.util.stream.IntStream;

public abstract class AbstractAlgorithmRunner implements IAlgorithmRunner{
    protected final OutputCalculator outputCalculator;
    public AbstractAlgorithmRunner(OutputCalculator outputCalculator)
    {
        this.outputCalculator=outputCalculator;
    }

    public Map<String, Integer> run(AlgorithmInput algorithmInput) {
        List<Integer> algorithmResult = runAlgorithm(algorithmInput);
        Map<String, Integer> barcodeStoreNumMap = new HashMap<>();
        IntStream.range(0, algorithmInput.getBarcodeList().size()).forEach(index ->
                barcodeStoreNumMap.put(
                        algorithmInput.getBarcodeList().get(index),
                        algorithmResult.get(index)
                )
        );
        return barcodeStoreNumMap;
    }

    public abstract List<Integer> runAlgorithm(AlgorithmInput algorithmInput);

}
