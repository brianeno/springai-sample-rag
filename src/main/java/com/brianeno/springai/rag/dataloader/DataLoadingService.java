package com.brianeno.springai.rag.dataloader;

import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@Slf4j
public class DataLoadingService {
    private static final Logger logger = LoggerFactory.getLogger(DataLoadingService.class);

    @Value("classpath:/data/2023-infoq-trends-reports-emag-1703183112474.pdf")
    private Resource documentResource;

    private final VectorStore vectorStore;

    @Autowired
    public DataLoadingService(VectorStore vectorStore) {
        Assert.notNull(vectorStore, "VectorStore must not be null.");
        this.vectorStore = vectorStore;
    }

    public void load() {

        DocumentReader reader = null;
        if (this.documentResource.getFilename() != null) {
            if (this.documentResource.getFilename().endsWith(".pdf")) {
                logger.info("Parsing PDF document");
                reader = new PagePdfDocumentReader(
                    this.documentResource,
                    PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                            .withNumberOfBottomTextLinesToDelete(3)
                            .withNumberOfTopPagesToSkipBeforeDelete(1)
                            .build())
                        .withPagesPerDocument(1)
                        .build());

            } else if (this.documentResource.getFilename().endsWith(".txt")) {
                reader = new TextReader(this.documentResource);
            } else if (this.documentResource.getFilename().endsWith(".json")) {
                reader = new JsonReader(this.documentResource);
            }
        }
        if (reader != null) {
            var textSplitter = new TokenTextSplitter();

            logger.info("Parsing document, splitting, creating embeddings and storing in vector store...  this will take a while.");
            this.vectorStore.accept(
                textSplitter.apply(
                    reader.get()));
            logger.info("Done parsing document, splitting, creating embeddings and storing in vector store");
        } else {
            throw new RuntimeException("No reader found for document");
        }
    }
}
