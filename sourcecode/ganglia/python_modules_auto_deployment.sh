#!/bin/bash
# important!!!!
# this script do not work until local machine and remote machines have been configured to trust each other.
node_addr="192.168.3.140 192.168.3.141 192.168.3.142 192.168.3.143 192.168.3.144"
for addr in $node_addr
do
echo $addr
ssh root@$addr 'cd /home'
ssh root@$addr 'mkdir suyi'
scp /home/suyi/sysstat-10.2.1.tar root@$addr:/home/suyi/sysstat-10.2.1.tar
ssh root@$addr 'cd /home/suyi'
ssh root@$addr 'tar -xvf ./sysstat-10.2.1.tar'
ssh root@$addr 'cd sysstat-10.2.1'
ssh root@$addr './configure'
ssh root@$addr 'make'
ssh root@$addr 'make install'
scp /etc/cron.d/sysstat root@$addr:/etc/cron.d/sysstat
scp /etc/sysconfig/sysstat root@$addr:/etc/sysconfig/sysstat
scp /usr/lib64/ganglia/python_modules/storage_metrics.py root@$addr:/usr/lib64/ganglia/python_modules.py
scp /etc/ganglia/conf.d/storage_metrics.pyconf root@$addr:/etc/ganglia/conf.d/storage_metrics.pyconf
ssh root@$addr 'service gmond restart'
done
