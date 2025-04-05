package com.windbell.mm.utils;


import java.util.concurrent.atomic.AtomicLong;

public class SnowflakeIdGenerator {
    private final long epoch = 1700000000000L; // 自定义起始时间戳
    private final long dataCenterId;
    private final long machineId;
    private final long sequenceMask = 4095L; // 12位序列号
    private final int dataCenterIdShift = 17;
    private final int machineIdShift = 12;
    private final int timestampShift = 22;
    private final AtomicLong lastTimestamp = new AtomicLong(-1L);
    private final AtomicLong sequence = new AtomicLong(0L);

    public SnowflakeIdGenerator(long dataCenterId, long machineId) {
        this.dataCenterId = dataCenterId;
        this.machineId = machineId;
    }

    public long nextId() {
        long timestamp = System.currentTimeMillis();
        long lastTs = lastTimestamp.get();

        if (timestamp < lastTs) {
            // 处理时钟回拨（等待直到时钟追上）
            timestamp = waitUntilNextMillis(lastTs);
        }

        if (timestamp == lastTs) {
            long seq = (sequence.incrementAndGet()) & sequenceMask;
            if (seq == 0) {
                // 序列号用完，等待下一个毫秒
                timestamp = waitUntilNextMillis(lastTs);
            }
        } else {
            sequence.set(0L);
        }

        lastTimestamp.set(timestamp);
        return ((timestamp - epoch) << timestampShift)
                | (dataCenterId << dataCenterIdShift)
                | (machineId << machineIdShift)
                | sequence.get();
    }

    private long waitUntilNextMillis(long lastTs) {
        long ts = System.currentTimeMillis();
        while (ts <= lastTs) {
            try {
                Thread.sleep(1); // 让出 CPU，避免空转
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            ts = System.currentTimeMillis();
        }
        return ts;
    }
}
