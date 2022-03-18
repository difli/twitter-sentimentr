# twitter-sentimentr

Sentiment analysis of twitter tweets powered by spring boot based applications and
- [local installation of apache cassandra and pulsar](#Quickstart-local-environment-setup) or
- [Astra DB and Astra Streaming](#Quickstart-powered-by-astra).

![alt text](/images/twitter-sentimentr.png)

[twitter-reader](/twitter-reader):
- consumes a [filtered stream of tweets from twitter api](https://developer.twitter.com/en/docs/twitter-api/tweets/filtered-stream/integrate/build-a-rule)
- publishes the tweets to pulsar topic from-twitterapi

[twitter-router-function](/twitter-router-function):
- [pulsar function](https://pulsar.apache.org/docs/en/functions-overview/)  
- subscribes to pulsar topic from-twitterapi and does content based routing.
- tweets in english language get published to to-en-sentimentr topic.
- all other tweets get published to to-db topic.

[tweet-db-sink](/pulsar-config-files):
- [pulsar sink](https://pulsar.apache.org/docs/en/io-overview/)  
- subscribes to pulsar topic to-db and streams/inserts all tweets into cassandra.

[twitter-sentimentr-service-en](/twitter-sentimentr-service-en):
- subscribes to to-en-sentimentr topic.
- does tweet sentiment analysis with [CoreNLP](http://nlp.stanford.edu/software/corenlp.shtml) library
- publishes tweets to to-db topic

[twitter-ui](/twitter-ui):
- subscribes to to-db topic and sends englisch tweets in realtime via websocket to all connected browsers.
- connects to apache cassandra to query tweets and sentiment over all languages
- visualizes tweets and the calculated sentiment

# Prerequisites
- please, create a twitter developer account in order to edit your [credentials](your-input-files/twitter-credentials.json.TEMPLATE) for accessing the twitter api:  https://developer.twitter.com/en/apps. remove extension '.TEMPLATE' from the file
- you can adapt [twitter filtered stream rule](https://developer.twitter.com/en/docs/twitter-api/tweets/filtered-stream/integrate/build-a-rule). Define it [here](env-file-docker.TEMPLATE). This to define a pattern for tweets to collect from twitter.   

# Quickstart local environment setup  
- download [apache cassandra](https://dlcdn.apache.org/cassandra/4.0.3/apache-cassandra-4.0.3-bin.tar.gz)
- install cassandra
- start cassandra
```
cd apache-cassandra-4.0.3
bin/cassandra -f
```
- create keyspace and table
```
cqlsh
CREATE KEYSPACE twitter WITH replication = { 'class': 'NetworkTopologyStrategy', 'datacenter1': 1 };
cqlsh -e CREATE TABLE twitter.tweet_by_lang (
    lang text,
    createdat text,
    id text,
    sentiment int,
    tweet text,
    PRIMARY KEY (lang, createdat, id)
) WITH CLUSTERING ORDER BY (createdat DESC, id DESC);
CREATE TABLE twitter.tweet_by_id (
    lang text,
    createdat text,
    id text,
    sentiment int,
    tweet text,
    PRIMARY KEY (id)
);
exit
```
- download [apache pulsar](https://archive.apache.org/dist/pulsar/pulsar-2.8.2/apache-pulsar-2.8.2-bin.tar.gz)
- install pulsar   
- start pulsar
```
cd apache-pulsar-2.8.2
bin/pulsar standalone
```
- create topics
```
bin/pulsar-admin topics create persistent://public/default/from-twitterapi
bin/pulsar-admin topics create persistent://public/default/to-en-sentimentr
bin/pulsar-admin topics create persistent://public/default/to-db
```
- list topics
```
bin/pulsar-admin topics list public/default
```
- pull the application container images (important to start the apps before you continue with function and sink setup!)
```
docker pull dieterfl/twitter-sentimentr-en-service:latest
docker pull dieterfl/twitter-ui:latest
docker pull dieterfl/twitter-reader:latest
```
- review [env-file-docker](env-file-docker.TEMPLATE) and remove the '.TEMPLATE' extension - ensure spring profile 'default' is configured
- start your applications
```
sh run-apps-in-docker.sh
```
- once the applications are up and running try to connect your browser to twitter-ui via browser: http://localhost:8081
- create a connectors folder in the pulsar base folder
```
mkdir connectors
```
- download DataStax Apache Pulsar Connector: https://downloads.datastax.com/#apc
- copy the connector into the just created connectors folder
```
cp PATH-TO-DOWNLOADED-CONNECTOR/cassandra-enhanced-pulsar-sink-1.5.0-nar.nar PULSAR-BASE-FOLDER/connectors
```
- create the cassandra sink (adapt the command line properties)
```
bin/pulsar-admin sinks reload
bin/pulsar-admin sinks available-sinks
bin/pulsar-admin sinks create --name tweet-db-sink --sink-type cassandra-enhanced --inputs persistent://public/default/to-db --sink-config-file /Users/dieter.flick/Documents/development/workspaces/workspace-datastax/twitter-sentimentr/pulsar-config-files/tweet-db-sink.yml
bin/pulsar-admin sinks list
```
- download [tweet-router](https://github.com/difli/twitter-sentimentr/releases/download/v1.0.0/twitter-router-0.0.1-SNAPSHOT.jar) function or use the one you have build yourself
- create the tweet-router function (adapt the command line properties)
```
bin/pulsar-admin functions create --jar /Users/dieter.flick/Documents/development/bin/pulsar-admin functions getstatus --name tweet-router
workspaces/workspace-datastax/tweet-router/target/twitter-router-0.0.1-SNAPSHOT.jar --function-config-file /Users/dieter.flick/Documents/development/workspaces/workspace-datastax/tweet-router/local-function-config.yaml
```
- Done !!!
- You should now see tweets appearing http://localhost:8081  

# Quickstart powered by astra   
- create your astra account: https://astra.datastax.com/
- create database with keyspace 'twitter'
- use cql concole and create tables
```
CREATE TABLE twitter.tweet_by_lang (
    lang text,
    createdat text,
    id text,
    sentiment int,
    tweet text,
    PRIMARY KEY (lang, createdat, id)
) WITH CLUSTERING ORDER BY (createdat DESC, id DESC);
CREATE TABLE twitter.tweet_by_id (
    lang text,
    createdat text,
    id text,
    sentiment int,
    tweet text,
    PRIMARY KEY (id)
);
```
- download your database secure connect bundle and copy to [your-input-files](your-input-files)
- create and download a 'database administrator' token csv file
- fill in username, password, client id and client secret from the downloaded csv file in [env-file-docker](env-file-docker.TEMPLATE)
- create streaming / create tenant
- Create topics from-twitterapi, to-en-sentimentr and to-db
- fill in the full names of your topics in [env-file-docker](env-file-docker)
- get astra streaming connection details
- click connect and get the Broker Service URL and fill in [env-file-docker](env-file-docker)
- create a token and fill in [env-file-docker](env-file-docker)
- pull the application container images (important to start the apps before you continue with function and sink setup!)
```
docker pull dieterfl/twitter-sentimentr-en-service:latest
docker pull dieterfl/twitter-ui:latest
docker pull dieterfl/twitter-reader:latest
```
- review [env-file-docker](env-file-docker.TEMPLATE) and remove the '.TEMPLATE' extension - ensure spring profile 'astra' is configured
- start your applications
```
sh run-apps-in-docker.sh
```
- once the applications are up and running try to connect your browser to twitter-ui via browser: http://localhost:8081
- create a sink that connects to your astra db in astra streaming
```
namespace = default
sink type = astra db
name = tweet-db-sink
connect-topic=to-db
database=
keyspace=twitter
table=tweet_by_lang
token=Astra DB token
mapping=lang=value.lang,id=value.id,tweet=value.tweet,createdat=value.createdAt,sentiment=value.sentiment
```
- Double check the mapping for createdat=value.createdAt
- hit create
- download [tweet-router](https://github.com/difli/twitter-sentimentr/releases/download/v1.0.0/twitter-router-0.0.1-SNAPSHOT.jar) function or use the one you have build yourself
- create tweet-router (function) for content based routing of tweets
```
name=tweet-router
namespace=default
upload twitter-router-0.0.1-SNAPSHOT.jar
choose function io/flickd/twitter/pulsar/functions/TweetRouter
choose input topic=from-twitterapi
```
hit create

- Done !!!
- You should now see tweets appearing http://localhost:8081  
