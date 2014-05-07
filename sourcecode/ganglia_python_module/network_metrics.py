import sys, os
descriptors = list()

def My_Callback_Function(name):
    '''Return a metric value.'''
    return <Some_Gathered_Value_Here>

def metric_init(params):
    '''Initialize all necessary initialization here.'''
    global descriptors

    if 'Param1' in params:
        p1 = params['Param1'])
    d1 = {'name': 'My_First_Metric',
        'call_back': My_Callback_Function,
        'time_max': 90,
        'value_type': 'uint',
        'units': 'N',
        'slope': 'both',
        'format': '%u',
        'description': 'Example module metric',
        'groups': 'example'}

    descriptors = [d1]
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
