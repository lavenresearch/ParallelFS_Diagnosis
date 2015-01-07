package utils;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;
import java.util.List;

import backtype.storm.tuple.Tuple;

public class PrintableMessage {
	private String msg = "";
	private String topic = "";
	
	public PrintableMessage(String message, String kafkaTopic) {
		msg = message;
		topic = kafkaTopic;
	}
	public PrintableMessage(Tuple tuple, String kafkaTopic) {
		msg = "";
		List<Object> items = tuple.getValues();
		for (Object item:items) {
			msg = msg + (String) item + "\t";
		}
		topic = kafkaTopic;
	}
	public void setmsg(String message){
		msg = message;
	}
	
	public void printmsg(){
		Properties props = new Properties();
		props.put("metadata.broker.list", "192.168.3.140:9092");
		props.put("serializer.class", "kafka.serializer.StringEncoder");
//		props.put("partitioner.class", "example.producer.SimplePartitioner");
		props.put("request.required.acks", "1");
		 
		ProducerConfig config = new ProducerConfig(props);
		Producer<String, String> producer = new Producer<String, String>(config);
        KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic, msg);
        producer.send(data);
        producer.close();
	}
}
