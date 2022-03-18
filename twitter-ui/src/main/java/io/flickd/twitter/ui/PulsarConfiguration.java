package io.flickd.twitter.ui;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;

import io.flickd.twitter.model.TweetData;
import io.flickd.twitter.ui.service.MyMessageListener;
import io.flickd.twitter.ui.service.MyTextWebSocketHandler;

@Configuration
@Profile("!astra")
public class PulsarConfiguration {

	@Value("${pulsar.service-url}")
	String serviceUrl;
	
	@Value("${pulsar.topic.to-db}")
	String toDb;

	@Autowired
	MyTextWebSocketHandler websocketHandler;

	private PulsarClient client;

	@Bean
	public Consumer<TweetData> consumer() throws PulsarClientException {
		client = PulsarClient.builder().serviceUrl(serviceUrl).build();
		return client.newConsumer(Schema.AVRO(TweetData.class)).topic(toDb)
				.subscriptionName("en-tweet-subscription")
				.messageListener(new MyMessageListener<TweetData>(websocketHandler)).subscribe();
	}

	@Bean
	public Jackson2JsonObjectMapper jackson2JsonObjectMapper() {
		return new Jackson2JsonObjectMapper();
	}
}
