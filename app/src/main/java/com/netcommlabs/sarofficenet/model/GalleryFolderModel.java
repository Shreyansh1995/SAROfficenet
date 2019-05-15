package com.netcommlabs.sarofficenet.model;

/**
 * Created by Flash_Netcomm on 2/19/2019.
 */

public class GalleryFolderModel {
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    String Name,Id,Image;
}
