# import sys, os
import commands
descriptors = list()

cmd = "sar -d 1 1" # the command getting storage status

def get_storage_status(cmd):
    status = {}
    cmd_output = commands.getstatusoutput(cmd)[1]
    status_temp = cmd_output[cmd_output.find("Average:"):]
    status_temp = status_temp.split("\n")
    title = ' '.join(status_temp[0].split())
    title = title.split(' ')
    for i in xrange(1, len(title)):
        status[ title[i] ] = []
    for i in xrange(1, len(status_temp)):
        values = ' '.join(status_temp[i].split())
        values = values.split(' ')
        status[title[1]].append(values[1])
        for j in xrange(2, len(values)):
            status[title[j]].append(float(values[j]))
    return status

def tps_callback_function(name):
    '''Return a metric value.'''
    status = get_storage_status(cmd)
    return sum(status['tps'])
def rd_sec_callback_function(name):
    '''Return a metric value.'''
    status = get_storage_status(cmd)
    return sum(status['rd_sec/s'])
def wr_sec_callback_function(name):
    '''Return a metric value.'''
    status = get_storage_status(cmd)
    return sum(status['wr_sec/s'])
def avgrq_sz_callback_function(name):
    '''Return a metric value.'''
    status = get_storage_status(cmd)
    return sum(status['avgrq-sz'])

def avgqu_sz_callback_function(name):
    '''Return a metric value.'''
    status = get_storage_status(cmd)
    return sum(status['avgqu-sz'])
def await_callback_function(name):
    '''Return a metric value.'''
    status = get_storage_status(cmd)
    return sum(status['await'])
def svctm_callback_function(name):
    '''Return a metric value.'''
    status = get_storage_status(cmd)
    return sum(status['svctm'])
def util_callback_function(name):
    '''Return a metric value.'''
    status = get_storage_status(cmd)
    return sum(status['%util'])


def metric_init(params):
    '''Initialize all necessary initialization here.'''
    global descriptors

    # if 'Param1' in params:
    #     p1 = params['Param1']

    tps_d = {'name': 'tps', #This is the name of this metric!
        'call_back': tps_callback_function, #Call back function queried by gmond
        'time_max': 90, #Maximum metric value gathering interval
        'value_type': 'uint', #Data type (string, uint, float, double)
        'units': 'requests/second', #Units label
        'slope': 'both', #Slope ('zero' constant values, 'both' numeric values)
        'format': '%f', #String formatting ('%s', '%u','%f')
        'description': 'storage related metric', #Free form metric description
        'groups': 'throughput'}
    rd_sec_d = {'name': 'rd_sec',
        'call_back': rd_sec_callback_function,
        'time_max': 90,
        'value_type': 'uint',
        'units': 'sectors/second',
        'slope': 'both',
        'format': '%f',
        'description': 'storage related metric',
        'groups': 'throughput'}
    wr_sec_d = {'name': 'wr_sec',
        'call_back': wr_sec_callback_function,
        'time_max': 90,
        'value_type': 'uint',
        'units': 'sectors/second',
        'slope': 'both',
        'format': '%f',
        'description': 'storage related metric',
        'groups': 'throughput'}
    avgrq_sz_d = {'name': 'avgrq_sz',
        'call_back': avgrq_sz_callback_function,
        'time_max': 90,
        'value_type': 'uint',
        'units': 'sectors/request',
        'slope': 'both',
        'format': '%f',
        'description': 'storage related metric',
        'groups': 'throughput'}

    avgqu_sz_d = {'name': 'avgqu_sz',
        'call_back': avgqu_sz_callback_function,
        'time_max': 90,
        'value_type': 'uint',
        'units': 'requests',
        'slope': 'both',
        'format': '%f',
        'description': 'storage related metric',
        'groups': 'latency'}
    await_d = {'name': 'await',
        'call_back': await_callback_function,
        'time_max': 90,
        'value_type': 'uint',
        'units': 'milliseconds',
        'slope': 'both',
        'format': '%f',
        'description': 'storage related metric',
        'groups': 'latency'}
    svctm_d = {'name': 'svctm',
        'call_back': svctm_callback_function,
        'time_max': 90,
        'value_type': 'uint',
        'units': 'milliseconds',
        'slope': 'both',
        'format': '%f',
        'description': 'storage related metric',
        'groups': 'latency'}
    util_d = {'name': 'util',
        'call_back': util_callback_function,
        'time_max': 90,
        'value_type': 'uint',
        'units': 'N',
        'slope': 'both',
        'format': '%f',
        'description': 'storage related metric',
        'groups': 'latency'}

    descriptors = [tps_d, rd_sec_d, wr_sec_d, avgrq_sz_d, avgqu_sz_d, await_d, svctm_d, util_d]
    return descriptors

def metric_cleanup():
    '''Clean up the metric module.'''
    pass

#This code is for debugging and unit testing
if __name__ == '__main__':
    params = {'Param1': 'Some_Value'}
    metric_init(params)
    for d in descriptors:
        v = d['call_back'](d['name'])
        print 'value for %s is %u' % (d['name'], v)
