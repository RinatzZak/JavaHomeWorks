package com.javarush.task.task27.task2712.ad;
import com.javarush.task.task27.task2712.statistic.StatisticManager;
import com.javarush.task.task27.task2712.statistic.event.VideoSelectedEventDataRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AdvertisementManager {
    private final AdvertisementStorage storage = AdvertisementStorage.getInstance();
    private int timeSeconds;


    public AdvertisementManager(int timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

    public void processVideos() throws NoVideoAvailableException {
        this.totalTimeLeft = Integer.MAX_VALUE;
        getOptimalVideoSet(new ArrayList<Advertisement>(), timeSeconds, 0l);

        VideoSelectedEventDataRow row = new VideoSelectedEventDataRow(optimalList, maxAmount, timeSeconds - totalTimeLeft);
        StatisticManager.getInstance().register(row);
        displayVideoSet();
        }


    private int totalTimeLeft;
    private long maxAmount;
    private List<Advertisement> optimalList;

    private void getOptimalVideoSet(List<Advertisement> total, int currentTimeLeft, long currentAmount){
        if (currentTimeLeft < 0){
            return;
        } else if (currentAmount > maxAmount || currentAmount == maxAmount
        && (totalTimeLeft > currentTimeLeft || totalTimeLeft == currentTimeLeft
        && total.size() < optimalList.size())) {
            this.maxAmount = currentAmount;
            this.totalTimeLeft = currentTimeLeft;
            this.optimalList = total;
            if (currentTimeLeft == 0){
                return;
            }
        }

        ArrayList<Advertisement> tmp = getActualAdvertisement();
        tmp.removeAll(total);
        for (Advertisement ad : tmp){
            if (!ad.isActive()) continue;
            ArrayList<Advertisement> currentNow = new ArrayList<>(total);
            currentNow.add(ad);
            getOptimalVideoSet(currentNow, currentTimeLeft - ad.getDuration(), currentAmount + ad.getAmountPerOneDisplaying());
        }

    }


    private ArrayList<Advertisement>  getActualAdvertisement() {
        ArrayList<Advertisement> actualList = new ArrayList<>();
        for (Advertisement ad : storage.list()){
            if (ad.isActive()){
                actualList.add(ad);
            }
        }
        return actualList;
    }

    private void displayVideoSet(){
        if (optimalList == null || optimalList.isEmpty()){
            throw new NoVideoAvailableException();
        }

        optimalList.sort((o1, o2) -> {
            long l = o2.getAmountPerOneDisplaying() - o1.getAmountPerOneDisplaying();
            return (int) l != 0 ? (int) l : o2.getDuration() - o1.getDuration();
        });

        for (Advertisement ad : optimalList){
            setStringForDisplay(ad);
            ad.revalidate();
        }
    }

    private void setStringForDisplay(Advertisement advertisement) {
        System.out.println(advertisement.getName() + " is displaying... " + advertisement.getAmountPerOneDisplaying()
                + ", "+ (1000 * advertisement.getAmountPerOneDisplaying() / advertisement.getDuration()));
    }
}

