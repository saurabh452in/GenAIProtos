package com.pmt.statusInterpreter.controllers;


import com.pmt.statusInterpreter.aiSvc.PmtAgentToolCallerSvc;
import com.pmt.statusInterpreter.aiSvc.PmtRagService;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/pmtStatusInterpreter")
public class ChatController {

    @Autowired
    private final PmtRagService pmtRagService = null;

    @Autowired
    private final PmtAgentToolCallerSvc pmtAgentToolCallerSvc =null;

    @Autowired
    private final OllamaChatModel chatModel = null;

    // all-arg constructor

    @PostMapping("/ragChat")
    public ResponseEntity<Void> ragChat(@RequestParam String userQuestion) {
        var chatResponse = pmtRagService.askQuestion(userQuestion);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/chatWithAgent")
    public ResponseEntity<String> chatWithAgent(@RequestParam String userQuestion) {
        var chatResponse = pmtAgentToolCallerSvc.askQuestion(userQuestion);
        return new ResponseEntity<String>(chatResponse, HttpStatus.OK);
    }

    @GetMapping("/ai/generate")
    public Map<String, String> generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return Map.of("generation", this.chatModel.call(message));
    }

    @GetMapping("/ai/generateStream")
    public Flux<String> generateStream(@RequestParam(value = "message",
            defaultValue = "Tell me a 500 words sci fi story") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return this.chatModel.stream(prompt)
                .map(chatResponse -> chatResponse.getResult().getOutput().getText())
                .delayElements(Duration.ofMillis(500));
    }
}

