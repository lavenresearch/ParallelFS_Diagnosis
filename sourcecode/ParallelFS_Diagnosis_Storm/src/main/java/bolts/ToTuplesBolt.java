package bolts;

import backtype.storm.task.ShellBolt;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import utils.PrintableMessage;

@SuppressWarnings("serial")
public class ToTuplesBolt extends BaseBasicBolt {
	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector){
		String message = tuple.getString(0);
		String[] words = message.split(" ");
//		for( int i=0; i<words.length; i++){
//			collector.emit(new Values(words[i]));
//		}
//		System.out.println(message);
//		printmsg(message);
		PrintableMessage pmsg = new PrintableMessage(message, "ttb");
		pmsg.printmsg();
		if( words.length >= 4){
			Long timeStamp = Long.valueOf(words[2]);
			Float value = Float.valueOf(words[3]);
			collector.emit(new Values(words[0],words[1],timeStamp,value));
		}
		else{
			collector.emit(new Values(0,0,0,0));
		}
	}
	
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer){
		declarer.declare(new Fields("NodeID","MetricName","TimeStamp","Value"));
	}
}
