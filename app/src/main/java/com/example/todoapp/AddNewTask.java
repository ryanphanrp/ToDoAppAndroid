package com.example.todoapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Utils.DatabaseController;
import com.example.todoapp.Utils.DialogCloseListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";

    private EditText newTaskInput;
    private Button saveButton;

    private DatabaseController db;

    public static AddNewTask newInstance() {
        return new AddNewTask();
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setStyle(STYLE_NORMAL, R.style.MyBottomSheetDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.new_task, container, false);
        assert getDialog() != null;
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);

        // Get all element from view XML
        assert getView() != null;   // Catch error when getView() is null
        newTaskInput = getView().findViewById(R.id.new_task_input);
        saveButton = getView().findViewById(R.id.save_button);

        // Handle with database controller
        db = new DatabaseController(getActivity());
        db.openDatabase();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            newTaskInput.setText(task);

            assert task != null;
            if (task.trim().length() > 0) {
                saveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.primary_pink));
            } else {
                saveButton.setEnabled(false);
                saveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.primary_gray));
            }
        }

        // Handle empty tasks before enter text
        if (newTaskInput.getText().toString().isEmpty()) {
            saveButton.setEnabled(false);
            saveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.primary_gray));
        }

        // Handle text input
        newTaskInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().equals("")) {
                    Toast.makeText(getContext(), "Please enter something", Toast.LENGTH_SHORT).show();
                    saveButton.setEnabled(false);
                    saveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.primary_gray));
                } else {
                    saveButton.setEnabled(true);
                    saveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.primary_pink));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Handle Save Input
        boolean finalIsUpdate = isUpdate;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = newTaskInput.getText().toString();

                if (finalIsUpdate) {
                    db.updateTask(bundle.getInt("id"), text);
                } else {
                    ToDoModel task = new ToDoModel();
                    task.setTask(text.trim());
                    task.setStatus(false);
                    db.insertTask(task);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialogInterface) {
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener) {
            ((DialogCloseListener) activity).handleDialogClose(dialogInterface);
        }
    }
}
