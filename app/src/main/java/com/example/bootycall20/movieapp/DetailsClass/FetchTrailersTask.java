package com.example.bootycall20.movieapp.DetailsClass;

import android.os.AsyncTask;
import android.util.Log;

import com.example.bootycall20.movieapp.BuildConfig;
import com.example.bootycall20.movieapp.NetworkClass.MovieDatabaseService;
import com.example.bootycall20.movieapp.NetworkClass.Trailer;
import com.example.bootycall20.movieapp.NetworkClass.Trailers;

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
public class FetchTrailersTask extends AsyncTask<Long, Void, List<Trailer>> {

    @SuppressWarnings("unused")
    public static String LOG_TAG = FetchTrailersTask.class.getSimpleName();
    private final Listener mListener;

    /**
     * Interface definition for a callback to be invoked when trailers are loaded.
     */
    interface Listener {
        void onFetchFinished(List<Trailer> trailers);
    }

    public FetchTrailersTask(Listener listener) {
        mListener = listener;
    }

    @Override
    protected List<Trailer> doInBackground(Long... params) {
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
        Call<Trailers> call = service.findTrailersById(movieId,
                BuildConfig.THE_MOVIE_DATABASE_API_KEY);
        try {
            Response<Trailers> response = call.execute();
            Trailers trailers = response.body();
            return trailers.getTrailers();
        } catch (IOException e) {
            Log.e(LOG_TAG, "A problem occurred talking to the movie db ", e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Trailer> trailers) {
        if (trailers != null) {
            mListener.onFetchFinished(trailers);
        } else {
            mListener.onFetchFinished(new ArrayList<Trailer>());
        }
    }
}
