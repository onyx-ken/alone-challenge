package onyx.config;

import onyx.file.FileStorageStrategy;
import onyx.file.LocalFileStorageStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class FileStorageConfig {
    @Bean
    @Profile({"local", "test"})
    public FileStorageStrategy localFileStorageStrategy() {
        return new LocalFileStorageStrategy();
    }
}
