package com.pmt.statusInterpreter.aiSvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.List;

@Component

public class PaymentDataTool {

    @Autowired
    PmtDataHelperSvc pmtDataHelperSvc;
    private static final Logger log = LoggerFactory.getLogger(PmtAgentToolCallerSvc.class);


    @Tool(
            name = "getPaymentById", description = "get payment details by paymentId from database "
    )
    public String pmtAgentToolCaller(String input) {
        log.info("Calling PaymentDataTool to get data from db with input: {}", input);
        return pmtDataHelperSvc.getPaymentById(input);
    }


    @Tool(
            name = "getPaymentDetailsFromLogs", description = "fetch details about paymentId from logs"
    )
    public List<String> getPaymentDetailsFromLogs(String input) {
        log.info("Calling PaymentDataTool to get data from logs with input: {}", input);

        String filePath = "D:\\saura\\work\\intellijWorkspace\\logs\\pmtprocessor.log";

        return searchLogFileParallel(filePath, input);
    }



    public List<String> searchLogFileParallel(String filePath, String searchString) {
        try {
            return Files.lines(Paths.get(filePath))
                    .parallel()
                    .filter(line -> line.contains(searchString))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            // Handle exception or log error
            return List.of();
        }
    }

    public List<String> searchLogFile(String filePath, String searchString) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return reader.lines()
                    .filter(line -> line.contains("paymentId='" + searchString + "'"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            // Handle exception or log error
            return List.of();
        }
    }
}
