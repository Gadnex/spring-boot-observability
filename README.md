# Spring Boot Observability
A collection of Spring Boot sample projects and infrastrusture to demonstrate observability.

## What is Observability?
[Observability](https://www.ibm.com/cloud/learn/observability) provides deep visibility into modern distributed applications for faster, automated problem identification and resolution.

The cornerstones of Observability are:
- **Logging** -  Logs are granular, timestamped, complete and immutable records of application events. Among other things, logs can be used to create a high-fidelity, millisecond-by-millisecond record of every event, complete with surrounding context, that developers can 'play back' for troubleshooting and debugging purposes.
- **Metrics** - Metrics(sometimes called time series metrics) are fundamental measures of application and system health over a given period of time, such as how much memory or CPU capacity an application uses over a five-minute span, or how much latency an application experiences during a spike in usage.
- **Tracing** - Traces record the end-to-end 'journey' of every user request, from the UI or mobile app through the entire distributed architecture and back to the user.
- **Dependencies** (also called dependency maps) reveal how each application component is dependent on other components, applications and IT resources.

## Technologies to implement observability
In our demo application we are going to use the following Spring Boot friendly technologies to implement observability.

### Logging
- **logstash-logback-encoder** - Used to output application console logs in JSON format to improve indexing of logs in Elastic.
- **[Logstash](https://www.elastic.co/logstash/)** - 
- **[ElasticSearch](https://www.elastic.co/elasticsearch/)** - To ingest, store and index logs.
- **[Kibana](https://www.elastic.co/kibana/)** - To visualise and query the logs data in ElasticSearch.

### Metrics
- **spring-boot-starter-actuator** - 
- **micrometer-registry-prometheus** - 
- **[Prometheus](https://prometheus.io/)** - 
- **[Grafana](https://grafana.com/grafana/)** - 

### Tracing
- **spring-cloud-starter-sleuth** - 
- **spring-cloud-sleuth-zipkin** - 
- **[Jaeger](https://www.jaegertracing.io/)** - 

### Dependencies
This demo will strictly speaking not cover the dependencies cornerstone, but the tracing cornerstone will provide some insights into dependencies.
