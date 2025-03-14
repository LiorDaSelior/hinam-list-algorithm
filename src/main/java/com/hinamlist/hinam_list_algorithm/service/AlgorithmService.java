package com.hinamlist.hinam_list_algorithm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hinamlist.hinam_list_algorithm.model.AlgorithmInput;
import com.hinamlist.hinam_list_algorithm.service.algorithm_runner.IAlgorithmRunner;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class AlgorithmService {

    IAlgorithmRunner algorithmRunner;
    protected final RabbitTemplate rabbitTemplate;
    protected final String algorithmConsumerExchangeName;
    protected final Jackson2JsonMessageConverter messageConverter;

    @Autowired
    public AlgorithmService(IAlgorithmRunner algorithmRunner,
                            @Value("${rabbitmq.algorithm-consumer.exchange}") String algorithmConsumerExchangeName,
                            RabbitTemplate rabbitTemplate,
                            Jackson2JsonMessageConverter messageConverter) {
        this.algorithmRunner = algorithmRunner;
        this.rabbitTemplate = rabbitTemplate;
        this.algorithmConsumerExchangeName = algorithmConsumerExchangeName;
        this.messageConverter = messageConverter;
    }

    @RabbitListener(queues = "${rabbitmq.algorithm-producer.queue}")
    public void execute(Message message) throws JsonProcessingException {
        Object messageJson = messageConverter.fromMessage(message);
        ObjectMapper objectMapper = new ObjectMapper();
        AlgorithmInput algorithmInput = objectMapper.readValue(objectMapper.writeValueAsString(messageJson), AlgorithmInput.class);
        //AlgorithmInput algorithmInput = (AlgorithmInput) new Jackson2JsonMessageConverter().fromMessage(message);
        var output = algorithmRunner.run(algorithmInput);
        MessageProperties messageProperties = message.getMessageProperties();
        Message algorithmMessage = new SimpleMessageConverter().toMessage(output,messageProperties);

        rabbitTemplate.convertAndSend(algorithmConsumerExchangeName, "", algorithmMessage);
    }




}
