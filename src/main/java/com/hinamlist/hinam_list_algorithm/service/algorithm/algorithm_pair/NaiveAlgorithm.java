package com.hinamlist.hinam_list_algorithm.service.algorithm.algorithm_pair;

import com.hinamlist.hinam_list_algorithm.model.AlgorithmInput;
import com.hinamlist.hinam_list_algorithm.service.algorithm_runner.AbstractAlgorithmRunner;
import com.hinamlist.hinam_list_algorithm.service.common.OutputCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class NaiveAlgorithm extends AbstractAlgorithmPair {

    @Autowired
    public NaiveAlgorithm(OutputCalculator outputCalculator) {
        super(outputCalculator);
    }

    @Override
    public List<Integer> execute(AlgorithmInput algorithmInput, int storeNum1, int storeNum2) {
        List<Integer> bestResult = null;
        float bestResultSum = 0F;
        for (StoreCombinationIterator it = new StoreCombinationIterator(storeNum1, storeNum2, algorithmInput.getBarcodeList().size()); it.hasNext(); ) {
            var output = it.next();

            if (!outputCalculator.isOutputValid(algorithmInput, output))
                continue;

            if (bestResult == null) {
                bestResult = output;
                bestResultSum = outputCalculator.calculateOutputTotalPrice(algorithmInput, output);
            }
            else {
                float currentResultSum = outputCalculator.calculateOutputTotalPrice(algorithmInput, output);
                if (bestResultSum > currentResultSum) {
                    bestResult = output;
                    bestResultSum = currentResultSum;
                }
            }
        }
        return bestResult;
    }
}
