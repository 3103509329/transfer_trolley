package com.zhcx.netcar.netcarservice.service.impl.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.state.RocksDBConfigSetter;
import org.rocksdb.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/6/9 9:50
 **/
public class KafkaServiceUtil {

    private static final Logger log = LoggerFactory.getLogger(KafkaServiceUtil.class);

    public static Properties baseStreamsConfig(final String bootstrapServers, final String stateDir, final String appId) {
        return baseStreamsConfig(bootstrapServers, stateDir, appId, false);
    }
    public static Properties baseStreamsConfig(final String bootstrapServers, final String stateDir, final String appId, final boolean enableEOS) {
        final Properties config = new Properties();
        // Workaround for a known issue with RocksDB in environments where you have only 1 cpu core.
        config.put(StreamsConfig.ROCKSDB_CONFIG_SETTER_CLASS_CONFIG, CustomRocksDBConfig.class);
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, appId);
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(StreamsConfig.STATE_DIR_CONFIG, stateDir);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        final String processingGuaranteeConfig = enableEOS ? "exactly_once" : "at_least_once";
        config.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, processingGuaranteeConfig);
        //commit as fast as possible
        config.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 1);
        config.put(StreamsConfig.consumerPrefix(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG), 30000);

        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);

//        config.put(StreamsConfig.NUM_STANDBY_REPLICAS_CONFIG, 3);
//        config.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 3);

        MonitoringInterceptorUtils.maybeConfigureInterceptorsStreams(config);
        return config;
    }
    public static class CustomRocksDBConfig implements RocksDBConfigSetter {

        @Override
        public void setConfig(final String storeName, final Options options,
                              final Map<String, Object> configs) {
            // Workaround: We must ensure that the parallelism is set to >= 2.  There seems to be a known
            // issue with RocksDB where explicitly setting the parallelism to 1 causes issues (even though
            // 1 seems to be RocksDB's default for this configuration).
            final int compactionParallelism = Math.max(Runtime.getRuntime().availableProcessors(), 2);
            // Set number of compaction threads (but not flush threads).
            options.setIncreaseParallelism(compactionParallelism);
        }
    }

    public static void addShutdownHookAndBlock(final KafkaStreamService service) throws InterruptedException {
//        Thread.currentThread().setUncaughtExceptionHandler((t, e) -> service.stop());
        Thread.currentThread().setUncaughtExceptionHandler((t, e) -> {
            log.info("Service Exception : " + e);
        });
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                service.stop();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }));
    }

}
