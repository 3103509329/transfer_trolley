package com.zhcx.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

import com.zhcx.spring.boot.job.annotation.JobScan;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class,CassandraDataAutoConfiguration.class})
@ImportResource("classpath:appcontext-dubbo-*.xml")
@JobScan("com.zhcx.**.job")
public class AuthorServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthorServiceApplication.class, args);
	}
}
