package com.pmt.statusInterpreter.controllers;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class SimpleInMemoryChatController {


    private final ChatClient chatClient;

    private ChatClient inMemoryChatClient = null;

    @Autowired
    private final ChatMemory chatMemory;

    public SimpleInMemoryChatController(@Qualifier("simpleChatClient")ChatClient chatClient,
                                        @Qualifier("inMemoryChatClient")ChatClient inMemoryChatClient,
                                        ChatMemory chatMemory) {
        this.chatClient = chatClient;
        this.chatMemory = chatMemory;
        this.inMemoryChatClient = inMemoryChatClient;
    }

    // Endpoint to send messages and get responses


    // Endpoint to view conversation history
    @GetMapping("/history")
    public List<Message> getHistory() {

        return chatMemory.get("default");
    }

    // Endpoint to clear conversation history
    @DeleteMapping("/history")
    public String clearHistory() {
        chatMemory.clear("default");

        return "Conversation history cleared";
    }
}
