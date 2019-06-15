package no.kobler.redisconfiguration;

import io.dropwizard.lifecycle.Managed;
import org.redisson.api.RedissonClient;
 
public class RedissonManaged implements Managed {
 
    private RedissonClient redissonClient;
 
    public RedissonManaged(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }
 
    @Override
    public void start() throws Exception {
    }
 
    @Override
    public void stop() throws Exception {
        redissonClient.shutdown();
    }
}