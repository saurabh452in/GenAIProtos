package com.pmt.statusInterpreter.aiSvc;

import jakarta.annotation.PostConstruct;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@Service

public class PmtAgentToolCallerSvc {

    private final ChatClient inMemoryChatClient;
    ChatClient chatClient;

    ChatMemory chatMemory;

     ;

    private static final String PROMPT_INSTR =
            "You'll act as an agent to help the user with questions on the paymentId number provided by user ." ;
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


    public String askQuestion(String userQuestion,String userId) {


        SystemMessage systemMessage = new SystemMessage(PROMPT_INSTR);
        Prompt prompt = new Prompt(systemMessage, new UserMessage(userQuestion));

        /*var response = chatClient.prompt(prompt)
                .tools(paymentDataTool).call().chatResponse();
*/
       var responseUsingMemory = inMemoryChatClient.prompt(prompt)
               .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, userId))
                .tools(paymentDataTool).call().chatResponse();

        return responseUsingMemory.getResult().getOutput().getText();

    }


}
