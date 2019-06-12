package com.example.getfood.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Keep this as a Singleton Class
 */
class FireBaseApiWrapper implements FireBaseApiWrapperInterface {

    private static FireBaseApiWrapper apiWrapper;
    private static DatabaseReference mDatabase;
//    todo: create a boolean for logging and enable it for dev/debug build flavors or changeable

    /**
     * Call to initialize various variables
     *
     * @return return instance of @{@link FireBaseApiWrapper}
     */
    static FireBaseApiWrapper getInstance() {
        if (apiWrapper == null) {
            apiWrapper = new FireBaseApiWrapper();
        }
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }
        return apiWrapper;
    }


    /**
     * Add a listener for child events occurring at this location. When child locations are added, removed, changed, or moved, the listener will be triggered for the appropriate event
     * Parameters
     * listener
     *
     * @param mDatabaseReference Path to which Child events to listen to
     * @param childEventListener The listener to be called with changes
     *                           Return A reference to the listener provided. Save this to remove the listener later.
     */
    @Override
    public void childEventListener(@NonNull DatabaseReference mDatabaseReference, final ChildEventListener childEventListener) {
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                childEventListener.onChildAdded(dataSnapshot, s);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                childEventListener.onChildChanged(dataSnapshot, s);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                childEventListener.onChildRemoved(dataSnapshot);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                childEventListener.onChildMoved(dataSnapshot, s);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                childEventListener.onCancelled(databaseError);
            }
        });
    }

    @Override
    public void valueEventListener(@NonNull DatabaseReference mDatabaseReference, final ValueEventListener eventListener) {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventListener.onDataChange(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                eventListener.onCancelled(databaseError);
            }
        });

    }

    /**
     * @param mDatabaseReference
     * @param singleValueEventListener
     */
    @Override
    public void singleValueEventListener(@NonNull DatabaseReference mDatabaseReference, final ValueEventListener singleValueEventListener) {
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                singleValueEventListener.onDataChange(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                singleValueEventListener.onCancelled(databaseError);
            }
        });
    }

    /**
     * Signs out user
     */
    @Override
    public void signOutUser() {
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void createNewUserWithEmailPassword(String userEmail, String password, final OnCompleteListener<AuthResult> onCompleteListener) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(userEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                onCompleteListener.onComplete(task);
            }
        });
    }

    @Override
    public void signInWithEmailAndPassword(@NonNull String userEmail, @NonNull String password, final OnCompleteListener<AuthResult> onCompleteListener) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                onCompleteListener.onComplete(task);
            }
        });
    }

    @Override
    public String getCurrentUserEmail() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            return FirebaseAuth.getInstance().getCurrentUser().getEmail();
        }
        return null;
    }

    @Override
    public void sendEmailVerification(ActionCodeSettings actionCodeSettings,
                                      final OnCompleteListener<Void> onCompleteListener, final OnFailureListener onFailureListener) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    onCompleteListener.onComplete(task);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    onFailureListener.onFailure(e);
                }
            });
        } else {
            onFailureListener.onFailure(new Exception("User not logged in"));
        }
    }

    @Override
    public boolean isUserVerified() {
        return FirebaseAuth.getInstance().getCurrentUser() != null && FirebaseAuth.getInstance().getCurrentUser().isEmailVerified();
    }
}
