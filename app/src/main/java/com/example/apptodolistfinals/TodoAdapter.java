package com.example.apptodolistfinals;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {
    private List<TodoItem> todoList;
    private DatabaseReference databaseReference;

    public TodoAdapter(List<TodoItem> todoList, DatabaseReference databaseReference) {
        this.todoList = todoList;
        this.databaseReference = databaseReference;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        TodoItem item = todoList.get(position);
        holder.todoText.setText(item.getTask());

        holder.editButton.setOnClickListener(v -> openEditDialog(holder.itemView.getContext(), item));
        holder.deleteButton.setOnClickListener(v -> databaseReference.child(item.getId()).removeValue());
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    static class TodoViewHolder extends RecyclerView.ViewHolder {
        TextView todoText;
        Button editButton, deleteButton;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            todoText = itemView.findViewById(R.id.todo_item);
            editButton = itemView.findViewById(R.id.btn_edit);
            deleteButton = itemView.findViewById(R.id.btn_delete);
        }
    }

    private void openEditDialog(Context context, TodoItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Task");

        final EditText input = new EditText(context);
        input.setText(item.getTask());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newTask = input.getText().toString();
            item.setTask(newTask);
            databaseReference.child(item.getId()).setValue(item);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}



