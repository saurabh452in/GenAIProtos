package com.pmt.statusInterpreter.aiSvc;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class PmtRagService {

    @Autowired
    PmtDataHelperSvc pmtDataHelperSvc;



    private static final String PROMPT_PAYMENT_DATA_INSTRUCTIONS = """        
    The object `payment_data` below represents the payment transaction data.
    
    Use the information in `payment_data` to answer the `user_main_prompt` below.
    If you are not able to data relevant to the transaction number specified in the user_main_prompt then 
     respond with "unable to find data". Don't include any other information in your response.
                
    `payment_data`:
        
""";


     private static final String structuredTxnResponse = "Your response should only have digits and no other characters.  " +
             "If you are unable to find the paymentId, please respond with blank string " ;


    private static final String CURRENT_PROMPT_INSTRUCTIONS2 = """
        extract the paymentId from the `user_main_prompt` and use it to retrieve the payment data.
        Your response should only have digits and no other characters. If you are unable to find the paymentId, please respond with blank string.
        Here's the `user_main_prompt`:
        """;

    private static final String CURRENT_PROMPT_INSTRUCTIONS = """

        Here's the `user_main_prompt`:
        """;

    private static final String PROMPT_INSTR = """
    Here are the general guidelines to answer the `user_main_prompt`
    You'll act as an agent to help the user with questions on the paymentId number provided by user .
""";

    private final OllamaChatModel chatModel;

    public PmtRagService(@Autowired OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String askQuestion(String userQuestion) {
        var generalInstructionsSystemMessage = new SystemMessage(PROMPT_INSTR);
        var prompt = new Prompt(
                new UserMessage(CURRENT_PROMPT_INSTRUCTIONS2.concat(userQuestion))
                );
        String txnId = chatModel.call(prompt).getResult().getOutput().getText();

        String paymentData = pmtDataHelperSvc.getPaymentById(txnId.trim());
        ;
        var generalInstructionsSystemMessage2 = new SystemMessage(PROMPT_PAYMENT_DATA_INSTRUCTIONS.concat(paymentData));

        var currentPromptMessage = new UserMessage(CURRENT_PROMPT_INSTRUCTIONS.concat(userQuestion));

        prompt = new Prompt(List.of(generalInstructionsSystemMessage, generalInstructionsSystemMessage2,currentPromptMessage));
        var response = chatModel.call(prompt).getResult().getOutput().getText();

        return response;

    }


public String askQuestion2(String userQuestion) {
    // Step 1: Extract transaction ID from user question
    var extractTxnIdSystemMessage = new SystemMessage("""
        You are an agent that extracts payment transaction numbers from user prompts.
        Only return the transaction number as digits. If not found, return a blank string.
    """);
    var extractTxnIdPrompt = new Prompt(List.of(
            extractTxnIdSystemMessage,
            new UserMessage(userQuestion)
    ));
    String txnId = chatModel.call(extractTxnIdPrompt).getResult().getOutput().getText().trim();

    // Step 2: Retrieve payment data
    String paymentData = pmtDataHelperSvc.getPaymentById(txnId);

    // Step 3: Answer the user's question using payment data
    var answerSystemMessage = new SystemMessage(PROMPT_PAYMENT_DATA_INSTRUCTIONS + paymentData);
    var answerPrompt = new Prompt(List.of(
            answerSystemMessage,
            new UserMessage(userQuestion)
    ));
    var response = chatModel.call(answerPrompt).getResult().getOutput().getText();

    return response;
}

}

