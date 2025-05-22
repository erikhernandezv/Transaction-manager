package co.erikhdez.transactionservice.event;

import co.erikhdez.transactionservice.dto.TransactionRequestDTO;
import co.erikhdez.transactionservice.model.Transaction;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EventPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbit.queue.name}")
    private String queueName;

    @Value("${rabbit.exchange.name}")
    private String exchangeName;

    @Value("${rabbit.routing.key}")
    private String routingKey;

    public EventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendEvent(TransactionRequestDTO transaction) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, transaction);
    }
}
