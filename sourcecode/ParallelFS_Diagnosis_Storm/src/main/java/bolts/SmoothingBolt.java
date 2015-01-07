package bolts;

import backtype.storm.task.ShellBolt;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import backtype.storm.topology.base.BaseRichSpout;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;

import utils.PrintableMessage;

@SuppressWarnings("serial")
public class SmoothingBolt extends BaseBasicBolt {
	
	private static final int SMOOTHING_LEVEL = 5;
	private Map<Integer,Queue<Float>> originalValuesQueueMap = new HashMap<Integer,Queue<Float>>();
	private Map<Integer,Long> latestTimestampMap = new HashMap<Integer,Long>();
	
	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector){
		String nodeid = tuple.getStringByField("NodeID");
		String metric = tuple.getStringByField("MetricName");
		Long timeStamp = tuple.getLongByField("TimeStamp");
		Float value = tuple.getFloatByField("Value");
		int keyNodeidMetric = (nodeid+metric).hashCode();
		
		Queue<Float> originalValuesQueue = originalValuesQueueMap.get(keyNodeidMetric);
		if( originalValuesQueue == null ) {
			originalValuesQueue = new LinkedList<Float>();
			originalValuesQueue.offer(value);
			originalValuesQueueMap.put(keyNodeidMetric, originalValuesQueue);//是否需要put回MAP？
			latestTimestampMap.put(keyNodeidMetric, timeStamp);
			return;
		}
		if( originalValuesQueue.size() == SMOOTHING_LEVEL) {
			originalValuesQueue.offer(value);
			originalValuesQueue.poll();
			originalValuesQueueMap.put(keyNodeidMetric, originalValuesQueue);//是否需要put回MAP？
			latestTimestampMap.put(keyNodeidMetric, timeStamp);
		}
		if( originalValuesQueue.size() < SMOOTHING_LEVEL) {
			originalValuesQueue.offer(value);
			originalValuesQueueMap.put(keyNodeidMetric, originalValuesQueue);//是否需要put回MAP？
			latestTimestampMap.put(keyNodeidMetric, timeStamp);
			if( originalValuesQueue.size() < SMOOTHING_LEVEL){
				return;
			}
		}
		Float sum = (float) 0;
//		Float[] values = (Float[]) originalValuesQueue.toArray();
		for(Float val:originalValuesQueue){
			sum += val;		
		}
		Float avg = sum/SMOOTHING_LEVEL;
		
		collector.emit(new Values(nodeid,metric,timeStamp,avg));
		
		String message = "Smoothing Bolt\t"+nodeid+"\t"+metric+"\t"+timeStamp+"\t"+avg;
		PrintableMessage pmsg = new PrintableMessage(message,"sb");
		pmsg.printmsg();
	}
	
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer){
		declarer.declare(new Fields("NodeID","MetricName","TimeStamp","Value"));
	}
}
