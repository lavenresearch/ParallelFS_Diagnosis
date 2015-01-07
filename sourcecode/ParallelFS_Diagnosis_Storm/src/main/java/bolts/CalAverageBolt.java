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

import utils.PDF;
import utils.PrintableMessage;

public class CalAverageBolt extends BaseBasicBolt{
	private static final int WINSIZE = 64;
	private static final int WINSHIFT = 32;
	private Map<Integer,Queue<Float>> ValuesQueueMap = new HashMap<Integer,Queue<Float>>();
	private Map<Integer,Long> latestTimestampMap = new HashMap<Integer,Long>();
	private Map<Integer,Integer> shiftCountsMap = new HashMap<Integer,Integer>();
	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector){
		String nodeid = tuple.getStringByField("NodeID");
		String metric = tuple.getStringByField("MetricName");
		Long timeStamp = tuple.getLongByField("TimeStamp");
		Float value = tuple.getFloatByField("Value");
		
//		String message = "RECEIVED CalAverage Bolt\t"+nodeid+"\t"+metric+"\t"+timeStamp+"\t"+value;
//		PrintableMessage pmsg = new PrintableMessage(message,"calavgb");
//		pmsg.printmsg();
		
		int keyNodeidMetric = (nodeid+metric).hashCode();
		
		Queue<Float> ValuesQueue = ValuesQueueMap.get(keyNodeidMetric);
		if(ValuesQueue == null){
			ValuesQueue = new LinkedList<Float>();
			ValuesQueue.offer(value);
			ValuesQueueMap.put(keyNodeidMetric, ValuesQueue);
			latestTimestampMap.put(keyNodeidMetric, timeStamp);
			shiftCountsMap.put(keyNodeidMetric, -1);
		}
		if( ValuesQueue.size() == WINSIZE){
			ValuesQueue.offer(value);
			ValuesQueue.poll();
			latestTimestampMap.put(keyNodeidMetric, timeStamp);
			int shiftCount = shiftCountsMap.get(keyNodeidMetric);
			shiftCount++;
			shiftCountsMap.put(keyNodeidMetric, shiftCount);
			if( shiftCount < WINSHIFT && shiftCount >= 0){
				return;
			}
		}
		if( ValuesQueue.size() < WINSIZE){
			ValuesQueue.offer(value);
			latestTimestampMap.put(keyNodeidMetric, timeStamp);
			if(ValuesQueue.size() < WINSIZE){
				shiftCountsMap.put(keyNodeidMetric, -1);
				return;
			}
		}
		shiftCountsMap.put(keyNodeidMetric, 0);
		Float avg = (float) 0;
		Float sum = (float) 0;
		for(Float val:ValuesQueue){
			sum += val;
		}
		avg = sum/ValuesQueue.size();
		collector.emit(new Values(nodeid,metric,timeStamp,avg));
		
		String message = "CalAverage Bolt\t"+nodeid+"\t"+metric+"\t"+timeStamp+"\t"+avg;
		PrintableMessage pmsg = new PrintableMessage(message,"calavgb");
		pmsg.printmsg();
	}
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer){
		declarer.declare(new Fields("NodeID","MetricName","TimeStamp","Average"));
	}
}
