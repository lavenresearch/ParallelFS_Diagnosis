#!/bin/sh
cp etc_cron.d_sysstat /etc/cron.d/sysstat
cp etc_sysconfig_sysstat /etc/sysconfig/sysstat
python ez_setup.py
easy_install ./kafka-python
