package com.hinamlist.hinam_list_algorithm.algorithm.algorithm_pair;

import com.hinamlist.hinam_list_algorithm.model.AlgorithmInput;
import com.hinamlist.hinam_list_algorithm.service.algorithm.algorithm_pair.NaiveAlgorithm;
import org.assertj.core.internal.Integers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hinamlist.hinam_list_algorithm.service.algorithm_runner.AbstractAlgorithmRunner.calculatePriceByStoreNumList;
import static com.hinamlist.hinam_list_algorithm.service.algorithm_runner.AbstractAlgorithmRunner.isOutputValid;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class NaiveAlgorithmTest {
    public static Stream<Arguments> getLowerLimitTestCases() {
        List<String> barcodeList = Arrays.asList("00","01", "02", "03");

        Map<Integer, List<Float>> storeNumberPriceListMap = new HashMap<>();
        storeNumberPriceListMap.put(1, Arrays.asList(3F, 2.1F, 20F, 30F));
        storeNumberPriceListMap.put(2, Arrays.asList(2F, 2F, 10F, 40F));

        Map<Integer, Float> storeNumberOrderAddonMap = Map.of(1, 0F, 2, 0F);
        return Stream.of(
                Arguments.of(
                        "Naive is valid",
                        new AlgorithmInput(
                                barcodeList,
                                storeNumberPriceListMap,
                                Map.of(1, 0F, 2, 0F),
                                storeNumberOrderAddonMap
                        ),
                        Arrays.asList(2, 2, 2, 1)
                ),
                Arguments.of(
                        "Optimal solution exist #1",
                        new AlgorithmInput(
                                barcodeList,
                                storeNumberPriceListMap,
                                Map.of(1, 31F, 2, 0F),
                                storeNumberOrderAddonMap
                        ),
                        Arrays.asList(2, 1, 2, 1)
                ),
                Arguments.of(
                        "Optimal solution exist #2",
                        new AlgorithmInput(
                                barcodeList,
                                storeNumberPriceListMap,
                                Map.of(1, 32.5F, 2, 0F),
                                storeNumberOrderAddonMap
                        ),
                        Arrays.asList(1, 2, 2, 1)
                ),
                Arguments.of(
                        "No optimal solution exist",
                        new AlgorithmInput(
                                barcodeList,
                                storeNumberPriceListMap,
                                Map.of(1, 31F, 2, 13F),
                                storeNumberOrderAddonMap
                        ),
                        Arrays.asList(2, 2, 2, 2)
                )
        );
    }


    @ParameterizedTest(name = "{0}")
    @MethodSource("getLowerLimitTestCases")
    public void lowerLimitTest(String name, AlgorithmInput algorithmInput, List<Integer> expectedResult) {

        var result = new NaiveAlgorithm().execute(algorithmInput, 1, 2);
        assertEquals(expectedResult, result);
    }

}
