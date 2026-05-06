package com.example.lab8.viewmodel;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.lab8.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginViewModel extends ViewModel {
    private static final String TAG = "LoginViewModel";
    private final DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users");
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public LiveData<User> login(String email) {
        userReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "Số lượng bản ghi dữ liệu: " + snapshot.getChildrenCount());
                User foundUser = null;
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        if (user != null && user.getEmail().equals(email)) {
                            foundUser = user;
                            Log.d(TAG, "Tìm thấy người dùng phù hợp: " + user.getEmail());
                            break;
                        }
                    }
                } else {
                    Log.d(TAG, "Không tìm thấy người dùng với email: " + email);
                }
                userLiveData.setValue(foundUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Lỗi cơ sở dữ liệu: " + error.getMessage(), error.toException());
                userLiveData.setValue(null);
            }
        });
        return userLiveData;
    }
}
