package com.pmt.statusInterpreter.aiSvc;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service

public class PmtAgentToolCallerSvc {

    private final ChatClient inMemoryChatClient;
    ChatClient chatClient;

    ChatMemory chatMemory;

    private static final String SYSTEM_PROMPT_INSTR =
            "You are an AI assistant who will help answer the user's questions on the paymentId number provided by user. " +
                    "Pick the paymentId from the user's question." +
                    "Make use of both the tools getPaymentDetailsFromLogs tool and getPaymentById tool provided to you " +
                    "to gather details about the paymentId.Don't include any information about how you are gathering the information about the " +
                    "paymentId in the final response.";


    private static final String SYSTEM_PROMPT_INSTR_2 =
            "You are an AI assistant who will help answer the user's questions on the paymentId number provided by user. " +
                    " Pick the paymentId from the user's question." +
                    " Make use of both the following tools provided to you to gather details about the paymentId." +
                    " getPaymentDetailsFromVectorStore tool retrieves information from vector store " +
                    " getPaymentById tool retrieves information from api" +
                    " Don't include any information about how you are gathering the information about the " +
                    " paymentId in the final response. Only provide answer to the user's question from the information retrieved from the tools." +
                    " Don't include anything else in the response. If you are not able to find any information about the paymentId, just say I cannot find relevant information" ;


    private static final Logger log = LoggerFactory.getLogger(PmtAgentToolCallerSvc.class);

    @Autowired
    PaymentDataTool paymentDataTool;

    private final OllamaChatModel chatModel;

    public PmtAgentToolCallerSvc(@Autowired OllamaChatModel chatModel, @Autowired
                                 ChatMemory chatMemory, @Qualifier("simpleChatClient") ChatClient chatClient,
                                 @Qualifier("inMemoryChatClient")
                                 ChatClient inMemoryChatClient) {
        this.chatModel = chatModel;
        this.chatMemory = chatMemory;
        this.chatClient = chatClient;
        this.inMemoryChatClient = inMemoryChatClient;

    }


    public String askQuestion(String userQuestion, String userId) {

        SystemMessage systemMessage = new SystemMessage(SYSTEM_PROMPT_INSTR_2);
        Prompt prompt = new Prompt(systemMessage, new UserMessage(userQuestion));

        var responseUsingMemory = inMemoryChatClient.prompt(prompt)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, userId))
                .tools(paymentDataTool).call().chatResponse();

        return responseUsingMemory.getResult().getOutput().getText();

    }


}
