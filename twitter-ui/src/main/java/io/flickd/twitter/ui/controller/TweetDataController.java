package io.flickd.twitter.ui.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.flickd.twitter.model.TweetData;
import io.flickd.twitter.ui.repository.TweetDataRepository;

@RestController
@RequestMapping(value = "/tweet_data")
public class TweetDataController {
    private static final Logger logger = LoggerFactory.getLogger(TweetDataController.class);
    
    private TweetDataRepository repository;

    @Autowired
    public TweetDataController(TweetDataRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<TweetData> TweetDatas() {
        return repository.findAll();
    }

    @RequestMapping(method = RequestMethod.PUT)
    public TweetData add(@RequestBody TweetData TweetData) {
        logger.info("Adding TweetData " + TweetData.getId());
        return repository.save(TweetData);
    }

    @RequestMapping(method = RequestMethod.POST)
    public TweetData update(@RequestBody TweetData TweetData) {
        logger.info("Updating TweetData " + TweetData.getId());
        return repository.save(TweetData);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TweetData getById(@PathVariable String id) {
        logger.info("Getting TweetData " + id);
        return repository.findById(id).orElse(null);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteById(@PathVariable String id) {
        logger.info("Deleting TweetData " + id);
        repository.deleteById(id);
    }
    @RequestMapping(value = "/language", method = RequestMethod.GET)
    public List<TweetData> findByLang(@RequestParam("lang") String lang) {
    	logger.info("Get TweetData by lang " + lang);
        return repository.findByLang(lang);
    }
    @RequestMapping(value = "/languageandsentiment", method = RequestMethod.GET)
    public List<TweetData> findByLangAndSentiment(@RequestParam("lang") String lang, @RequestParam("sentiment") int sentiment) {
    	logger.info("Get TweetData by lang " + lang + " and Sentiment "+ sentiment);
        return repository.findByLangAndSentiment(lang, sentiment);
    }
    @RequestMapping(value = "/language/{lang}/top", method = RequestMethod.GET)
    public List<TweetData> findFirst10ByLang(@PathVariable("lang") String lang) {
    	logger.info("Get TweetData by lang " + lang);
        return repository.findTop10ByLang(lang);
    }
    @RequestMapping(value = "/search/tweetContains", method = RequestMethod.GET)
    public List<TweetData> findByTweetContainsIgnoreCase(@RequestParam("lang") String lang, @RequestParam("q") String query) {
    	logger.info("Get TweetData by query " + query);
        return repository.findByLangAndTweetContainsIgnoreCase(lang, query);
    }
    @RequestMapping(value = "/search/languages", method = RequestMethod.GET)
    public List<TweetData> findLanguages() {
    	logger.info("Get Languages ");
        return repository.findLanguages();
    }
}
