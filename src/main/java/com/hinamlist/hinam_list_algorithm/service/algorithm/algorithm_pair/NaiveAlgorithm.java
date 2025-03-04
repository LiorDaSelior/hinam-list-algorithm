package com.hinamlist.hinam_list_algorithm.service.algorithm.algorithm_pair;

import com.hinamlist.hinam_list_algorithm.model.AlgorithmInput;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hinamlist.hinam_list_algorithm.service.algorithm_runner.AbstractAlgorithmRunner.calculatePriceByStoreNumList;
import static com.hinamlist.hinam_list_algorithm.service.algorithm_runner.AbstractAlgorithmRunner.isOutputValid;

@Component
public class NaiveAlgorithm implements IAlgorithmPair {

    @Override
    public List<Integer> execute(AlgorithmInput algorithmInput, int storeNum1, int storeNum2) {
        List<Integer> bestResult = null;
        float bestResultSum = 0F;
        for (StoreCombinationIterator it = new StoreCombinationIterator(storeNum1, storeNum2, algorithmInput.getBarcodeList().size()); it.hasNext(); ) {
            var output = it.next();

            if (!isOutputValid(algorithmInput, output))
                continue;

            if (bestResult == null) {
                bestResult = output;
                bestResultSum = calculatePriceByStoreNumList(algorithmInput, output);
            }
            else {
                float currentResultSum = calculatePriceByStoreNumList(algorithmInput, output);
                if (bestResultSum > currentResultSum) {
                    bestResult = output;
                    bestResultSum = currentResultSum;
                }
            }
        }
        return bestResult;
    }
}
