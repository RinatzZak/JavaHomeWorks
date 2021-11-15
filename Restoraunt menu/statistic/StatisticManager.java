package com.javarush.task.task27.task2712.statistic;

import com.javarush.task.task27.task2712.kitchen.Cook;
import com.javarush.task.task27.task2712.statistic.event.CookedOrderEventDataRow;
import com.javarush.task.task27.task2712.statistic.event.EventDataRow;
import com.javarush.task.task27.task2712.statistic.event.EventType;
import com.javarush.task.task27.task2712.statistic.event.VideoSelectedEventDataRow;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

public class StatisticManager {
    private static StatisticManager instance = new StatisticManager();

    private StatisticManager() {
    }

    public static StatisticManager getInstance() {
        if (instance == null) {
            instance = new StatisticManager();
        }
        return instance;
    }

    private StatisticStorage statisticStorage = new StatisticStorage();

    private class StatisticStorage {
        private Map<EventType, List<EventDataRow>> storage = new HashMap<>();

        private StatisticStorage() {
            for (EventType et : EventType.values()) {
                this.storage.put(et, new ArrayList<EventDataRow>());
            }
        }

        private void put(EventDataRow data) {
            EventType type = data.getType();
            if (!this.storage.containsKey(type))
                throw new UnsupportedOperationException();

            this.storage.get(type).add(data);
        }

        public Map<EventType, List<EventDataRow>> getStorage() {
            return storage;
        }
    }

    public void register(EventDataRow data) {
        this.statisticStorage.put(data);
    }




    public Map<String, Double> getProfitForEveryDay() {
        List<EventDataRow> rowList = statisticStorage.getStorage().get(EventType.SELECTED_VIDEOS);
        TreeMap<String, Double> treeMap = new TreeMap<>(Collections.reverseOrder());
        double sumOfProfit = 0.0;
        for (EventDataRow row : rowList) {
            VideoSelectedEventDataRow videoRow = (VideoSelectedEventDataRow) row;
            SimpleDateFormat simp = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            String date = simp.format(videoRow.getDate());
            if (!treeMap.containsKey(date)) {
                treeMap.put(date, 0.0);
            }
            sumOfProfit += videoRow.getAmount();
            treeMap.put(date, treeMap.get(date) + videoRow.getAmount());

            treeMap.put("Total", sumOfProfit);


        }
        return treeMap;
    }

    public Map<String, Map<String, Integer>> getCookWorkTime() {
        Map<String, Map<String, Integer>> res = new HashMap(); //name, time
        List<EventDataRow> rows = statisticStorage.getStorage().get(EventType.COOKED_ORDER);
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        for (EventDataRow row : rows) {
            CookedOrderEventDataRow dataRow = (CookedOrderEventDataRow) row;
            String date = format.format(dataRow.getDate());
            if (!res.containsKey(date)) {
                res.put(date, new HashMap<String, Integer>());
            }
            Map<String, Integer> cookMap = res.get(date);
            String cookName = dataRow.getCookName();
            if (!cookMap.containsKey(cookName)) {
                cookMap.put(cookName, 0);
            }

            Integer totalTime = cookMap.get(cookName);
            cookMap.put(cookName, totalTime + dataRow.getTime());
        }
        return res;
    }
}

