package com.hinamlist.hinam_list_algorithm.service;

import com.hinamlist.hinam_list_algorithm.model.AlgorithmInput;
import com.hinamlist.hinam_list_algorithm.model.AlgorithmOutput;
import com.hinamlist.hinam_list_algorithm.service.algorithm.IAlgorithm;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RabbitListener(queues = "${rabbitmq.algorithm-producer.queue}")
public class AlgorithmService {
    IAlgorithm algorithm;
    protected final RabbitTemplate rabbitTemplate;
    protected final String algorithmConsumerExchangeName;

    @Autowired
    public AlgorithmService(IAlgorithm algorithm,
                            @Value("${algorithm-consumer-exchange}") String algorithmConsumerExchangeName,
                            RabbitTemplate rabbitTemplate) {
        this.algorithm = algorithm;
        this.rabbitTemplate = rabbitTemplate;
        this.algorithmConsumerExchangeName = algorithmConsumerExchangeName;
    }

    @RabbitHandler
    public void runAlgorithm(Message message) {
        AlgorithmInput algorithmInput = (AlgorithmInput) new SimpleMessageConverter().fromMessage(message);
        
        List<Integer> storeNumberList = algorithmInput.getStoreNumberPriceListMap().keySet().stream().toList();

        float[][] dataMatrix = new float[algorithmInput.getBarcodeList().size()][storeNumberList.size()];
        float[] lowerLimitArray = new float[storeNumberList.size()];
        float[] addonArray = new float[storeNumberList.size()];

        for (int i = 0; i < storeNumberList.size(); i++) {
            int storeNumber = storeNumberList.get(i);
            lowerLimitArray[i] = algorithmInput.getStoreNumberLowerLimitMap().get(storeNumber);
            addonArray[i] = algorithmInput.getStoreNumberOrderAddonMap().get(storeNumber);
            for (int j = 0; j < algorithmInput.getBarcodeList().size(); j++) {
                dataMatrix[i][j] = algorithmInput.getStoreNumberPriceListMap().get(storeNumber).get(j);
            }
        }
        
        int[] resultArray = algorithm.run(dataMatrix, lowerLimitArray, addonArray);
        Map<String, Integer> result = new HashMap<>();
        IntStream.range(0, algorithmInput.getBarcodeList().size()).forEach(
                index -> result.put(
                        algorithmInput.getBarcodeList().get(index),
                        resultArray[index]
                )
        );

        AlgorithmOutput algorithmOutput = new AlgorithmOutput(result);

        MessageProperties messageProperties = message.getMessageProperties();
        Message algorithmMessage = new SimpleMessageConverter().toMessage(algorithmOutput,messageProperties);

        rabbitTemplate.convertAndSend(algorithmConsumerExchangeName, "", algorithmMessage);
    }




}
