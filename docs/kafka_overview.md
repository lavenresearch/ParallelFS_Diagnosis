# kafka overview
## components
1. producer
2. consumer
3. topic: a category or feed name to which messages are published
4. broker: a node in kafka cluster

for each topic, the kafka cluster maintains a _partitioned_ log. message in one partition is uniquely identified by a sequential id called the _offset_.

consumer use its own _offset_, which is controlled by itself and is the only metadata retained on per-consumer basis, in the log to get messages.

history message can be reprocessed if consumer use the corresponding _offset_.

order guarantee within partition.

passive replicate.


## install and usage
1. start zookeeper service
2. download kafka
    * tar -xzf kafka_2.9.2-0.8.1.tgz
    * cd kafka_2.9.2-0.8.1
3. start kafka
    * bin/kafka-server-start.sh config/server.properties
4. create and list topic
    * bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test
    * bin/kafka-topics.sh --list --zookeeper localhost:2181
5. start producer
    * bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test
6. start consumer
    * bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic test --from-beginning
7. setting up a multi-broker cluster
    * see document

## kafka API
1. use java program as an producer(how to send messages to kafka cluster in java) [Program Example](https://cwiki.apache.org/confluence/display/KAFKA/0.8.0+Producer+Example)

## integrate kafka with storm
1. use Storm-Kafka-0.8-plus, which provide "KafkaSpout" as Spout, [an simple program example](http://blog.csdn.net/xeseo/article/details/18615761).

## kafka maven dependency
1. http://stackoverflow.com/questions/17037209/where-can-i-find-maven-repository-for-kafka
