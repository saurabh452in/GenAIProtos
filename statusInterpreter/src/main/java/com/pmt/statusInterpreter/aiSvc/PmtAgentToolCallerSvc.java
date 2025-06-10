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
            "You'll act as an agent to help the user with questions on the paymentId number provided by user . " +
                    "Use both the tools getPaymentDetailsFromLogs tool and getPaymentById tool to retrieve the payment details. " ;
    private static final Logger log = LoggerFactory.getLogger(PmtAgentToolCallerSvc.class);

    @Autowired
    PaymentDataTool paymentDataTool;

    private final OllamaChatModel chatModel;

    public PmtAgentToolCallerSvc(@Autowired OllamaChatModel chatModel, @Autowired
    ChatMemory chatMemory, @Qualifier("simpleChatClient") ChatClient chatClient,
                                 @Qualifier("inMemoryChatClient")
                                 ChatClient inMemoryChatClient ) {
        this.chatModel = chatModel;
        this.chatMemory = chatMemory;
        this.chatClient = chatClient;
        this.inMemoryChatClient = inMemoryChatClient;

    }


    public String askQuestion(String userQuestion,String userId) {


        SystemMessage systemMessage = new SystemMessage(SYSTEM_PROMPT_INSTR);
        Prompt prompt = new Prompt(systemMessage, new UserMessage(userQuestion));

        /*var response = chatClient.prompt(prompt)
                .tools(paymentDataTool).call().chatResponse();*/
       var responseUsingMemory = inMemoryChatClient.prompt(prompt)
               .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, userId))
                .tools(paymentDataTool).call().chatResponse();

        return responseUsingMemory.getResult().getOutput().getText();

    }


}
