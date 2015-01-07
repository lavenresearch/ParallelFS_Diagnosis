package storm;

import storm.kafka.KafkaSpout;
import storm.kafka.BrokerHosts;
import storm.kafka.Broker;
import storm.kafka.StaticHosts;
import storm.kafka.ZkHosts;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.trident.GlobalPartitionInformation;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.task.TopologyContext;
import bolts.BottleneckLocatingBolt;
import bolts.CalAverageBolt;
import bolts.CalPDFBolt;
import bolts.CmpAverageBolt;
import bolts.CompPDFBolt;
import bolts.SmoothingBolt;
import bolts.ToTuplesBolt;

public class PfsdTopology {

	public static void main(String[] args) throws Exception {
		GlobalPartitionInformation info = new GlobalPartitionInformation();
		info.addPartition(0, new Broker("192.168.3.140", 9092));
		BrokerHosts brokerHosts = new StaticHosts(info);
//		BrokerHosts brokerHosts = new ZkHosts("192.168.3.140:2181");
		String topic = "ksptest";
		String zkRoot = "";
		String spoutId = "kspout";
		
		SpoutConfig spoutConfig = new SpoutConfig(brokerHosts, topic, zkRoot, spoutId);
		spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme()); 
		
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("kspout", new KafkaSpout(spoutConfig), 1);
		builder.setBolt("totuples",new ToTuplesBolt(),1).shuffleGrouping("kspout");
		builder.setBolt("smoothing", new SmoothingBolt(), 1).shuffleGrouping("totuples");
//		builder.setBolt("calpdf", new CalPDFBolt(), 1).shuffleGrouping("smoothing");
//		builder.setBolt("comppdf", new CompPDFBolt(), 1).shuffleGrouping("calpdf");
//		builder.setBolt("bottlenecklocating", new BottleneckLocatingBolt(), 1).shuffleGrouping("comppdf");
		builder.setBolt("calavg", new CalAverageBolt(), 1).shuffleGrouping("smoothing");
		builder.setBolt("cmpavg", new CmpAverageBolt(), 1).shuffleGrouping("calavg");
		builder.setBolt("bottlenecklocating",new BottleneckLocatingBolt(),1).shuffleGrouping("cmpavg");
		
		Config conf = new Config();
		conf.setDebug(true);
		conf.setNumWorkers(6);
		StormSubmitter.submitTopology("KafkaTest", conf, builder.createTopology());

	}

}
