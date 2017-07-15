package demo.functions.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMongoRepositories
public class MongoConfig {

    @Bean
    public MongoTemplate mongoTemplate(MongoDbFactory factory) {
        // Set custom date converters
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, new MongoMappingContext());
        List<Converter> converters = Arrays.asList(new DateReadingConverter(), new DateWritingConverter());
        converter.setCustomConversions(new CustomConversions(converters));
        converter.afterPropertiesSet();
        return new MongoTemplate(factory, converter);
    }
}
