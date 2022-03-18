package io.flickd.twitter.model;

import java.io.Serializable;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.Data;

@Data
@Table("tweet_by_lang")
public class TweetData implements Serializable {

	@PrimaryKeyColumn(name = "lang", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    public String lang;
	
	@PrimaryKeyColumn(name = "createdat", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
	public String createdAt;

	@PrimaryKeyColumn(name = "id", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    private String id;
	
	@Column
	public String tweet;
	
	
	@Column
	public int sentiment;
}
