package com.aicat.influxdemo.config;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
public class InfluxDBConnect implements InitializingBean {

    private static InfluxDB influxDB;
    private static final int maxActiveNum = 100;//最大化初始化连接数
    private static BlockingQueue<InfluxDB> connPool;
    @Value("${influx.openurl}")
    private String openurl; //连接地址
    @Value("${influx.database}")
    private String database; //数据库

    private String retentionPolicy; //保留策略

    void initPool() {
        connPool = new LinkedBlockingQueue<>(maxActiveNum);
        while (connPool.size() < maxActiveNum) {
            if (influxDBBuild() != null) {
                try {
                    connPool.put(influxDBBuild());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initPool();
        influxDBBuild();
    }

    /**
     * 连接时序数据库，获得influxDB
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public InfluxDB influxDBBuild() {
        if (influxDB == null) {
            if (openurl != null) {
                influxDB = InfluxDBFactory.connect(openurl, "", "");
                if (influxDB != null) {
                    boolean flag = influxDB.databaseExists(database);
                    if (!flag) {
                        influxDB.createDatabase(database);
                        createDefaultRetentionPolicy();//初始化创建保存策略
                    }
                }
            }
        }
        return influxDB;
    }

    public InfluxDB getInfluxDB() {
        if (connPool.size() > 0) {
            InfluxDB conn = null;
            try {
                conn = connPool.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (conn == null) {
                initPool();
            }
            return conn;
        } else {
            return influxDBBuild();
        }
    }

    public static void serInfluxDB(InfluxDB influxDB) {
        InfluxDBConnect.influxDB = influxDB;
    }

    /**
     * 创建数据库
     *
     * @param dbName
     */
    @SuppressWarnings("deprecation")
    public void createDB(String dbName) {
        getInfluxDB().createDatabase(dbName);
    }

    /**
     * 删除数据库
     *
     * @param dbName
     */
    @SuppressWarnings("deprecation")
    public void deleteDB(String dbName) {
        getInfluxDB().deleteDatabase(dbName);
    }

    /**
     * 测试连接是否正常
     *
     * @return
     */
    public boolean ping() {
        return getInfluxDB().ping() != null;
    }

    /**
     * 设置默认保留策略
     * 策略名：default； 保存天数：30天； 保存副本数量：1
     */
    public void createDefaultRetentionPolicy() {
        String command = String.format(
                "CREWATE RETENTION POLICY \"%s\" ON \"%s\" DURATION %s REPLICATION %s DEFAULT",
                "default", database, "30d", 1);
        this.query(command);
    }

    public void createRetentionPolicy(String policyName, String duration, int replication, boolean isDefault) {
        String command = String.format(
                "CREWATE RETENTION POLICY \"%s\" ON \"%s\" DURATION %s REPLICATION %s DEFAULT",
                policyName, database, duration, replication);
        if (isDefault) {
            command += "DEFAULT";
        }
        this.query(command);
    }

    /**
     * 查询
     *
     * @param command
     * @return
     */
    public QueryResult query(String command) {
        return getInfluxDB().query(new Query(command, database), TimeUnit.MILLISECONDS);//time显示为时间戳，
    }

    /**
     * 插入数据
     *
     * @param measurement 表名
     * @param tags        标签
     * @param fields      字段
     * @param time
     * @param timeUnit
     */
    public void insert(String measurement, Map<String, String> tags, Map<String, Object> fields, long time, TimeUnit timeUnit) {
        Point.Builder builder = Point.measurement(measurement);
        builder.tag(tags);
        builder.fields(fields);
        if (0 != time) {
            builder.time(time, timeUnit);
        }
        getInfluxDB().write(database, retentionPolicy, builder.build());
    }

    /**
     * 批量写入测点
     *
     * @param batchPoints
     */
    public void batchInsert(BatchPoints batchPoints) {
        getInfluxDB().write(batchPoints);
    }

    /**
     * 批量写入数据
     *
     * @param database         数据库
     * @param retentionPolicy  保存策略
     * @param consistencyLevel 一致性
     * @param records          要保存的数据（调用BatchPoints.lineProtocol()可得到一条record）
     */
    public void batchInsert(final String database, final String retentionPolicy, final InfluxDB.ConsistencyLevel consistencyLevel, final List<String> records) {
        getInfluxDB().write(database, retentionPolicy, consistencyLevel, records);
    }

    /**
     * 删除
     *
     * @param command
     * @return
     */
    public String deleteMeasurementData(String command) {
        return this.query(command).getError();
    }

    /**
     * 关闭数据库
     */
    public void close() {
        getInfluxDB().close();
    }

    /**
     * 构建Point
     *
     * @param measurement
     * @param time
     * @param tags
     * @param fields
     * @return
     */
    public Point pointBuilder(String measurement, long time, Map<String, String> tags, Map<String, Object> fields) {
        return Point.measurement(measurement).time(time, TimeUnit.MILLISECONDS).tag(tags).fields(fields).build();
    }

}
