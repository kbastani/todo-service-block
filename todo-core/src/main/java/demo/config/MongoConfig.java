package demo.config;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMongoAuditing
@Profile({"dev", "cloud"})
public class MongoConfig extends AbstractMongoConfiguration {

	@Value("${spring.data.mongodb.host}")
	private String host;

	@Value("${spring.data.mongodb.port}")
	private Integer port;

	@Value("${spring.data.mongodb.database}")
	private String database;

	@Override
	public MongoClient mongoClient() {
		return new MongoClient(host, port);
	}

	@Override
	protected String getDatabaseName() {
		return database;
	}

	@Bean
	@Override
	public CustomConversions customConversions() {
		List<Converter> converters = Arrays.asList(new DateReadingConverter(), new DateWritingConverter());
		return new MongoCustomConversions(converters);
	}
}
