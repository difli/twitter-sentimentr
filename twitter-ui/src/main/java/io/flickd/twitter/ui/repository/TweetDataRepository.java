package io.flickd.twitter.ui.repository;

import java.util.List;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import io.flickd.twitter.model.TweetData;

//	@RepositoryRestResource(collectionResourceRel = "tweet_data", path = "tweet_data")
	public interface TweetDataRepository extends CassandraRepository<TweetData, String> {

//	    @RequestLine("GET /TweetData?count={size}&page={page}&sorting.Sentiment={sort}")
//	    Page<TweetData> findAll(@Named("page") Integer page, @Named("size") Integer size);
////	    Page<TweetData> findAll(Pageable pageable);

	    
////	    @RestResource(path = "tweetContains", rel = "tweetContains")
////		Page<TweetData> findByTweetContainsIgnoreCase(@Param("q") String tweet, Pageable pageable);

////		@RestResource(path = "sentiment", rel = "sentiment")
////		Page<TweetData> findBySentiment(@Param("q") int sentiment, Pageable pageable);
		
////		@RestResource(path = "top", rel = "top")
////        List<TweetData> findFirst20ByOrderByIdDesc();
		
		    List<TweetData> findByLang(String lang);
		    @AllowFiltering
		    List<TweetData> findByLangAndSentiment(String lang, int sentiment);
		    List<TweetData> findTop10ByLang(String lang);

		    List<TweetData> findByLangAndTweetContainsIgnoreCase(String lang, String tweet);
		    
		    @Query(value = "SELECT DISTINCT lang FROM tweet_by_lang")
		    List<TweetData> findLanguages();
	}
	
