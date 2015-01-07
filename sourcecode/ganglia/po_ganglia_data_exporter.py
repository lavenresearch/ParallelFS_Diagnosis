import commands
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
    metrics_path = {}
    # the node_path format is "/var/lib/ganglia/rrds/ds-new/127.0.0.1/"
    # the node_name format is "de80"
    def __init__(self, node_name, node_path, metrics_list = []):
        self.node_name = node_name
        self.node_path = node_path
        self.metrics_list = metrics_list
        for metric in  self.metrics_list:
            # print self.node_path
            self.metrics_path[metric] = self.node_path + metric + ".rrd"
            # print self.metrics_path[metric]
        # print self.metrics_path
    def output_parser(self, message):
        result = {}
        message = message.split('\n')
        if len(message) == 1:
            return result
        message = message[-1].split(':')
        result['timestamp'] = message[0]
        result['value'] = message[1]
        return result
    def get_lastupdate(self):
        cmd = "rrdtool lastupdate "
        node_last_state = {}
        for metric in self.metrics_list:
            result = self.output_parser(commands.getstatusoutput(cmd + self.metrics_path[metric])[1])
            node_last_state[metric] = result
        return node_last_state

def find_nodes(cluster_name, cluster_path):
    node_name_list = []
    items = os.listdir(cluster_path)
    for item in items:
        item_path = os.path.join(cluster_path, item)
        if os.path.islink(item_path) or item == "__SummaryInfo__":
            continue
        if os.path.isdir(item_path):
            node_name_list.append(item)
    return node_name_list

def get_node_state(node_name, node_path, metrics_list):
    node = Ganglia_Node(node_name, node_path, metrics_list)
    return node.get_lastupdate()

def save_node_state(node_name, node_path, metrics_list):
    node_state = get_node_state(node_name, node_path, metrics_list)
    pass

def print_node_state(node_name, node_path, metrics_list):
    node_state = get_node_state(node_name, node_path, metrics_list)
    print node_state

if __name__ == '__main__':
    cluster_path = BASE_RRD_DIR+CLUSTER_NAME+'/'
    node_name_list = find_nodes(CLUSTER_NAME, cluster_path)
    for node_name in node_name_list:
        print_node_state(node_name, cluster_path+node_name+'/', metrics_list)
