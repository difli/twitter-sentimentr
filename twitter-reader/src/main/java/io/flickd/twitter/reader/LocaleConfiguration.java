package io.flickd.twitter.reader;

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
@Profile("!astra")
public class LocaleConfiguration {

	private PulsarClient client;
	
	@Value("${pulsar.service-url}")
	String serviceUrl;
	
	@Value("${pulsar.topic.from-twitterapi}")
	String topic;

	@Bean
	public Producer<TweetData> producer() throws PulsarClientException {
		client = PulsarClient.builder().serviceUrl(serviceUrl).build();
		return client.newProducer(Schema.AVRO(TweetData.class)).topic(topic).create();
	}
}
