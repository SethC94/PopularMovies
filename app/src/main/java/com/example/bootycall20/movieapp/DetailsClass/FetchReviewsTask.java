package com.example.bootycall20.movieapp.DetailsClass;

import android.os.AsyncTask;
import android.util.Log;

import com.example.bootycall20.movieapp.BuildConfig;
import com.example.bootycall20.movieapp.NetworkClass.MovieDatabaseService;
import com.example.bootycall20.movieapp.NetworkClass.Review;
import com.example.bootycall20.movieapp.NetworkClass.Reviews;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by BootyCall2.0 on 1/3/2017.
 */
public class FetchReviewsTask extends AsyncTask<Long, Void, List<Review>> {

    @SuppressWarnings("unused")
    public static String LOG_TAG = FetchReviewsTask.class.getSimpleName();
    private final Listener mListener;

    /**
     * Interface definition for a callback to be invoked when reviews are loaded.
     */
    interface Listener {
        void onReviewsFetchFinished(List<Review> reviews);
    }

    public FetchReviewsTask(Listener listener) {
        mListener = listener;
    }

    @Override
    protected List<Review> doInBackground(Long... params) {
        // If there's no movie id, there's nothing to look up.
        if (params.length == 0) {
            return null;
        }
        long movieId = params[0];

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieDatabaseService service = retrofit.create(MovieDatabaseService.class);
        Call<Reviews> call = service.findReviewsById(movieId,
                BuildConfig.THE_MOVIE_DATABASE_API_KEY);
        try {
            Response<Reviews> response = call.execute();
            Reviews reviews = response.body();
            return reviews.getReviews();
        } catch (IOException e) {
            Log.e(LOG_TAG, "A problem occurred talking to the movie db ", e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Review> reviews) {
        if (reviews != null) {
            mListener.onReviewsFetchFinished(reviews);
        } else {
            mListener.onReviewsFetchFinished(new ArrayList<Review>());
        }
    }
}
