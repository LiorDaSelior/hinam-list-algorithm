package com.hinamlist.hinam_list_algorithm.service.algorithm_runner.algorithm_pair;

import com.hinamlist.hinam_list_algorithm.model.AlgorithmInput;
import com.hinamlist.hinam_list_algorithm.service.algorithm.algorithm_pair.IAlgorithmPair;
import com.hinamlist.hinam_list_algorithm.service.algorithm_runner.AbstractAlgorithmRunner;
import com.hinamlist.hinam_list_algorithm.service.common.OutputCalculator;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AlgorithmPairRunner extends AbstractAlgorithmRunner {
    private final IAlgorithmPair algorithm;

    @Autowired
    public AlgorithmPairRunner(OutputCalculator outputCalculator, IAlgorithmPair algorithm) {
        super(outputCalculator);
        this.algorithm = algorithm;
    }


    protected List<ImmutablePair<Integer, Integer>> getValidPairs(AlgorithmInput algorithmInput) {
        List<Integer> storeNumberList = algorithmInput.getStoreNumberPriceListMap().keySet().stream().toList();
        List<ImmutablePair<Integer, Integer>> validPairList = new ArrayList<>();
        for (int i = 0; i < storeNumberList.size(); i++) {
            for (int j = i + 1; j < storeNumberList.size(); j++) {
                if (checkPairValidity(algorithmInput, storeNumberList.get(i), storeNumberList.get(j))) {
                    validPairList.add(new ImmutablePair<>(storeNumberList.get(i), storeNumberList.get(j)));
                }
            }
        }
        return validPairList;
    }

    protected boolean checkPairValidity(AlgorithmInput algorithmInput, int i, int j) {
        var priceMap = algorithmInput.getStoreNumberPriceListMap();
        for (int k = 0; k < algorithmInput.getBarcodeList().size(); k++) {
            if (priceMap.get(i).get(k) < 0 && priceMap.get(j).get(k) < 0) {
                return false;
            }
        }
        return true;
    }

    // assumption: pair is valid
    protected List<Integer> naiveOutputFunction(AlgorithmInput algorithmInput, int i, int j) {
        List<Integer> naiveOutput = new ArrayList<>();
        var priceMap = algorithmInput.getStoreNumberPriceListMap();
        for (int k = 0; k < algorithmInput.getBarcodeList().size(); k++) {
            if (priceMap.get(i).get(k) < 0F) {
                naiveOutput.add(j);
            }
            else if (priceMap.get(j).get(k) < 0F) {
                naiveOutput.add(i);
            }
            else {
                if (priceMap.get(i).get(k) < priceMap.get(j).get(k))
                    naiveOutput.add(i);
                else
                    naiveOutput.add(j);
            }
        }
        return naiveOutput;
    }

    private List<Integer> getBestOutput(AlgorithmInput algorithmInput , List<ImmutablePair<Integer, Integer>> validPairList, boolean isCheckValidity, AlgorithmPairOutputFunction outputFunction) {
        List<Integer> bestResult = null;
        float bestResultSum = 0F;

        for (var pair : validPairList) {
            var currentResult = outputFunction.output(
                    algorithmInput,
                    pair.getKey(),
                    pair.getValue()
            );

            if (isCheckValidity && !outputCalculator.isOutputValid(algorithmInput, currentResult))
                continue;

            if (bestResult == null) {
                bestResult = currentResult;
                bestResultSum = outputCalculator.calculateOutputTotalPrice(algorithmInput, currentResult);
            }
            else {
                float currentResultSum = outputCalculator.calculateOutputTotalPrice(algorithmInput, currentResult);
                if (bestResultSum > currentResultSum) {
                    bestResult = currentResult;
                    bestResultSum = currentResultSum;
                }
            }
        }
        return bestResult;
    }

    @Override
    public List<Integer> runAlgorithm(AlgorithmInput algorithmInput) {
        List<Integer> bestResult = null;

        List<ImmutablePair<Integer, Integer>> validPairList = getValidPairs(algorithmInput);
        if (validPairList.isEmpty()) {
            return null;
        }

        bestResult = getBestOutput(algorithmInput, validPairList, true, this::naiveOutputFunction);
        if (bestResult != null)
            return bestResult;

        bestResult = getBestOutput(algorithmInput, validPairList, false, algorithm::execute);

        return bestResult;
    }
}
