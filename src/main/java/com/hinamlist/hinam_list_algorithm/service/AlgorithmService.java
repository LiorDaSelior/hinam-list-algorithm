package com.hinamlist.hinam_list_algorithm.service;

import com.hinamlist.hinam_list_algorithm.model.AlgorithmInput;
import com.hinamlist.hinam_list_algorithm.service.algorithm_runner.IAlgorithmRunner;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@RabbitListener(queues = "${rabbitmq.algorithm-producer.queue}")
public class AlgorithmService {
    IAlgorithmRunner algorithmRunner;
    protected final RabbitTemplate rabbitTemplate;
    protected final String algorithmConsumerExchangeName;

    @Autowired
    public AlgorithmService(IAlgorithmRunner algorithmRunner,
                            @Value("${rabbitmq.algorithm-consumer.exchange}") String algorithmConsumerExchangeName,
                            RabbitTemplate rabbitTemplate) {
        this.algorithmRunner = algorithmRunner;
        this.rabbitTemplate = rabbitTemplate;
        this.algorithmConsumerExchangeName = algorithmConsumerExchangeName;
    }

    @RabbitHandler
    public void execute(Message message) {
        AlgorithmInput algorithmInput = (AlgorithmInput) new SimpleMessageConverter().fromMessage(message);
        MessageProperties messageProperties = message.getMessageProperties();
        Message algorithmMessage = new SimpleMessageConverter().toMessage(algorithmRunner.run(algorithmInput),messageProperties);

        rabbitTemplate.convertAndSend(algorithmConsumerExchangeName, "", algorithmMessage);
    }




}
