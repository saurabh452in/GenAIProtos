package com.pmt.statusInterpreter.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {


    @Bean(name = "simpleChatClient")
    public ChatClient simpleChatClient(@Autowired OllamaChatModel chatModel, @Autowired ChatMemory chatMemory) {

        return ChatClient.builder(chatModel).

                build();
    }

    @Bean(name = "inMemoryChatClient")
    public ChatClient inMemoryChatClient(@Autowired OllamaChatModel chatModel, @Autowired ChatMemory chatMemory) {

        return ChatClient.builder(chatModel).defaultAdvisors(PromptChatMemoryAdvisor.builder(chatMemory).

                build()).

                build();


        /*return ChatClient.builder(chatModel)
                .
                defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).

                        build()).

                build();*/
    }

}
