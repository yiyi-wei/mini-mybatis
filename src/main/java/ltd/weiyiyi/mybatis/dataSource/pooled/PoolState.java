package ltd.weiyiyi.mybatis.dataSource.pooled;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wei Han
 * @description
 * @date 2024/2/20 11:53
 * @domain www.weiyiyi.ltd
 */
public class PoolState {

    /**
     * 连哪个库（datasource）
     */
    protected PooledDataSource dataSource;

    /**
     * 活跃的连接
     */
    protected final List<PooledConnection> activeConnections = new ArrayList<>();

    /**
     * 空闲的连接
     */
    protected final List<PooledConnection> idleConnections = new ArrayList<>();

    /**
     * 请求次数
     */
    protected long requestCount = 0;
    /**
     * 总请求时间
     */
    protected long accumulatedRequestTime = 0;

    /**
     * 累积的Checkout时间
     */
    protected long accumulatedCheckoutTime = 0;
    protected long claimedOverdueConnectionCount = 0;
    protected long accumulatedCheckoutTimeOfOverdueConnections = 0;

    /**
     * 总等待时间
     */
    protected long accumulatedWaitTime = 0;
    /**
     * 等待过的次数
     */
    protected long hadToWaitCount = 0;
    /**
     * 失败连接次数
     */
    protected long badConnectionCount = 0;


    public PoolState(PooledDataSource dataSource) {
        this.dataSource = dataSource;
    }
}
