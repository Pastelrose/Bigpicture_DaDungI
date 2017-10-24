package com.bigpicture.team.dadungi.item;

/**
 * Created by Soomin Jung on 2017-10-17.
 */

public class SearchItem {
    public String name;
    public String type;
    public String district;

    public SearchItem(String name, String type, String district){
        this.name = name;
        this.type=type;
        this.district = district;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
