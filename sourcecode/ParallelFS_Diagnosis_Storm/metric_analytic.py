import os
metric_distances = {}
f = open("metric_threshold.txt","r")
lines = list(f)
print lines
for line in lines:
    if line[0] == "M":
        words = line.split("\t")
	metric = words[0]
	value = float(words[1])
	if metric_distances.has_key(metric):
		if value > metric_distances[metric]:
			metric_distances[metric] = value
	else:
		metric_distances[metric] = value
f.close()
f = open("metric_value.txt","w")
for metric,value in metric_distances.items():
	f.write(metric+" "+str(value)+"\n")
f.close()

