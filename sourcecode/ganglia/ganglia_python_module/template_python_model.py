import sys, os
descriptors = list()

def My_Callback_Function(name):
    '''Return a metric value.'''
    return <Some_Gathered_Value_Here>

def metric_init(params):
    '''Initialize all necessary initialization here.'''
    global descriptors

    if 'Param1' in params:
        p1 = params['Param1']
    d1 = {'name': '<name>', #Name used in configuration
        'call_back': <handler_function>, #Call back function queried by gmond
        'time_max': int(<time_max>), #Maximum metric value gathering interval
        'value_type': '<data_type>', #Data type (string, uint, float, double)
        'units': '<label>', #Units label
        'slope': '<slope_type>', #Slope ('zero' constant values, 'both' numeric values)
        'format': '<format>', #String formatting ('%s', '%u','%f')
        'description': '<description>'} #Free form metric description

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
