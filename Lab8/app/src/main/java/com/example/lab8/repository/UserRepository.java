package com.example.lab8.repository;

import androidx.annotation.NonNull;
import com.example.lab8.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserRepository {
    private final DatabaseReference database;

    public UserRepository() {
        database = FirebaseDatabase.getInstance().getReference("users");
    }

    public void getUserByEmail(String email, UserCallback callback) {
        database.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = null;
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        user = userSnapshot.getValue(User.class);
                        break;
                    }
                }
                callback.onUserRetrieved(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.toException());
            }
        });
    }

    public interface UserCallback {
        void onUserRetrieved(User user);
        void onError(Exception exception);
    }
}
