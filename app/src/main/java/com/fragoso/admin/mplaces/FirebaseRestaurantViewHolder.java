package com.fragoso.admin.mplaces;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class FirebaseRestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;
    public static final String FIREBASE_CHILD_RESTAURANTS = "Postagens";
   private View mView;
   private Context mContext;
  private Geocoder geocoder;
   // Button btnButton1 ,btnButton2;
 private    Double lat = null, lon = null;
    public FirebaseRestaurantViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindRestaurant(Postagens post) {
       ImageButton gost =(ImageButton) mView.findViewById(R.id.gostar) ;
        TextView nameTextView = (TextView) mView.findViewById(R.id.tvComentario);
        TextView categoryTextView = (TextView) mView.findViewById(R.id.tvLocal);
        ImageView img =(ImageView) mView.findViewById(R.id.photos) ;
        TextView data =(TextView) mView.findViewById(R.id.tvData) ;
        geocoder = new Geocoder(mContext);
        lat = post.getLatitude();
        lon = post.getLongitude();


         Geolocalizacao geolocalizacao= new Geolocalizacao();




                String base64Image =post.getFotos();
        byte[] imageAsBytes = Base64.decode(base64Image.getBytes(), Base64.DEFAULT);
        img.setImageBitmap(
                BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
        );
        nameTextView.setText(post.getComentarios());
        categoryTextView.setText(geolocalizacao.localidade(lat,lon,geocoder));
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date datas = new Date(post.getData());
       data.setText( df.format(datas));
        //ratingTextView.setText("Rating: " + restaurant.getRating() + "/5");
    }

    @Override
    public void onClick(View view) {
        final ArrayList<Postagens> posts = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(FIREBASE_CHILD_RESTAURANTS);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    posts.add(snapshot.getValue(Postagens.class));
                }

                int itemPosition = getLayoutPosition();

                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("position", itemPosition + "");
               // intent.putExtra("restaurants", Parcels.wrap(restaurants));
                mContext.startActivity(intent);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}