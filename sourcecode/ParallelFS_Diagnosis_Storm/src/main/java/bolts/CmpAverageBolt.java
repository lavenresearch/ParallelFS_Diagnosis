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
import java.lang.Math;

import utils.PrintableMessage;

public class CmpAverageBolt extends BaseBasicBolt{
	private static final int TOTAL_NODE = 4;
//	private float thresHold = 0;
//	private float trainStep = 1;
	private boolean trainFlag = false;
	private Map<String,Float> metricThresHoldMap = new HashMap<String,Float>();
//	{{
//		metricThresHoldMap.put("tps", (float) 0);
//		metricThresHoldMap.put("rd_sec/s", (float) 0);
//		metricThresHoldMap.put("wr_sec/s", (float) 0);
//		metricThresHoldMap.put("avgrq-sz", (float) 0);
//		metricThresHoldMap.put("avgqu-sz", (float) 0);
//		metricThresHoldMap.put("await", (float) 0);
//		metricThresHoldMap.put("svctm", (float) 0);
//		metricThresHoldMap.put("%util", (float) 0);
//	}}
	private Map<Integer,Float> avgMap = new HashMap<Integer,Float>();
	List<String> nodeList = new ArrayList<String>();
	
	public CmpAverageBolt(){
		metricThresHoldMap.put("tps", (float) 135.925);
		metricThresHoldMap.put("rd_sec/s", (float) 2284.5005);
		metricThresHoldMap.put("wr_sec/s", (float) 24808.8);
		metricThresHoldMap.put("avgrq-sz", (float) 82.4144);
		metricThresHoldMap.put("avgqu-sz", (float) 23.229187);
		metricThresHoldMap.put("await", (float) 45.302753);
		metricThresHoldMap.put("svctm", (float) 2.6582828);
		metricThresHoldMap.put("%util", (float) 62.78578);
	}
	
	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector){
		PrintableMessage pmsg = new PrintableMessage("start","cmpavgb");
		String sourceComponent = tuple.getSourceComponent();
		PrintableMessage testmsg = new PrintableMessage("from:" + sourceComponent ,"suyi");
		testmsg.printmsg();
		
		String nodeid = tuple.getStringByField("NodeID");
		String metric = tuple.getStringByField("MetricName");
		Long timeStamp = tuple.getLongByField("TimeStamp");
		Float average = tuple.getFloatByField("Average");
		int keyNodeidMetric = (nodeid+metric).hashCode();
		
//		String rmessage = "RECEIVED CompAverage Bolt\t"+nodeid+"\t"+metric+"\t"+timeStamp+"\t"+average;
//		PrintableMessage rpmsg = new PrintableMessage(rmessage,"cmpavgb");
//		rpmsg.printmsg();
		
		if(nodeList.size() < TOTAL_NODE){
			if( nodeList.contains(nodeid) == false){
				nodeList.add(nodeid);
			}
		}
		if(nodeList.size() < TOTAL_NODE){
			return;
		}
		avgMap.put(keyNodeidMetric, average);
		int count = 0;
		for(String n:nodeList){
			Integer k = (n+metric).hashCode();
			Float avg = avgMap.get(k);
			if(avg == null){
				return;
			}
			Float distance = Math.abs(average-avg);
			Float threshold = metricThresHoldMap.get(metric);
			if(threshold == null){
				metricThresHoldMap.put(metric,(float) 0);
			}
			if( distance >= metricThresHoldMap.get(metric)){
				count++;
				if(trainFlag){
					metricThresHoldMap.put(metric, distance);
					PrintableMessage trainmsg = new PrintableMessage("Metric:"+metric+"\t"+distance,"trainchannle");
					trainmsg.printmsg();
				}
			}
		}
		Integer status = 0;
		if(count>=(TOTAL_NODE/2)){
			status = 1;
		}
		else{
			status = 0;
		}
		collector.emit(new Values(nodeid,metric,timeStamp,status));
		
		String message = "CompAverage Bolt\t"+nodeid+"\t"+metric+"\t"+timeStamp+"\t"+status;
		pmsg.setmsg(message);
		pmsg.printmsg();
	}
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer){
		declarer.declare(new Fields("NodeID","MetricName","TimeStamp","Status"));
	}
}
