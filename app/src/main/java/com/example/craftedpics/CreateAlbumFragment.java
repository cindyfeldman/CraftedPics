package com.example.craftedpics;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.api.client.*;

import android.Manifest;

import com.example.craftedpics.databinding.FragmentCreatealbumBinding;
import com.google.api.client.auth.oauth2.Credential;


import java.io.FileInputStream;
import java.io.IOException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Collections;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.UserCredentials;

import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

public class CreateAlbumFragment extends Fragment {
    private static final int REQUEST_READ_MEDIA_VISUAL_USER_SELECTED = 1;
    private FragmentCreatealbumBinding binding;
    private Button createAlbumButton;
    private EditText promptEditText;
    String apiEndpoint = "http://10.0.2.2:5000/predict";
    Credential credential;
    MainActivity activity;
    ActivityResultLauncher<Intent> resultLauncher;
    private ImageView imageView ;
    private long mTokenExpired;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreatealbumBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        createAlbumButton = root.findViewById(R.id.new_album_button);
        promptEditText = root.findViewById(R.id.prompt_edittext);
        promptEditText.setOnClickListener(view ->  promptEditText.setText(""));
        imageView = root.findViewById(R.id.image_view);
        onImageSelected();

        createAlbumButton.setOnClickListener(view -> pickImage());
        //createAlbumButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
                //check for permission at runtime
//                if(ContextCompat.checkSelfPermission(createAlbumButton.getContext(), Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) != PackageManager.PERMISSION_GRANTED)
//                {
//                    //request camera permission
//                   ActivityCompat.requestPermissions(activity, new String[]{ Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED}, REQUEST_READ_MEDIA_VISUAL_USER_SELECTED);
//                   return;
//                }
                //open photo album
//                Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
//                startActivity(intent);
//                Toast.makeText(root.getContext(), promptEditText.getText(), Toast.LENGTH_SHORT).show();


          //}
       // });

        return root;
    }

    private void pickImage(){
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);

    }



    private void onImageSelected() {
        resultLauncher =  registerForActivityResult (
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try {
                            Uri imageUri = result.getData().getData();
                            imageView.setImageURI(imageUri);
                        }catch(Exception e){
                            Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}