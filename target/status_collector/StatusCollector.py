from kafka.client import KafkaClient
from kafka.consumer import SimpleConsumer
from kafka.producer import SimpleProducer, KeyedProducer
import time
import commands
import socket
import re

class ParallelFSNode:
    NodeID = ""
    last_update_timestamp = None

    def get_NodeID(self):
        '''
        return node ip as node id.
        '''
        names,aliases,ips = socket.gethostbyname_ex(socket.gethostname())
        for ip in ips:
            if re.match("^192", ip):
                nodeid = ip
        return nodeid

    def get_system_time(self):
        '''
        return local(not UTC) time expressed in seconds since the epoch.
        '''
        return time.mktime(time.localtime())

    def __init__(self):
        self.NodeID = self.get_NodeID()
        self.last_update_timestamp = self.get_system_time()

    def get_storage_status(self, status_timestamp):
        '''
        return a dict of "status values" indexed by "metric names"
        '''
        status = {}
        starting_time_point = time.localtime( status_timestamp - 5)
        starting_time_point = ('%02d' % starting_time_point.tm_hour) + ":" + ('%02d' % starting_time_point.tm_min) + ":" + ('%02d' % starting_time_point.tm_sec)
        cmd = "sadf -d -s " + starting_time_point + " 1 1 -- -d"
        # print cmd
        cmd_output = commands.getstatusoutput(cmd)[1].split("\n")
        # print cmd_output
        title = cmd_output[0].split(";")
        for i in xrange(4,len(title)):
            status[ title[i] ] = []
        for i in xrange(1 , len(cmd_output)):
            values = cmd_output[i].split(";")
            # status[title[3]].append(values[3])
            for j in xrange(4, len(values)):
                status[title[j]].append(float(values[j]))
        return status

    def get_network_status(self, status_timestamp):
        status = {}
        return status

    def get_standardized_status(self):
        '''
        return list of standardized_status which is a string in the format as
        "NodeID metric_name timestamp value"
        '''
        standardized_status_list = []
        standardized_status = ""
        status_timestamp = self.get_system_time()
        storage_status = self.get_storage_status(status_timestamp)
        network_status = self.get_network_status(status_timestamp)
        status_timestamp = str(long(status_timestamp))
        for metric,value in storage_status.items():
            # print metric,value
            standardized_status = self.NodeID + " " + metric + " " + status_timestamp + " " + str(sum(value))
            # print standardized_status
            standardized_status_list.append(standardized_status)
            # print standardized_status_list
        for metric,value in network_status.items():
            standardized_status = self.NodeID + " " + metric + " " + status_timestamp + " " + str(value[-1])
            standardized_status_list.append(standardized_status)
        # print standardized_status_list
        return standardized_status_list


class KafkaCluster:
    """define a kafka cluster"""
    kafka_ip = ""
    kafka_port = ""
    topic_name = ""
    def __init__(self,kafka_cluster_ip,kafka_cluster_port,topic_name):
        self.kafka_ip = kafka_cluster_ip
        self.kafka_port = kafka_cluster_port
        self.topic_name = topic_name


class StatusSender:
    """
    send parallelFS status to kafka cluster for storm cluster to consume
    """
    node = None
    kafka_cluster = None
    def __init__(self, node, kafka_cluster):
        self.node = node
        self.kafka_cluster = kafka_cluster

    def send_to_kafka(self, interval = 1, batch_number = 15, batch_time = 15):
        kafka = KafkaClient(self.kafka_cluster.kafka_ip+":"+self.kafka_cluster.kafka_port)
        producer = SimpleProducer(kafka, batch_send=True,
                                  batch_send_every_n=batch_number,
                                  batch_send_every_t=batch_time)
        while True:
            status_list = self.node.get_standardized_status()
            # need to implement this function in unblock way
            # flag = input("\nInput \'q\' to quit loop...\n")
            # if flag == 'q':
            #     break
            # print status_list
            for status in status_list:
                # print status
                producer.send_messages(self.kafka_cluster.topic_name, status)
                # time.sleep(interval)


if __name__ == '__main__':
    kafka_cluster = KafkaCluster("192.168.3.140",
                                "9092",
                                "ksptest")
    pfs_node = ParallelFSNode()
    sender = StatusSender(pfs_node, kafka_cluster)
    sender.send_to_kafka()
    # print pfs_node.NodeID
    # t = pfs_node.get_system_time()
    # print t
    # print pfs_node.get_storage_status(t)
