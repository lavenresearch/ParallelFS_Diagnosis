# 2 kinds of problems
1. disk busy[^mistake]: cause by cron jobs like _updatedb_ for GNU _locate_ etc. this is due to file server OS itself.
2. disk hog[^mistake]: cause by third-party/unmonitored node run a disk hog process on the shared storage devices. this increase throughput of the file server.
3. network hog: result from traffic-emitter like a backup process, or the receipt of data during a denial-of-service attack. view increase throughput.
4. packet loss(network busy): result of network congestion.

![metrics](/docs/parallel fs fault analysis.png)

# fault diagnosis
peer-divergence

1. throughput
    * storage
        - tps: I/O requests to disk per second
        - rd_sec: sectors read from disk per second
        - wr_sec: sectors write from disk per second
        - avgrq-sz: average sector amount per request
    * network
        - rxpck: packets received per second
        - txpck: packets transmitted per second
        - rxbyt: bytes received per second
        - txbyt: bytes transmitted per second
2. latency
    * avgqu-sz: average number of queued disk I/O requests
    * await: average time to response I/O requests( only disk servicing time, queuing delay not included)
3. cwnd
    * cwnd: number of segments allowed to be sent outstanding without acknowledgment( congestion control window )

# tools/source for data collection
1. [sysstat](http://sebastien.godard.pagesperso-orange.fr)
2. /proc/net/tcp

[^mistake]: 这两个定义有问题，应该是相反的！
