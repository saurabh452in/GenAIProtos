package com.pmt.statusInterpreter.aiSvc;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component

public class PaymentDataTool {

    @Autowired
    PmtDataHelperSvc pmtDataHelperSvc;
    private static final Logger log = LoggerFactory.getLogger(PaymentDataTool.class);


    @Tool(
            name = "getPaymentById", description = "Fetch payment details by payment ID"
    )
    public String pmtAgentToolCaller(String input) {
        log.info("Calling PaymentDataTool with input: {}", input);
        return pmtDataHelperSvc.getPaymentById(input);
    }
}
