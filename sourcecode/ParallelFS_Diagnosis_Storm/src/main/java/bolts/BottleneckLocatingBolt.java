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
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

import utils.PrintableMessage;

public class BottleneckLocatingBolt extends BaseBasicBolt{
	private static final int K = 3;
	private int statusQueueSize = 2*K-1;
	Map<Integer,Queue<Integer>> statusQueueMap = new HashMap<Integer,Queue<Integer>>();
	public void execute(Tuple tuple, BasicOutputCollector collector){
		String nodeid = tuple.getStringByField("NodeID");
		String metric = tuple.getStringByField("MetricName");
		Long timeStamp = tuple.getLongByField("TimeStamp");
		Integer status = tuple.getIntegerByField("Status");
		int keyNodeidMetric = (nodeid+metric).hashCode();
		
//		String rmessage = "RECEIVED BottleneckLocating Bolt\t"+nodeid+"\t"+metric+"\t"+timeStamp+"\t"+status;
//		PrintableMessage rpmsg = new PrintableMessage(rmessage,"blb");
//		rpmsg.printmsg();
		
		Queue<Integer> statusQueue = statusQueueMap.get(keyNodeidMetric);
		if(statusQueue == null){
			statusQueue = new LinkedList<Integer>();
			statusQueue.offer(status);
			statusQueueMap.put(keyNodeidMetric, statusQueue);
			if(K == 1){
				collector.emit(new Values(nodeid,metric,timeStamp,"ERROR"));
			}
			return;
		}
		statusQueue.offer(status);
		if(statusQueue.size() > statusQueueSize){
			statusQueue.poll();
		}
		statusQueueMap.put(keyNodeidMetric, statusQueue);
		Integer anomalousCount = 0;
		for(Integer s:statusQueue){
			anomalousCount += s;
		}
		if(anomalousCount >= K){
			collector.emit(new Values(nodeid,metric,timeStamp,"ERROR"));
			
			String message = "BottleneckLocating Bolt:"+nodeid+"\t"+metric+"\t"+timeStamp+"\t"+"ERROR";
			PrintableMessage pmsg = new PrintableMessage(message,"blb");
			pmsg.printmsg();
		}
//		String message = "BottleneckLocating Bolt:"+nodeid+"\t"+metric+"\t"+timeStamp+"\t"+"ok!";
//		PrintableMessage pmsg = new PrintableMessage(message,"blb");
//		pmsg.printmsg();
	}
	
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer){
		declarer.declare(new Fields("NodeID","MetricName","TimeStamp","Error"));
	}
}
