import time
import commands
import sqlite3
import os

# Basic information for the location of ganglia monitoring data.
# These can also be read from the ganglia configuration files, which is /etc/ganglia/gmetad.conf in SUSE11sp1(may be different for different ops)
BASE_RRD_DIR = "/var/lib/ganglia/rrds/"
CLUSTER_NAME = "ds-new"
SUMMARYINFO = "__SummaryInfo__"

metrics_list = ['tps', 'rd_sec', 'wr_sec', 'util', 'svctm', 'avgrq_sz', 'avgqu_sz', 'await']

class Ganglia_Node:
    node_path = ""
    node_name = ""
    metrics_list = []
    # metrics_path = {}
    # the node_path format is "/var/lib/ganglia/rrds/ds-new/127.0.0.1/"
    # the node_name format is "de80"
    def __init__(self, node_name, node_path, metrics_list = []):
        self.node_name = node_name
        self.node_path = node_path
        self.metrics_list = metrics_list
        # for metric in  self.metrics_list:
            # print self.node_path
            # self.metrics_path[metric] = self.node_path + metric + ".rrd"
            # print self.metrics_path[metric]
        # print self.metrics_path
    def output_parser(self, message):
        result = {}
        message = message.split('\n')
        if len(message) == 1:
            return result
        message = message[-1].split(':')
        result['timestamp'] = message[0]
        result['value'] = float(message[1])
        return result
    def get_lastupdate(self):
        '''
        this method return node status with structure;
        {metric:{'timestamp':XXX,'value':XXX},...}
        '''
        cmd = "rrdtool lastupdate "
        node_last_status = {}
        for metric in self.metrics_list:
            # result = self.output_parser(commands.getstatusoutput(cmd + self.metrics_path[metric])[1])
            result = self.output_parser(commands.getstatusoutput(cmd+self.node_path+metric+'.rrd')[1])
            node_last_status[metric] = result
        return node_last_status

class Ganglia_Cluster:
    cluster_name = ""
    cluster_path = ""
    metrics_list = []
    nodes_list = []
    nodes = {}
    # database related valuables
    # db_name = ""
    db_path = ""
    table_name = ""

    def find_nodes(self):
        node_name_list = []
        items = os.listdir(self.cluster_path)
        for item in items:
            item_path = os.path.join(self.cluster_path, item)
            if os.path.islink(item_path) or item == "__SummaryInfo__":
                continue
            if os.path.isdir(item_path):
                node_name_list.append(item)
        return node_name_list
    def update_cluster(self, metrics_list):
        self.metrics_list = metrics_list
        self.nodes_list = []
        self.nodes = {}
        node_name_list = self.find_nodes()
        for node_name in node_name_list:
            node_path = self.cluster_path+node_name+'/'
            node = Ganglia_Node(node_name, node_path , self.metrics_list)
            # print node.metrics_path
            self.nodes_list.append(node)
            self.nodes[node_name] = node
            # print self.nodes_list[-1].metrics_path
    # the cluster_path format is "/var/lib/ganglia/rrds/ds-new/"
    # the cluster_name format is "ds-new"
    def __init__(self, cluster_name, cluster_path, metrics_list = []):
        self.cluster_name = cluster_name
        self.cluster_path = cluster_path
        self.update_cluster(metrics_list)
    def get_cluster_last_status(self):
        '''
        this method return cluster lastest status with structure:
        {node_name:{metric:{'timestamp':XXX,'value':XXX},...},...}
        '''
        cluster_last_status = {}
        for node in self.nodes_list:
            cluster_last_status[node.node_name] = node.get_lastupdate()
        return cluster_last_status
    def create_cluster_status_db(self, db_path="./status.db", table_name="cluster_status"):
        self.db_path = db_path
        self.table_name = table_name
        db_connection = sqlite3.connect(self.db_path)
        db_cursor = db_connection.cursor()
        db_cursor.execute('''
            CREATE TABLE %s (
                node        TEXT,
                metric      TEXT,
                timestamp   TIMESTAMP,
                value       FLOAT)
            '''% self.table_name)
        db_cursor.close()
    def load_cluster_status_db(self, db_path="./status.db", table_name="cluster_status"):
        self.db_path = db_path
        self.table_name = table_name
    def save_status_to_db(self):
        if not self.db_path and not self.table_name:
            return 0 # save data failed, db file or table not exsit
        save_query = "INSERT INTO %s VALUES (?,?,?,?)"%self.table_name
        cluster_status = self.get_cluster_last_status()
        db_connection = sqlite3.connect(self.db_path)
        db_cursor = db_connection.cursor()
        for node,node_status in cluster_status.items():
            # print node
            for metric,record in node_status.items():
                # print metric
                if not record:
                    # print record,"continue"
                    continue
                db_cursor.execute(save_query,[node,metric,record['timestamp'],record['value']])
        db_connection.commit()
        db_connection.close()
    def view_status_from_db(self):
        # print "in view db function"
        if not self.db_path and not self.table_name:
            # print self.db_path, self.table_name
            # print "leave view db"
            return 0 # view data failed, db file or table not exsit
        view_query = "SELECT * FROM %s"%self.table_name
        # print view_query
        db_connection = sqlite3.connect(self.db_path)
        db_cursor = db_connection.cursor()
        for row in db_cursor.execute(view_query):
            print row
        # print db_cursor.fetchall()
        db_connection.close()


if __name__ == '__main__':
    cluster_path = BASE_RRD_DIR + CLUSTER_NAME + '/'
    cluster = Ganglia_Cluster(CLUSTER_NAME , cluster_path, metrics_list)
    for node, status in cluster.get_cluster_last_status().items():
        print node
        print status
    cluster.create_cluster_status_db()
    # cluster.load_cluster_status_db()
    for i in xrange(10):
        cluster.save_status_to_db()
        print i
        time.sleep(3)
    print "data read from db"
    cluster.view_status_from_db()
