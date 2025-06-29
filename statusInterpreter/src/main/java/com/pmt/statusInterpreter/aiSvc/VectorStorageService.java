package com.pmt.statusInterpreter.aiSvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.ArrayList;
import java.util.List;

@Service
public class VectorStorageService {
    private static final Logger log = LoggerFactory.getLogger(VectorStorageService.class);
    @Autowired
    VectorStore vectorStore;

    public void storeVector(String message) {
        // Convert the message into a Document object
        TokenTextSplitter textSplitter = new TokenTextSplitter();

        Document document = new Document(message);
        List<Document> documents = new ArrayList<>();
        documents.add(document);

        vectorStore.add(textSplitter.split(documents));

        log.info("added document to vector store: {}", message);
    }




    List<Document> findInVectorStore(String txId) {
        SearchRequest searchRequest = SearchRequest.builder()
                .query(txId)
                .topK(200)
                .build();
        List<Document> docs = vectorStore.similaritySearch(searchRequest);
        return docs;
    }
}
