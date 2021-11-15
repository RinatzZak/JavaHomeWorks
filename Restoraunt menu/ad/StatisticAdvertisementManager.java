package com.javarush.task.task27.task2712.ad;

import java.util.ArrayList;
import java.util.List;

public class StatisticAdvertisementManager {
    private static StatisticAdvertisementManager statisticAdvertisementManager;
    private AdvertisementStorage advertisementStorage = AdvertisementStorage.getInstance();

    private StatisticAdvertisementManager(){}
    public static StatisticAdvertisementManager getInstance(){
        if (statisticAdvertisementManager == null){
            statisticAdvertisementManager = new StatisticAdvertisementManager();
        }
        return statisticAdvertisementManager;
    }

    public List<Advertisement> getActiveVideoSet(){
        List<Advertisement> video = advertisementStorage.list();
        List<Advertisement> activeVideo = new ArrayList<>();
        for (Advertisement ad : video){
            if (ad.isActive()){
                activeVideo.add(ad);
            }
        }
        return activeVideo;
    }

    public List<Advertisement> getUnActiveVideoSet(){
        List<Advertisement> video = advertisementStorage.list();
        List<Advertisement> unActiveVideo = new ArrayList<>();
        for(Advertisement ad : video){
            if(!ad.isActive()){
                unActiveVideo.add(ad);
            }
        }
        return unActiveVideo;
    }
}
