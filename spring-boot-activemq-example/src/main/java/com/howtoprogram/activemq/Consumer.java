package com.howtoprogram.activemq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

	@JmsListener(destination = "${hello.activemq.queue}")
	public void receiveQueue(String text) {
		System.out.println(text);
	}

}
