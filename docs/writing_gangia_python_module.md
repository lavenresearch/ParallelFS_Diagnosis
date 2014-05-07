# Writing Ganglia Python Module

## Step 1 : configure ganglia to use python module
TheConfiguration file for Mod_Python(__modpython.conf__) can usually be found in Ganglia’s etc/conf.d directory of the Ganglia installation.(In our system, the path is __/etc/ganglia/conf.d__)

    modules { 
        module { 
            name = "python_module" 
            path = "modpython.so" 
            /* 
            * The path that is assigned to the directive below will be passed down to the Mod_Python module and used to search for and locate the Python modules that should be loaded.
            *  For each Python module that it finds, it will attempt to also locate a matching module configuration block with a module name that corresponds with the Python module filename.
            */
            params = "<directory_path_for_python_modules>"
        }
    } 
    /*
    * each python module's configure file(.pyconf) should copied to the ganglia's etc/conf.d directory. 
    * so it is where those files are used.
    */
    include ('../etc/conf.d/*.pyconf')

## Step 2 : writing a python metric module
there are three functions that every Python module must include and implement: metric_init(params), metric_handler(name), and metric_cleanup(). 

### metric_init(params)

The primary purpose is to construct and return a dictionary of metric definitions, but it can also perform any other initialization functionality required to properly gather the intended metric set.

1. Python init function must be called metric_init and must take a single parameter.
2. The metric_init(params)function will be called once at initialization time. 

The __params__ parameter that is passed into the init function contains a dictionary of name/value pairs that were specified as configuration directives for the module in the module section of the __.pyconf__ module configuration file. (param is not a feature that must be used? not sure)

Each metric definition returned by the init function as a dictionary object, must supply at least the following elements:

        d = {'name': '<name>', #Name used in configuration
        'call_back': <handler_function>, #Call back function queried by gmond
        'time_max': int(<time_max>), #Maximum metric value gathering interval
        'value_type': '<data_type>', #Data type (string, uint, float, double)
        'units': '<label>', #Units label
        'slope': '<slope_type>', #Slope ('zero' constant values, 'both' numeric values)
        'format': '<format>', #String formatting ('%s', '%u','%f')
        'description': '<description>'} #Free form metric description

In addition to the required elements, the metric definition may contain additional elements. The additional elements will be ignored by gmond itself, but the name/value pairs will be included in the metric data packet as extra data. You can consider any additional elements as extra metadata for a metric. Metadata can be used to further identify or describe the metric and will be available to anything that consumes the metric XML produced directly by gmond or from gmetad.

### metric_handler(name)

Python metric module must implement at least one handler function, but it may also choose to implement more than one if needed. The handler definitions must be defined similar to the following: 

    def My_Metric_Handler(name): 

The value of the name parameter will be the name of the metric that is gathered. This is the name of the metric that was defined in the metric definition returned by the init function described previously. By passing the metric name as a parameter to the callback function, a handler function gains the ability to process more than one metric and to determine which metric is currently being gathered.

### metric_cleanup()

This function will be called once when gmond is shutting down. The cleanup function should include any code that is required to close files, disconnection from the network, or  any  other  type  of  clean  up  functionality.  Like  the  metric_init function,  the cleanup function must be called metric_cleanup and must not take any parameters. In addition, the cleanup function must not return a value. 
