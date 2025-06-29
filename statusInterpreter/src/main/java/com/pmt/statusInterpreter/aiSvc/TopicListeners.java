package com.pmt.statusInterpreter.aiSvc;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class TopicListeners {

    private static final Logger logger = LoggerFactory.getLogger(TopicListeners.class);
    @Autowired
    VectorStorageService vectorStorageService;

/*
    @KafkaListener( groupId = "kafka-log-topic-CG", concurrency = "1" ,
            topicPartitions =
                    {
                            @TopicPartition(topic = "kafka-log-topic",
                                    partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0")),
                            @TopicPartition(topic = "kafka-log-topic",
                                    partitionOffsets = @PartitionOffset(partition = "1", initialOffset = "0"))
                    })*/



    @KafkaListener(topics = "kafka-log-topic", groupId = "kafka-log-topic-CG", concurrency = "1" )
    public void listen1(@Payload String message, Acknowledgment acknowledgment) {
        logger.info("Received message from kafka-log-topic: {}", message);
        vectorStorageService.storeVector(message);

        acknowledgment.acknowledge();
    }


}

