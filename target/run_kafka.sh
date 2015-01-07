zkServer.sh start /opt/zookeeper-3.4.6/conf/zoo_sample.cfg
nohup kafka-server-start.sh /opt/kafka_2.10-0.8.1.1/config/server.properties


kafka-console-consumer.sh --zookeeper 192.168.3.140:2181 --topic
