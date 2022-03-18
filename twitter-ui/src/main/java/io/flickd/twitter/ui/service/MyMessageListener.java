package io.flickd.twitter.ui.service;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageListener;

import io.flickd.twitter.model.TweetData;

public class MyMessageListener<T> implements MessageListener<T> {

	MyTextWebSocketHandler websocketHandler;

	public MyMessageListener(MyTextWebSocketHandler websocketHandler) {
		this.websocketHandler = websocketHandler;
	}

	@Override
	public void received(Consumer<T> consumer, Message<T> msg) {
		try {
//			System.out.printf("Message received: %s", msg.getValue());
			if(((TweetData) msg.getValue()).getLang().compareToIgnoreCase("en") == 0)
				websocketHandler.send((TweetData) msg.getValue());
			consumer.acknowledge(msg);
		} catch (Exception e) {
			consumer.negativeAcknowledge(msg);
		}
	}

}
