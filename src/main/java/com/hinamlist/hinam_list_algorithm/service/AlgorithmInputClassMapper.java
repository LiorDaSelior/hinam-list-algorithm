package com.hinamlist.hinam_list_algorithm.service;

import com.hinamlist.hinam_list_algorithm.model.AlgorithmInput;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AlgorithmInputClassMapper extends DefaultClassMapper {
    AlgorithmInputClassMapper() {
        super();
        Map<String, Class<?>> newIdClassMapping = new HashMap<>();
        newIdClassMapping.put("com.hinamlist.hinam_list_web.model.algorithm_converter.AlgorithmInput", AlgorithmInput.class);
        setIdClassMapping(newIdClassMapping);
    }
}
