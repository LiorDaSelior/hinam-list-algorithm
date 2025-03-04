package com.hinamlist.hinam_list_algorithm.service.algorithm_runner;

import com.hinamlist.hinam_list_algorithm.model.AlgorithmInput;

import java.util.*;

public abstract class AbstractAlgorithmRunner implements IAlgorithmRunner{
    public static float calculatePriceByStoreNumList(AlgorithmInput algorithmInput, List<Integer> storeNumList) {
        Set<Integer> usedStoreNumbers = new HashSet<>();
        float sum = 0F;

        for (int i = 0; i < storeNumList.size(); i++) {
            int currentStoreNum = storeNumList.get(i);
            if (!usedStoreNumbers.contains(currentStoreNum)) {
                usedStoreNumbers.add(currentStoreNum);
                sum += algorithmInput.getStoreNumberOrderAddonMap().get(currentStoreNum);
            }

            sum += algorithmInput.getStoreNumberPriceListMap()
                    .get(currentStoreNum)
                    .get(i);
        }
        return sum;
    }

    public static boolean isOutputValid(AlgorithmInput algorithmInput, List<Integer> output) {
        Map<Integer, Float> sumSet = new HashMap<>();
        for (int i = 0; i < output.size(); i++) {
            int outputStoreNum = output.get(i);

            if (algorithmInput.getStoreNumberPriceListMap().get(outputStoreNum).get(i) < 0F)
                return false;

            if (!sumSet.containsKey(outputStoreNum))
                sumSet.put(outputStoreNum,
                        algorithmInput.getStoreNumberOrderAddonMap().get(outputStoreNum) +
                                algorithmInput.getStoreNumberPriceListMap().get(outputStoreNum).get(i));
            else {
                sumSet.put(outputStoreNum,
                        sumSet.get(outputStoreNum) +
                                algorithmInput.getStoreNumberPriceListMap().get(outputStoreNum).get(i));
            }
        }

        for (var entry : sumSet.entrySet()) {
            if (entry.getValue() < algorithmInput.getStoreNumberLowerLimitMap().get(entry.getKey()))
                return false;
        }

        return true;
    }
}
