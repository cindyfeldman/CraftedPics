package com.example.craftedpics;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.Manifest;

import com.example.craftedpics.databinding.FragmentCreatealbumBinding;

public class CreateAlbumFragment extends Fragment {
    private static final int REQUEST_READ_MEDIA_VISUAL_USER_SELECTED = 1;
    private FragmentCreatealbumBinding binding;
    private Button createAlbumButton;
    MainActivity activity;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreatealbumBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        createAlbumButton = root.findViewById(R.id.new_album_button);;
        createAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check for permission at runtime
//                if(ContextCompat.checkSelfPermission(createAlbumButton.getContext(), Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) != PackageManager.PERMISSION_GRANTED)
//                {
//                    //request camera permission
//                   ActivityCompat.requestPermissions(activity, new String[]{ Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED}, REQUEST_READ_MEDIA_VISUAL_USER_SELECTED);
//                   return;
//                }
                //open photo album
                Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
                startActivity(intent);
                Toast.makeText(root.getContext(), "Access to photos", Toast.LENGTH_LONG).show();
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}