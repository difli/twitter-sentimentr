package io.flickd.twitter.reader;

import org.apache.pulsar.client.api.AuthenticationFactory;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.flickd.twitter.model.TweetData;

@Configuration
@Profile("astra")
public class AstraConfiguration {
	
	private PulsarClient client;

	@Value("${astra-streaming.service-url}")
	String serviceUrl;
	
	@Value("${astra-streaming.token}")
	String token;
	
	@Value("${astra-streaming.topic.from-twitterapi}")
	String fromTwitterapi;

	@Bean
	public Producer<TweetData> consumer() throws PulsarClientException {
		client = PulsarClient.builder().serviceUrl(serviceUrl)
				.authentication(AuthenticationFactory.token(token)).build();
		return client.newProducer(Schema.AVRO(TweetData.class)).topic(fromTwitterapi).create();
	}
}
