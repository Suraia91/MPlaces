package com.fragoso.admin.mplaces;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MPost extends Fragment {
    private Firebase ref;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference mRestaurantReference;
    private FirebaseRecyclerAdapter mFirebaseAdapter;
    String FIREBASE_CHILD_RESTAURANTS = "Postagens";
    Postagens post = new Postagens() ;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mpost,
                container, false);
        Firebase.setAndroidContext(getContext());
        ref = new Firebase("https://mypplaces.firebaseio.com/");

        recyclerView =
                (RecyclerView)view.findViewById(R.id.rv);
        mRestaurantReference = FirebaseDatabase.getInstance().getReference("Postagens");


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String child="";
            String myInt = bundle.getString("key", child);
            setUpFirebaseAdaptert(child);

        }
        else {
            setUpFirebaseAdapter();
        }
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    private void setUpFirebaseAdapter() {
        Firebase.setAndroidContext(getContext());

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Postagens, FirebaseRestaurantViewHolderAdm>
                (Postagens.class, R.layout.cardview, FirebaseRestaurantViewHolderAdm.class,
                        mRestaurantReference) {

            @Override
            protected void populateViewHolder(FirebaseRestaurantViewHolderAdm viewHolder, Postagens model, final int position) {
                viewHolder.bindRestaurant(model);
                SwipeableRecyclerViewTouchListener swipeTouchListener =
                        new SwipeableRecyclerViewTouchListener( recyclerView , new SwipeableRecyclerViewTouchListener.SwipeListener(){
                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                String key=  mFirebaseAdapter.getRef(position).getKey();

                                mRestaurantReference = FirebaseDatabase.getInstance().getReference("Postagens");
                                mRestaurantReference.child(key).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                                    @Override
                                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                                        Postagens post= new Postagens();
                                        post=dataSnapshot.getValue(Postagens.class);
                                        dataSnapshot.getRef().removeValue();


                                        ref.child("Postagens").child("Posts").push().setValue(post);
                                        Snackbar.make(getView(), "Postagens feita", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.w("Erros", "getUser:onCancelled", databaseError.toException());

                                    }
                                });

                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {

                            }

                           /* @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                String key=  mFirebaseAdapter.getRef(position).getKey();

                                mRestaurantReference = FirebaseDatabase.getInstance().getReference("Postagens");
                                mRestaurantReference.child(key).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                                    @Override
                                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                                        Postagens post= new Postagens();
                                        post=dataSnapshot.getValue(Postagens.class);
                                        dataSnapshot.getRef().removeValue();
                                        Snackbar.make(getView(), "Reclamação Fechada", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();

                                        ref.child("Reclamacoes").child("Fechadas").push().setValue(recl);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.w("Erros", "getUser:onCancelled", databaseError.toException());

                                    }
                                });

                            }*/
                        });

                recyclerView.addOnItemTouchListener(swipeTouchListener);



            }
        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mFirebaseAdapter);
    }
    private void setUpFirebaseAdaptert(String child) {
        Firebase.setAndroidContext(getContext());
        mRestaurantReference = FirebaseDatabase.getInstance().getReference("Reclamacoes"+ child);
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Postagens, FirebaseRestaurantViewHolderAdm>
                (Postagens.class, R.layout.cardview, FirebaseRestaurantViewHolderAdm.class,
                        mRestaurantReference) {

            @Override
            protected void populateViewHolder(FirebaseRestaurantViewHolderAdm viewHolder, Postagens model, final int position) {
                viewHolder.bindRestaurant(model);
                SwipeableRecyclerViewTouchListener swipeTouchListener =
                        new SwipeableRecyclerViewTouchListener( recyclerView , new SwipeableRecyclerViewTouchListener.SwipeListener(){
                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                String key=  mFirebaseAdapter.getRef(position).getKey();

                                mRestaurantReference = FirebaseDatabase.getInstance().getReference("Reclamacoes");
                                mRestaurantReference.child(key).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                                    @Override
                                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                                        Postagens post= new Postagens();
                                        post=dataSnapshot.getValue(Postagens.class);
                                        dataSnapshot.getRef().removeValue();


                                        ref.child("Reclamacoes").child("Andamento").push().setValue(post);
                                        Snackbar.make(getView(), "Reclamação em adamento", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.w("Erros", "getUser:onCancelled", databaseError.toException());

                                    }
                                });

                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {

                            }

                          /*  @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                String key=  mFirebaseAdapter.getRef(position).getKey();

                                mRestaurantReference = FirebaseDatabase.getInstance().getReference("Reclamacoes/Aberto");
                                mRestaurantReference.child(key).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                                    @Override
                                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                                        Reclamacao recl= new Reclamacao();
                                        recl=dataSnapshot.getValue(Reclamacao.class);
                                        dataSnapshot.getRef().removeValue();
                                        Snackbar.make(getView(), "Reclamação Fechada", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();

                                        ref.child("Reclamacoes").child("Fechadas").push().setValue(recl);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.w("Erros", "getUser:onCancelled", databaseError.toException());

                                    }
                                });

                            }*/
                        });

                recyclerView.addOnItemTouchListener(swipeTouchListener);



            }
        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mFirebaseAdapter);
    }
    @Override
    public void  onStart(){
        super.onStart();

    }
}
