package ru.kermilov.targetaudience.vkadapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

import ru.kermilov.targetaudience.vkadapter.event.publisher.ProducerChannels;

@SpringBootApplication
@EnableBinding(ProducerChannels.class)
public class VKAdapterApplication {

	public static void main(String[] args) {
		SpringApplication.run(VKAdapterApplication.class, args);
	}

}
