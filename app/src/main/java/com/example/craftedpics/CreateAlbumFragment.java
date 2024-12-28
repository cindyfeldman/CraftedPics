package com.example.craftedpics;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;


import androidx.fragment.app.Fragment;

import com.example.craftedpics.databinding.FragmentCreatealbumBinding;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;


import java.util.List;


import com.google.gson.annotations.SerializedName;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;

public class CreateAlbumFragment extends Fragment {
    private FragmentCreatealbumBinding binding;
    private Button createAlbumButton;
    private EditText promptEditText;
    private TextView titleText;
    String apiEndpoint = "http://10.0.2.2:5000/predict";
    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;

    private ImageView imageView ;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreatealbumBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        createAlbumButton = root.findViewById(R.id.new_album_button);
        promptEditText = root.findViewById(R.id.prompt_edittext);
        promptEditText.setOnClickListener(view -> promptEditText.setText(""));
        imageView = root.findViewById(R.id.image_view);
        titleText = root.findViewById(R.id.title_prompt_textview);
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.server_client_id))
                .requestScopes( new Scope("https://www.googleapis.com/auth/photoslibrary.readonly"))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getContext(), googleSignInOptions);


        createAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, 123);

            }
        });

        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                String token = getResources().getString(R.string.api_token);// Use this token to make API requests
                fetchAlbums(token);
            }catch(Exception e){
                System.out.println("ERROR");
            }
        }
    }
    public class AlbumsResponse implements Result {
        @SerializedName("albums")
        private List<Album> albums;

        public List<Album> getAlbums() {
            return albums;
        }

        @NonNull
        @Override
        public Status getStatus() {
            return null;
        }
    }

    public class Album {
        @SerializedName("title")
        private String title;

        public String getTitle() {
            return title;
        }
    }
    public interface PhotosApiService {
        @GET("albums")
        Call<AlbumsResponse> getAlbums(@Header("Authorization") String authHeader);
    }
    private void fetchAlbums(String token) {
        String authHeader = "Bearer " + token;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://photoslibrary.googleapis.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PhotosApiService apiService = retrofit.create(PhotosApiService.class);
        Call<AlbumsResponse> call = apiService.getAlbums(authHeader);

        call.enqueue(new Callback<AlbumsResponse>() {
            @Override
            public void onResponse(Call<AlbumsResponse> call, retrofit2.Response<AlbumsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Album> albums = response.body().getAlbums();
                    StringBuilder sb = new StringBuilder();

                    for (Album album : albums) {
                        Log.d("Album", "Title: " + album.getTitle());
                        sb.append(album.getTitle() + " ");
                    }
                    titleText.setText(sb.toString());
                } else {
                    Log.e("API Error", "Response code: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<AlbumsResponse> call, Throwable t) {
                Log.e("API Error", t.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}