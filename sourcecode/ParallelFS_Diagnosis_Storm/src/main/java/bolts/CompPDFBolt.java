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
import utils.PDF;

public class CompPDFBolt extends BaseBasicBolt{
	private static final int TOTAL_NODE = 4;
	private static final int TIME_DISCRETIZE_FACTOR = 32;
	private static final int PDF_BUFFER_SIZE = 5;
	private float thresHold = 0;
	private float trainStep = 1;
//	Map<Integer,Map<Long,Map<Integer,Float>>> timeDistanceMapMap = new HashMap<Integer,Map<Long,Map<Integer,Float>>>();
	Map<Integer,Map<Long,Integer>> timeAnomalousCountMapMap = new HashMap<Integer,Map<Long,Integer>>();
	Map<Integer,Map<Long,Integer>> timeNormalCountMapMap = new HashMap<Integer,Map<Long,Integer>>();
	Map<Integer,Map<Long,PDF>> timePDFMapMap = new HashMap<Integer,Map<Long,PDF>>();
	List<String> nodeList = new ArrayList<String>();
	
	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector){
		String nodeid = tuple.getStringByField("NodeID");
		String metric = tuple.getStringByField("MetricName");
		Long timeStamp = tuple.getLongByField("TimeStamp")/TIME_DISCRETIZE_FACTOR*TIME_DISCRETIZE_FACTOR;
		String pdfString = tuple.getStringByField("PDF");
		int keyNodeidMetric = (nodeid+metric).hashCode();
		
		String rmessage = "RECEIVED CompPDF Bolt\t"+nodeid+"\t"+metric+"\t"+timeStamp+"\t"+pdfString;
		PrintableMessage rpmsg = new PrintableMessage(rmessage,"cmpb");
		rpmsg.printmsg();
		
		if(nodeList.size() < TOTAL_NODE){
			if( nodeList.contains(nodeid) == false){
				nodeList.add(nodeid);
			}
		}
		PDF pdf = new PDF(pdfString);
		Map<Long,PDF> timePDFMap = new HashMap<Long,PDF>();
		timePDFMap.put(timeStamp, pdf);
		if(timePDFMapMap.size() < PDF_BUFFER_SIZE) {
			timePDFMapMap.put(keyNodeidMetric, timePDFMap);
		}
		else{
			Long min_key = timeStamp;
			for(Long key : timePDFMapMap.get(keyNodeidMetric).keySet()){
				if(key < min_key){
					min_key = key;
				}
			}
			timePDFMapMap.get(keyNodeidMetric).remove(min_key);
			timePDFMapMap.get(keyNodeidMetric).put(timeStamp, pdf);
		}
		for(String othernode:nodeList){
			if(nodeid.equals(othernode) == true){
				continue;
			}
			int keyOthernodeMetric = (othernode+metric).hashCode();
			Map<Long,PDF> othernodeTimePDFMap = timePDFMapMap.get(keyOthernodeMetric);
			if(othernodeTimePDFMap == null){
				continue;
			}
			PDF othernodePDF = othernodeTimePDFMap.get(timeStamp);
			if(othernodePDF == null){
				continue;
			}
			Float distance = pdf.CalDistance(othernodePDF);
			if( distance <= thresHold){
				int count = updateCount(keyNodeidMetric,timeStamp,true);
				int otherCount = updateCount(keyOthernodeMetric,timeStamp,true);
				Integer status = 0;// Normal
				if(count >= TOTAL_NODE/2){
					collector.emit(new Values(nodeid,metric,timeStamp,status));
					String message = "CompPDF Bolt 1:\t"+nodeid+"\t"+metric+"\t"+timeStamp+"\t"+status;
					PrintableMessage pmsg = new PrintableMessage(message,"cmpb");
					pmsg.printmsg();
				}
				if(otherCount >= TOTAL_NODE/2){
					collector.emit(new Values(othernode,metric,timeStamp,status));
					String message = "CompPDF Bolt 2:\t"+nodeid+"\t"+metric+"\t"+timeStamp+"\t"+status;
					PrintableMessage pmsg = new PrintableMessage(message,"cmpb");
					pmsg.printmsg();
				}
			}
			else{
				int count = updateCount(keyNodeidMetric,timeStamp,false);
				int otherCount = updateCount(keyOthernodeMetric,timeStamp,false);
				Integer status = 1;// Anomalous
				if(count >= TOTAL_NODE/2){
					collector.emit(new Values(nodeid,metric,timeStamp,status));
					String message = "CompPDF Bolt 3:\t"+nodeid+"\t"+metric+"\t"+timeStamp+"\t"+status;
					PrintableMessage pmsg = new PrintableMessage(message,"cmpb");
					pmsg.printmsg();
				}
				if(otherCount >= TOTAL_NODE/2){
					collector.emit(new Values(othernode,metric,timeStamp,status));
					String message = "CompPDF Bolt 4:\t"+nodeid+"\t"+metric+"\t"+timeStamp+"\t"+status;
					PrintableMessage pmsg = new PrintableMessage(message,"cmpb");
					pmsg.printmsg();
				}
			}
//			Map<Long,Integer> anomalousCountMap = timeAnomalousCountMapMap.get();
		}
	}
	public int updateCount(int key, Long time, boolean normalFlag){
		Map<Integer,Map<Long,Integer>> timeCountMapMap;
		int count = 0;
		if(normalFlag == true){
			timeCountMapMap = timeNormalCountMapMap;
		}
		else{
			timeCountMapMap = timeAnomalousCountMapMap;
		}
		Map<Long,Integer> timeCountMap = timeCountMapMap.get(key);
		if(timeCountMap == null){
			timeCountMap = new HashMap<Long,Integer>();
			count = 1;
			timeCountMap.put(time, 1);
			timeCountMapMap.put(key, timeCountMap);
		}
		else{
			count = timeCountMap.get(time);
			count++;
			timeCountMap.put(time, count);
			timeCountMapMap.put(key, timeCountMap);
		}
		return count;
	}
	
	public void training(){
		thresHold += trainStep;
		return;
	}
	
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer){
		declarer.declare(new Fields("NodeID","MetricName","TimeStamp","Status"));
	}
}
