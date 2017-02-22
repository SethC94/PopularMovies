package com.example.bootycall20.movieapp.NetworkClass;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BootyCall2.0 on 1/3/2017.
 */
public class Reviews {   @SerializedName("results")
private List<Review> reviews = new ArrayList<>();

    public List<Review> getReviews() {
        return reviews;
    }
}
