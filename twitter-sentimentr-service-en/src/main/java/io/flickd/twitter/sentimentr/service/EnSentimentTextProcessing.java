package io.flickd.twitter.sentimentr.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.twitter.Extractor;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
 
@Service
public class EnSentimentTextProcessing {
    static StanfordCoreNLP pipeline;

    @PostConstruct
    public static void init() {
        pipeline = new StanfordCoreNLP("nlp.properties");
    }
  
	public int calculateSentiment(String text)
	{
		List<String> list;
		Extractor extractor = new Extractor();
		
		String temp = text;

		// Clear Tweet for sentiment analytics
		list = extractor.extractURLs(temp);	
		for (String i : list) {
			temp = StringUtils.delete(temp, i);
		}
		
		list = extractor.extractHashtags(temp);		
		for (String i : list) {
			temp = StringUtils.delete(temp, "#"+i);
		}

		list = extractor.extractMentionedScreennames(temp);		
		for (String i : list) {
			temp = StringUtils.delete(temp, "@"+i);
		}
		
		list = extractor.extractCashtags(temp);		
		for (String i : list) {
			temp = StringUtils.delete(temp, i);
		}
	 	
		String s = extractor.extractReplyScreenname(temp);		
		temp = StringUtils.delete(temp, s);
		temp = StringUtils.delete(temp, "RT :");
		temp = StringUtils.delete(temp, "RT");
		temp = StringUtils.delete(temp, "&amp;");
		temp = StringUtils.trimLeadingWhitespace(temp);
		
        Pattern unicodeOutliers = Pattern.compile("[^\\x00-\\x7F]",
                Pattern.UNICODE_CASE | Pattern.CANON_EQ
                        | Pattern.CASE_INSENSITIVE);
        Matcher unicodeOutlierMatcher = unicodeOutliers.matcher(temp);

        temp =  unicodeOutlierMatcher.replaceAll(" ");
  
        if(temp.length() < 5)
        	return 2;
        
		return EnSentimentTextProcessing.calculateSentimentfromClearedString(temp);
	}

    private static int calculateSentimentfromClearedString(String tweet) {
 
        int mainSentiment = 0;
        if (tweet != null && tweet.length() > 0) {
            int longest = 0;
            Annotation annotation = pipeline.process(tweet);
            for (CoreMap sentence : annotation
                    .get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence
                        .get(SentimentCoreAnnotations.AnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sentence.toString();
                if (partText.length() > longest) {
                    mainSentiment = sentiment;
                    longest = partText.length();
                }
 
            }
        }
        return mainSentiment;
    }
}