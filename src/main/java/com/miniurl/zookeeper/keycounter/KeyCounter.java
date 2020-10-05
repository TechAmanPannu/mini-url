package com.miniurl.zookeeper.keycounter;

import com.miniurl.utils.ObjUtil;
import com.miniurl.utils.Preconditions;
import com.miniurl.zookeeper.keycounter.model.Counter;
import com.miniurl.zookeeper.keycounter.model.Range;
import com.miniurl.zookeeper.keycounter.model.SubRange;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class KeyCounter {

    static final String RANGE_CLUSTER = "/key_ranges";

    static final String SUB_RANGE = RANGE_CLUSTER + "/range";

    static final String SUB_RANGE_BY_NAME = RANGE_CLUSTER + "/%s";

    // ============================================
    // Total subRanges = (END_AT / RANGE_DIFFERENCE)
    // Total ranges = (END_AT / (SUB_RANG_LIMIT * RANGE_DIFFERENCE))
    // Sub ranges under ranges = SUB_RANGE_LIMIT

    private static final long START_FROM = 100L;

    private static final long END_AT = 1000L;

    private static final long SUB_RANGE_LIMIT = 50L;

    private static final long RANGE_DIFFERENCE = 100L;

    // ===================================================
    private CuratorFramework curatorFramework;

    private Counter counter;

    public KeyCounter(final CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
        start();
    }

    public void start() {
        addCluster();
    }

    @SneakyThrows
    public boolean addCluster() {

        Stat stat = curatorFramework.checkExists().forPath(RANGE_CLUSTER);
        if (stat != null)
            return true;
        curatorFramework.create().creatingParentsIfNeeded()
                .forPath(RANGE_CLUSTER);

        return true;
    }

    @SneakyThrows
    public void addAllRanges() {

        List<SubRange> subRanges = new ArrayList<>();
        int subRangeCounter = 1;
        for (long startRange = START_FROM; startRange <= END_AT; startRange = startRange + RANGE_DIFFERENCE) {

            if (subRangeCounter == SUB_RANGE_LIMIT) {
                curatorFramework.create()
                        .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                        .forPath(SUB_RANGE, ObjUtil.getJsonAsBytes(subRanges));
                subRanges = new ArrayList<>();
                subRangeCounter = 1;
            }
            long endRange = startRange + RANGE_DIFFERENCE;
            subRanges.add(new SubRange(startRange, endRange));
            subRangeCounter++;
        }
    }

    @SneakyThrows
    public List<String> getAllRanges() {
        return curatorFramework.getChildren()
                .forPath(RANGE_CLUSTER);
    }


    public List<SubRange> getSubRanges(String rangeName) {

        try {
            return SubRange.asList(new String(curatorFramework.getData()
                    .forPath(String.format(SUB_RANGE_BY_NAME, rangeName)), StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("Exception while getting sub ranges ", e.getMessage(), e);
            return new ArrayList<>();
        }
    }


    public void updateRange(String rangeName, List<SubRange> subRanges) {

        if (ObjUtil.isNullOrEmpty(subRanges))
            deleteRange(rangeName);

        try {
            curatorFramework.setData()
                    .forPath(String.format(SUB_RANGE_BY_NAME, rangeName), ObjUtil.getJsonAsBytes(subRanges));
        } catch (Exception e) {
            log.error("Exception while updating range ", e.getMessage(), e);
        }
    }

    public void deleteRange(String rangeName) {
        try {
            curatorFramework.delete()
                    .forPath(String.format(SUB_RANGE_BY_NAME, rangeName));
        } catch (Exception e) {
            log.error("Exception while deleting range ", e.getMessage(), e);
        }

    }

    public synchronized long getCountAndIncr() {

        log.info("time of request :" + System.currentTimeMillis());
        if (this.counter == null)
            return createNewCounter();

        if (isCounterEnd())
            return updateCounter();

        long count = this.counter.getCount();
        counter.setCount(counter.getCount() + 1);

        log.info("count " + count);
        return count;

    }

    private boolean isCounterEnd() {
        return counter != null && counter.getCount() != null && counter.getSubRange() != null && counter.getCount() > counter.getSubRange().getEnd();
    }


    private long updateCounter() {

        String rangeName = counter.getRangeName();
        List<SubRange> subRanges = getSubRanges(rangeName);

        if (ObjUtil.isNullOrEmpty(subRanges)) {
            Range range = getValidRange();

            subRanges = range.getSubRanges();
            rangeName = range.getRangeName();
        }

        SubRange subRange = subRanges.get(0);
        counter = new Counter(subRange.getStart(), subRange, rangeName);
        subRanges.remove(0);
        updateRange(rangeName, subRanges);
        return getCountAndIncr();
    }

    private long createNewCounter() {

        Range range = getValidRange();

        String rangeName = range.getRangeName();
        List<SubRange> subRanges = range.getSubRanges();

        SubRange subRange = subRanges.get(0);
        counter = new Counter(subRange.getStart(), subRange, rangeName);
        subRanges.remove(0);
        updateRange(rangeName, subRanges);

        return getCountAndIncr();
    }

    private Range getValidRange() {

        Range range = new Range();

        List<String> ranges = getAllRanges();
        for (String rangeName : ranges) {

            List<SubRange> subRanges = getSubRanges(rangeName);
            if (ObjUtil.isNullOrEmpty(subRanges))
                continue;

            range.setSubRanges(subRanges);
            range.setRangeName(rangeName);
        }

        Preconditions.checkArgument(range == null, "Invalid range, send feedback to dev team");
        Preconditions.checkArgument(ObjUtil.isNullOrEmpty(range.getSubRanges()), "Invalid sub ranges, please send feedback to dev team");
        return range;
    }

}
