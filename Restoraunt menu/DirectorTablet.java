package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.ad.Advertisement;
import com.javarush.task.task27.task2712.ad.StatisticAdvertisementManager;
import com.javarush.task.task27.task2712.statistic.StatisticManager;

import java.util.*;

public class DirectorTablet {

    public void printAdvertisementProfit(){
        Map<String, Double> result = StatisticManager.getInstance().getProfitForEveryDay();
        ArrayList<String> list = new ArrayList<>(result.keySet());
        Collections.sort(list);
        
        for (String date : list){
            double sum = 1.0 * result.get(date) / 100;
            System.out.println(date + " - " + String.format(Locale.ENGLISH, "%.2f", sum));
        }
    }

    public void printCookWorkloading(){
        Map<String, Map<String, Integer>> result = StatisticManager.getInstance().getCookWorkTime();
        ArrayList<String> list = new ArrayList<>(result.keySet());
        Collections.sort(list);

        for(String key : list){
            Map<String, Integer> values = result.get(key);
            System.out.println(key);
            
            ArrayList<String> cookNames = new ArrayList<>(values.keySet());
            Collections.sort(cookNames);
            for (String cookName : cookNames){
                System.out.println(cookName + " - " + (values.get(cookName)/60) + " min");
            }
            System.out.println();
        }
    }

    public void printActiveVideoSet() {
        List<Advertisement> active = StatisticAdvertisementManager.getInstance().getActiveVideoSet();
        Collections.sort(active, new Comparator<Advertisement>() {
            @Override
            public int compare(Advertisement o1, Advertisement o2) {
                return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
            }
        });

        for (Advertisement ad : active) {
            System.out.println(ad.getName() + " - " + ad.getHits());
        }
    }

   public void printArchivedVideoSet() {
            List<Advertisement> unActive = StatisticAdvertisementManager.getInstance().getUnActiveVideoSet();
            Collections.sort(unActive, new Comparator<Advertisement>() {
                @Override
                public int compare(Advertisement o1, Advertisement o2) {
                    return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
                }
            });
            for(Advertisement ad : unActive){
                System.out.println(ad.getName());
            }
        }
   }

