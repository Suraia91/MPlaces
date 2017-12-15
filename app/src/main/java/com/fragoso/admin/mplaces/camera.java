package com.fragoso.admin.mplaces;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;



public class camera extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener , LocationListener, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private int  REQUEST_LOC = 0;
    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private Point mSize;
    private ImageView ivImage;
    private EditText comentario;
    private boolean estado = true;
    private String mdata;
    private CoordinatorLayout coordinatorLayout;
    Location location;
    private double latitude;
    private double longitude;
    private Spinner btnCaten;
    private String categoria;
    private ImageView salva;
    private Firebase ref;
    private GoogleApiClient mGoogleApiClient;
    private View mLayout,mLoc;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        mSize = new Point();
        display.getSize(mSize);
        showLoc(mLoc);
        requestLocPermission();

        Firebase.setAndroidContext(getContext());
        ref = new Firebase("https://krukuteka.firebaseio.com/");
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            Firebase.setAndroidContext(getContext());
            //Notificare();

        }
    }
        @Override
        public View onCreateView (LayoutInflater inflater, ViewGroup container,
                                  Bundle savedInstanceState){
            View view =
                    // Inflate the layout for this fragment
                    inflater.inflate(R.layout.fragment_camera, container, false);
            ivImage = (ImageView) view.findViewById(R.id.ivImage);
            comentario = (EditText) view.findViewById(R.id.edtcomentario);
            salva = (ImageView) view.findViewById(R.id.upload);
            salva.setOnClickListener(this);
            return view;

        }
    private void Salvar (View view)
    {

        boolean hasDrawable = ( ivImage.getDrawable() != null);
        if( comentario.getText().toString().trim().equals(""))
        {
            comentario.setError( "É obrigatorio fazer a descricao!" );

            comentario.setHint("Por favor descreva a situacao");

        }else if(hasDrawable==false) {
            Snackbar.make(view, "Deve fazer uma fotografia para acompanhar a sua reclmacao", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else if (!checkNetworkStatus()){
            //comentario.setError("inernet fail contecta se");

            Snackbar.make(view, "E necessario conectar se a uma internet para fazer a sua reclamacao", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        else {
            try {
                Bitmap image = ((BitmapDrawable) ivImage.getDrawable()).getBitmap();

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                String problema = comentario.getText().toString().trim();

                //Bundle bundle = getActivity().getIntent().getExtras();
               // String email = bundle.getString("email");
                Postagens recl = new Postagens();
                //Adding values
                // recl.setImage(image);
                recl.setComentarios(problema);
                recl.setLatitude(latitude);
                recl.setLongitude(longitude);
                recl.setImage(encoded);
                recl.setData(System.currentTimeMillis());
               // recl.setEmail(email);
                ref.child("Postagens/Todas").push().setValue(recl);
                Snackbar.make(view, "Post registrada com sucesso", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                ivImage.setImageBitmap(null);
                comentario.setText("");

                // do something
            } catch (Exception e) {
                Toast.makeText(getContext(),
                        "Ocorreu um erro ao fazer a reclamacao, Desculpa", Toast.LENGTH_LONG).show();

            }
           // NMorador();

        }


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload:
                Salvar(v);
                break;
        }

    }


    private void showPermissionRationaleDialog(final String message, final String permission) {
        new android.support.v7.app.AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        camera.this.requestForPermission(permission);
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
        ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, REQUEST_CAMERA_PERMISSION);

    }

    private void launch() {
        Intent startCustomCameraIntent = new Intent(getContext(), CameraActivity.class);
        startActivityForResult(startCustomCameraIntent, REQUEST_CAMERA);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                final int numOfRequest = grantResults.length;
                final boolean isGranted = numOfRequest == 1
                        && PackageManager.PERMISSION_GRANTED == grantResults[numOfRequest - 1];
                if (isGranted) {
                    launch();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
             throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public boolean  checkNetworkStatus() {
        boolean retorn = false;

        ConnectivityManager conMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {

            retorn = true;

        } else if (conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {

            retorn = false;


        }
        return retorn;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            mdata = String.valueOf(mLastLocation.getTime());
        }

        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(5000);
        request.setFastestInterval(1000);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,request, (LocationListener) this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
    private void vServico() {
        Boolean gps_enabled = false, network_enabled = false;
        LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        if (!gps_enabled && !network_enabled) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle("Location settings");
            dialog.setMessage("We cannot retrieve your location. Please click on Settings and make sure your Location services is enabled");
            dialog.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            /*Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            frag.getActivity().startActivity(intent);*/
                        }
                    });
            dialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //tryLocating = false;
                            dialog.cancel();
                        }
                    });
            dialog.show();

            dialog.show();
        }
    }
    public void showLoc(View view) {
        // Log.i(TAG, "Show camera button pressed. Checking permission.");
        // BEGIN_INCLUDE(camera_permission)
        // Check if the Camera permission is already available.
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Camera permission has not been granted.

            requestLocPermission();

        }
        // END_INCLUDE(camera_permission)

    }
    private void requestLocPermission() {
        // Log.i(TAG, "CAMERA permission has NOT been granted. Requesting permission.");

        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            //  Log.i(TAG,
            //        "Displaying camera permission rationale to provide additional context.");
            Snackbar.make(mLoc, R.string.permission_loc_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_LOC);
                        }
                    })
                    .show();
        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOC);
        }
        // END_INCLUDE(camera_permission_request)
    }
    /*private void Notificare()
    {


        final Firebase data = new Firebase("https://krukuteka.firebaseio.com/Reclamacoes/Andamento");
        data.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {



            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {



            }

         *//*   @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                Intent resultIntent = new Intent(getContext(), RecActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getContext());
                stackBuilder.addParentStack(MainActivity.class);
// Because clicking the notification opens a new ("special") activity, there's
// no need to create an artificial back stack.
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_NO_CREATE);
                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getContext())
                                .setSmallIcon(R.drawable.notificacao)
                                .setPriority(Notification.PRIORITY_HIGH)
                                //  .setLargeIcon(largeIcon)
                                .setContentTitle("Krukuteca")
                                .setContentText("Acabamos de solucionar mais um problema")
                                .setTicker("")
                                .setSound(soundUri)
                                .setContentIntent(resultPendingIntent)
                                .setAutoCancel(true);


                //  Bitmap largeIcon = BitmapFreactory.decodeResource(getResources(), R.drawable.notification);
                // Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.notification);



                // PendingIntent resultPendingIntent;
                mBuilder.setContentIntent(resultPendingIntent);
// Sets an ID for the notification
                int mNotificationId = 001;
// Gets an instance of the NotificationManager service
                NotificationManager mNotifyMgr =
                        (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.

                mNotifyMgr.notify(mNotificationId, mBuilder.build());
                //mBuilder.setAutoCancel(true);

            }*//*

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }*/
  /*  private void NMorador(){
        Firebase ref= new Firebase("https://krukuteka.firebaseio.com/Usuarios/Administracao");
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Log.i("MainActivity", child.getKey());
                    Salvarnotifica(child.getKey());

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("MainActivity", "onCancelled", firebaseError.toException());
            }
        });

    }*/
  /*  private void Salvarnotifica(String solucao){
//        Toast.makeText(MainActivity.this, "Authentication failed." + solucao,
//                Toast.LENGTH_LONG).show();
        Firebase dado= new Firebase("https://krukuteka.firebaseio.com/Notificacao/Administracao");
        dado.child(solucao).child("Notificacao").push().setValue("Notificacao");
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
}
