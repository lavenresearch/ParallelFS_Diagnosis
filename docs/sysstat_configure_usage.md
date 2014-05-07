# sysstat configuration and usage

## daily log
1. make sysstat record system statement in daily log
    * /etc/cron.d/sysstat to set recording frequency
    * /etc/sysconfig/sysstat to choose metrics to monitor
        - SADC_OPTIONS = "-S XALL" for recording everything include storage devices' information
2. use command "sadf" for reading daily log
    * -d means produce database friendly outputs
    * -s 08:00:00 means only print system status after the specified time
    * 1 1 means interval and count
    * -- -d means only include storage device related information
