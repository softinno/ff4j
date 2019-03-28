package org.ff4j.web.chart;

/*-
 * #%L
 * ff4j-web
 * %%
 * Copyright (C) 2013 - 2019 FF4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.ff4j.event.Event;
import org.ff4j.event.EventQueryDefinition;
import org.ff4j.event.Serie;
import org.ff4j.event.monitoring.HitCount;
import org.ff4j.utils.ColorsUtils;

/**
 * Utility class to render charts.
 */
public class ChartRenderer {
    
    /** Create key. */
    protected static final SimpleDateFormat KDF = new SimpleDateFormat("yyyyMMdd");
    
    /** Constant for colors. */
    private static final String COLOR_SOURCE_START = "AB008B";
    /** Constant for colors. */
    private static final String COLOR_SOURCE_END = "FFEEEE";
    
    /** Constant for colors. */
    private static final String COLOR_USER_START = "008BAB";
    /** Constant for colors. */
    private static final String COLOR_USER_END = "EEEEFF";
    
    /** Constant for colors. */
    private static final String COLOR_HOST_START = "00AB8B";
    /** Constant for colors. */
    private static final String COLOR_HOST_END = "EEFFEE";
    
    /** total hit count. */
    public static final String TITLE_PIE_HITCOUNT = "Total Hit Counts";
    /** distribution. */
    public static final String TITLE_BAR_HITCOUNT = "HitCounts Distribution";
    
    /**
     * Hide default constructor.
     */
    private ChartRenderer() {
    }
    
    /**
     * Generation of title.
     *
     * @param q
     *      currsent query
     * @return
     *      title formated with slot.
     */
    protected static String getTitle(EventQueryDefinition q) {
        return " FROM <b>" + getKeyDate(q.getFrom()) + "</b> TO <b>" + getKeyDate(q.getTo()) + "</b>";
    }
    
    /**
     * Convert hicount into pieChart.
     *
     * @param hitRatio
     *      current hist ratio
     * @return
     *      pie chart
     */
    private static PieChart renderPieChart(String title, Map < String, HitCount > hitRatio, List < String > colors) {
        PieChart pieChart = new PieChart(title);
        int idxColor = 0;
        for (String key : hitRatio.keySet()) {
            Serie<Integer> ps = new Serie<Integer>(key, hitRatio.get(key).get(), colors.get(idxColor));
            pieChart.getSectors().add(ps);
            idxColor++;
        }
        return pieChart;
    }
    
    /**
     * Convert hicount into pieChart.
     *
     * @param hitRatio
     *      current hist ratio
     * @return
     *      pie chart
     */
    protected static PieChart renderPieChartGradient(String title, Map < String, HitCount > hitRatio, String fromColor, String toColor) {
       return renderPieChart(title, hitRatio, ColorsUtils.getRGBGradientColors(fromColor, toColor, hitRatio.size()));
    }
    
    /**
     * Convert hicount into pieChart.
     *
     * @param hitRatio
     *      current hist ratio
     * @return
     *      pie chart
     */
    protected static PieChart renderPieChartRainBow(String title, Map < String,HitCount > hitRatio) {
        return renderPieChart(title, hitRatio, ColorsUtils.getColorsRainbowt(hitRatio.size()));
    }
    
    /**
     * Convert hicount into pieChart.
     *
     * @param hitRatio
     *      current hist ratio
     * @return
     *      pie chart
     */
    private static BarChart renderBarChart(String title, Map < String, HitCount > hitRatio, List < String > colors) {
        BarChart barChart = new BarChart(title);
        int idxColor = 0;
        for (String key : hitRatio.keySet()) {
            Serie<Integer> bar = new Serie<Integer>(key, new Double(hitRatio.get(key).get()).intValue(), colors.get(idxColor));
            barChart.getChartBars().add(bar);
            idxColor++;
        }
        orderBarDecrecent(barChart);
        return barChart;
    }
    
    protected static BarChart renderBarChartRainbow(String title, Map < String, HitCount > hitRatio) {
        return renderBarChart(title, hitRatio, ColorsUtils.getColorsRainbowt(hitRatio.size()));
    }
    
    protected static BarChart renderBarChartGradient(String title, Map < String, HitCount > hitRatio, String colorFrom, String colorTo) {
        return renderBarChart(title, hitRatio, ColorsUtils.getRGBGradientColors(colorFrom, colorTo, hitRatio.size()));
    }
    
    protected static void orderBarDecrecent(BarChart barChart) {
        Comparator<Serie<Integer>> c = new Comparator<Serie<Integer>>() {
            public int compare(Serie<Integer> o1, Serie<Integer> o2) {
                return o1.getValue() - o2.getValue();
            }
        };
        Collections.sort(barChart.getChartBars(), c);
    }
   
    
    // Feature Usage
    
    public static PieChart getFeatureUsagePieChart(EventQueryDefinition query, Map < String, HitCount> hitcounts) {
        return renderPieChartRainBow(TITLE_PIE_HITCOUNT + getTitle(query), hitcounts);
    }
    
    public static BarChart getFeatureUsageBarChart(EventQueryDefinition query, Map < String, HitCount> hitcounts) {
        return renderBarChartRainbow(TITLE_BAR_HITCOUNT + getTitle(query), hitcounts);
    }
    
    public static PieChart getHostPieChart(EventQueryDefinition q, Map < String, HitCount> hitcounts) {
        return renderPieChartGradient("HostNames " + getTitle(q), hitcounts, COLOR_HOST_START, COLOR_HOST_END);
    }
    public static PieChart getSourcePieChart(EventQueryDefinition q, Map < String, HitCount> hitcounts) {
        return renderPieChartGradient("Sources " + getTitle(q), hitcounts, COLOR_SOURCE_START, COLOR_SOURCE_END);
    }
    public static PieChart getUserPieChart(EventQueryDefinition q, Map < String, HitCount> hitcounts) {
        return renderPieChartGradient("Users " + getTitle(q), hitcounts, COLOR_USER_START, COLOR_USER_END);
    }
   
    public static int getFeatureUsageTotalHitCount(Map < String, HitCount> hitcounts) {
        int total = 0;
        if (hitcounts != null) {
            for (HitCount hc : hitcounts.values()) {
                total += hc.get();
            }
        }
        return total;
    }
    
    /**
     * Utility.
     *
     * @param evt
     *      current evenement
     * @param startTime
     *      begin time
     * @param endTime
     *      end time
     * @return
     *      if the event is between dates
     */
    protected static boolean isEventInInterval(Event evt, long startTime, long endTime) {
        return (evt.getTimestamp() >= startTime) && (evt.getTimestamp() <= endTime);
    }   
    
    /**
     * Format a timestamp to create a Key.
     *
     * @param time
     *      current tick
     * @return
     *      date as Key
     */
    protected static String getKeyDate(long time) {
        return KDF.format(new Date(time));
    }
    
    /**
     * Will get a list of all days between 2 dates.
     *
     * @param startTime
     *      tip start
     * @param endTime
     *      tip end
     * @return
     *      list of days
     */
    protected static Set < String > getCandidateDays(long startTime, long endTime) {
        Set < String > resultKeys = new TreeSet<String>();
        String endKey = getKeyDate(endTime);
        resultKeys.add(endKey);
        long time = startTime;
        while (!endKey.equals(getKeyDate(time))) {
            resultKeys.add(getKeyDate(time));
            time += 3600 * 1000 * 24;
        }
        return resultKeys;
    } 
    

}
