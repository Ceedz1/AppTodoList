package com.example.apptodolistfinals;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText inputTask;
    private Button btnAddTask, btnLogout;
    private RecyclerView recyclerView;

    private DatabaseReference databaseReference;
    private List<TodoItem> todoList = new ArrayList<>();
    private TodoAdapter todoAdapter;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputTask = findViewById(R.id.input_task);
        btnAddTask = findViewById(R.id.btn_add_task);
        btnLogout = findViewById(R.id.btn_logout); // Add logout button
        recyclerView = findViewById(R.id.recycler_view);

        auth = FirebaseAuth.getInstance();

        // Check if user is authenticated
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            navigateToLogin();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("tasks");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        todoAdapter = new TodoAdapter(todoList, databaseReference);
        recyclerView.setAdapter(todoAdapter);

        btnAddTask.setOnClickListener(v -> addTask());
        btnLogout.setOnClickListener(v -> logout());

        loadTasks();
    }

    private void addTask() {
        String task = inputTask.getText().toString();
        if (!task.isEmpty()) {
            String id = databaseReference.push().getKey();
            TodoItem todoItem = new TodoItem(id, task);
            databaseReference.child(id).setValue(todoItem);
            inputTask.setText("");
        } else {
            Toast.makeText(this, "Task cannot be empty!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadTasks() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                todoList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TodoItem item = dataSnapshot.getValue(TodoItem.class);
                    todoList.add(item);
                }
                todoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to load tasks", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout() {
        auth.signOut();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        navigateToLogin();
    }

    private void navigateToLogin() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
}
