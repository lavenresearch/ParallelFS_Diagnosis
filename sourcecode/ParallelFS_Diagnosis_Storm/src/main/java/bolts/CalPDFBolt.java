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
import utils.PDF;

@SuppressWarnings("serial")
public class CalPDFBolt extends BaseBasicBolt{
	private static final int WINSIZE = 64;
	private static final int WINSHIFT = 32;
	private Map<Integer,Queue<Float>> smoothedValuesQueueMap = new HashMap<Integer,Queue<Float>>();
	private Map<Integer,Long> latestTimestampMap = new HashMap<Integer,Long>();
	private Map<Integer,Integer> shiftCountsMap = new HashMap<Integer,Integer>();
	public void execute(Tuple tuple, BasicOutputCollector collector){
		String nodeid = tuple.getStringByField("NodeID");
		String metric = tuple.getStringByField("MetricName");
		Long timeStamp = tuple.getLongByField("TimeStamp");
		Float value = tuple.getFloatByField("Value");
		
		String message = "RECEIVED CalPDF Bolt\t"+nodeid+"\t"+metric+"\t"+timeStamp+"\t"+value;
		PrintableMessage pmsg = new PrintableMessage(message,"calpb");
		pmsg.printmsg();
		
		int keyNodeidMetric = (nodeid+metric).hashCode();
		
		Queue<Float> smoothedValuesQueue = smoothedValuesQueueMap.get(keyNodeidMetric);
		if( smoothedValuesQueue == null){
			smoothedValuesQueue = new LinkedList<Float>();
			smoothedValuesQueue.offer(value);
			smoothedValuesQueueMap.put(keyNodeidMetric, smoothedValuesQueue);//是否需要put回MAP？
			latestTimestampMap.put(keyNodeidMetric, timeStamp);
			shiftCountsMap.put(keyNodeidMetric, -1);
			return;
		}
		if( smoothedValuesQueue.size() == WINSIZE){
			smoothedValuesQueue.offer(value);
			smoothedValuesQueue.poll();
			smoothedValuesQueueMap.put(keyNodeidMetric, smoothedValuesQueue);//是否需要put回MAP？
			latestTimestampMap.put(keyNodeidMetric, timeStamp);
			int shiftCount = shiftCountsMap.get(keyNodeidMetric);
			shiftCount++;
			shiftCountsMap.put(keyNodeidMetric, shiftCount);
			if( shiftCount < WINSHIFT && shiftCount >= 0){
				return;
			}
		}
		if( smoothedValuesQueue.size() < WINSIZE){
			smoothedValuesQueue.offer(value);
			smoothedValuesQueueMap.put(keyNodeidMetric, smoothedValuesQueue);//是否需要put回MAP？
			latestTimestampMap.put(keyNodeidMetric, timeStamp);
			if(smoothedValuesQueue.size() < WINSIZE){
				shiftCountsMap.put(keyNodeidMetric, -1);
				return;
			}
		}
		shiftCountsMap.put(keyNodeidMetric, 0);
		PDF pdf = new PDF(smoothedValuesQueue);
		String pdf_string = pdf.ToString();
		collector.emit(new Values(nodeid,metric,timeStamp,pdf_string));
		
		message = "CalPDF Bolt\t"+nodeid+"\t"+metric+"\t"+timeStamp+"\t"+pdf_string;
		pmsg = new PrintableMessage(message,"calpb");
		pmsg.printmsg();
	}
	
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer){
		declarer.declare(new Fields("NodeID","MetricName","TimeStamp","PDF"));
	}
}
