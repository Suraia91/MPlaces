package com.fragoso.admin.mplaces;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import static com.fragoso.admin.mplaces.R.id.photos;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener,camera.OnFragmentInteractionListener {

    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private Point mSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardview);

        Display display = getWindowManager().getDefaultDisplay();
        mSize = new Point();
        display.getSize(mSize);

        ImageView tirar = (ImageView) findViewById(R.id.tirarfoto);
        tirar.setOnClickListener(this);
        //Button btnRespostas = (Button)findViewById(R.id.BtnRespostas);
        // btnRespostas.setOnClickListener(this);

        // Button places = (Button)findViewById(R.id.places);
        // places.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            // case R.id.places:
            //camera cFragment = new camera();
            //FragmentTransaction ansaction = getSupportFragmentManager().beginTransaction();
            //ansaction.replace(R.id.camerafrag, cFragment).addToBackStack(null);
            //ansaction.commit();
            // launch();

            // Salvar(v);
            //  break;
            //  case R.id.salva:
            //  Salvar(v);
            // break;
            case R.id.tirarfoto:
                camera faze = new camera();
                FragmentTransaction nsaction = getSupportFragmentManager().beginTransaction();
                nsaction.replace(R.id.camerafrag, faze).addToBackStack(null);
                nsaction.commit();

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_CAMERA) {
            Uri photoUri = data.getData();
            // Get the bitmap in according to the width of the device
            Bitmap bitmap = ImageUtility.decodeSampledBitmapFromPath(photoUri.getPath(), mSize.x, mSize.x);
            ((ImageView) findViewById(photos)).setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void requestForCameraPermission(View view) {
        camera cFragment = new camera();
        FragmentTransaction ansaction = getSupportFragmentManager().beginTransaction();
        ansaction.replace(R.id.camerafrag, cFragment);
        ansaction.commit();

        //
        final String permission = android.Manifest.permission.CAMERA;
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                showPermissionRationaleDialog("Test", permission);
            } else {
                requestForPermission(permission);
            }
        } else {
            launch();

        }

    }

    private void launch() {
        Intent startCustomCameraIntent = new Intent(this, CameraActivity.class);
        startActivityForResult(startCustomCameraIntent, REQUEST_CAMERA);

    }

    private void showPermissionRationaleDialog(final String message, final String permission) {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.requestForPermission(permission);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    private void requestForPermission(final String permission) {
        ActivityCompat.requestPermissions(
                this, new String[]{permission}, REQUEST_CAMERA_PERMISSION);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}