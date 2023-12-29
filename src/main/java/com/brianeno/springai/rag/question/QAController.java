package com.brianeno.springai.rag.question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/question")
public class QAController {

    private final QAService qaService;

    @Autowired
    public QAController(QAService qaService) {
        this.qaService = qaService;
    }

    @GetMapping
    public Map completion(@RequestParam(value = "question", defaultValue = "What is the largest trend of 2023?") String question,
                          @RequestParam(value = "prompstuff", defaultValue = "true") boolean prompstuff) {
        String answer = this.qaService.generate(question, prompstuff);
        Map map = new LinkedHashMap();
        map.put("question", question);
        map.put("answer", answer);
        return map;
    }
}
