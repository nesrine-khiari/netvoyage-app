package tn.fst.spring.netvoyage.services.implementations;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.stereotype.Service;
import tn.fst.spring.netvoyage.services.interfaces.ISentimentService;

import java.util.Properties;

@Service
public class SentimentServiceImpl implements ISentimentService {

    private final StanfordCoreNLP pipeline;

    public SentimentServiceImpl() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,parse,sentiment");
        this.pipeline = new StanfordCoreNLP(props);
    }

    @Override
    public String analyzeSentiment(String text) {
        CoreDocument doc = new CoreDocument(text);
        pipeline.annotate(doc);

        int totalSentiment = 0;
        for (CoreSentence sentence : doc.sentences()) {
            totalSentiment += sentimentToScore(sentence.sentiment());
        }

        int avgSentiment = totalSentiment / doc.sentences().size();
        return scoreToSentiment(avgSentiment);
    }

    private int sentimentToScore(String sentiment) {
        switch (sentiment) {
            case "Very negative": return 0;
            case "Negative": return 1;
            case "Neutral": return 2;
            case "Positive": return 3;
            case "Very positive": return 4;
            default: return 2;
        }
    }

    private String scoreToSentiment(int score) {
        switch (score) {
            case 0: return "Very Negative";
            case 1: return "Negative";
            case 2: return "Neutral";
            case 3: return "Positive";
            case 4: return "Very Positive";
            default: return "Neutral";
        }
    }
}