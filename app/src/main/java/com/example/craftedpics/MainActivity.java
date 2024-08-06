package com.example.craftedpics;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.example.craftedpics.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;
    ActivityResultLauncher<Intent> resultLauncher;
    private Button newAlbumButton;
    private ImageView imageView ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.AlbumsFragment, R.id.CreateAlbumFragment, R.id.SettingsFragment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        View root = binding.getRoot();
        imageView = findViewById(R.id.image_view);
        onImageSelected();
        // newAlbumButton.setOnClickListener(view -> pickImage());

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
                            Toast.makeText(MainActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}



