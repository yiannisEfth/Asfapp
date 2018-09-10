package ltd.solutions.software.myt.asfapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

public class ProfileFragment extends Fragment {

    private ImageView nameEdit;
    private ImageView surnameEdit;
    private ImageView heightEdit;
    private ImageView weightEdit;
    private ImageView dateStartedEdit;
    private ImageView idEdit;
    private TextView nameText, surnameText, heightText, weightText, dateStartedText, idText;
    private Button clearAllBtn, viewUserClassesBtn;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersReference = database.getReference("Users");
    private List<String> currentUsers = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, null);
    }


    @Override
    public void onStart() {
        super.onStart();
        sharedPref = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        initialSetup();
        nameSetup();
        surnameSetup();
        heightSetup();
        weightSetup();
        dateStartedSetup();
        idSetup();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Edit Images
        nameEdit = view.findViewById(R.id.profile_name_edit);
        surnameEdit = view.findViewById(R.id.profile_surname_edit);
        heightEdit = view.findViewById(R.id.profile_height_edit);
        weightEdit = view.findViewById(R.id.profile_weight_edit);
        dateStartedEdit = view.findViewById(R.id.profile_started_edit);
        idEdit = view.findViewById(R.id.profile_id_edit);
        //Changed Views
        nameText = view.findViewById(R.id.profile_name_input);
        surnameText = view.findViewById(R.id.profile_surname_input);
        heightText = view.findViewById(R.id.profile_height_input);
        weightText = view.findViewById(R.id.profile_weight_input);
        dateStartedText = view.findViewById(R.id.profile_started_input);
        idText = view.findViewById(R.id.profile_id_input);
        //Buttons
        clearAllBtn = view.findViewById(R.id.profile_button_clear);
        viewUserClassesBtn = view.findViewById(R.id.profile_button_classes);

    }

    public void initialSetup() {
        String name = sharedPref.getString("name", null);
        String surname = sharedPref.getString("surname", null);
        String height = sharedPref.getString("height", null);
        String weight = sharedPref.getString("weight", null);
        String dateStarted = sharedPref.getString("dateStarted", null);
        String id = sharedPref.getString("id", null);
        nameText.setText(name);
        surnameText.setText(surname);
        heightText.setText(height);
        weightText.setText(weight);
        dateStartedText.setText(dateStarted);
        idText.setText(id);
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot users : dataSnapshot.getChildren()) {
                    currentUsers.add(users.getKey());
                }
                buttonSetup();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void nameSetup() {
        nameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Please Enter Your Name");

                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Pattern.matches("[A-z]{0,20}", input.getText().toString())) {
                            nameText.setText(input.getText().toString());
                            editor.putString("name", input.getText().toString());
                            editor.commit();
                        } else {
                            Toast.makeText(getContext(), "Invalid input. Name can only contain letters and no spaces.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    public void surnameSetup() {
        surnameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Please Enter Your Name");

                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Pattern.matches("[A-z]{0,20}", input.getText().toString())) {
                            surnameText.setText(input.getText().toString());
                            editor.putString("surname", input.getText().toString());
                            editor.commit();
                        } else {
                            Toast.makeText(getContext(), "Invalid input. Surname can only contain letters and no spaces.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    public void heightSetup() {
        heightEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Please Enter Your Height in cm");

                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Regex for heights between 80 and 270 cm.
                        if (Pattern.matches("(8[0-9]|9[0-9]|1[0-9]{2}|2[0-6][0-9]|270)", input.getText().toString())) {
                            heightText.setText(input.getText().toString());
                            editor.putString("height", input.getText().toString());
                            editor.commit();
                        } else {
                            Toast.makeText(getContext(), "Invalid Height. Must be between 80 and 270 cm", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    public void weightSetup() {
        weightEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Please Enter Your Weight in kgs");

                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Regex for weights between 30 and 550 kg.
                        if (Pattern.matches("\\b([3-8][0-9]|9[0-9]|[1-4][0-9]{2}|5[0-4][0-9]|550)\\b", input.getText().toString())) {
                            weightText.setText(input.getText().toString());
                            editor.putString("weight", input.getText().toString());
                            editor.commit();
                        } else {
                            Toast.makeText(getContext(), "Invalid Weight. Must be between 30 and 550 kg", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    public void dateStartedSetup() {
        dateStartedEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener mDateSetListener;
                mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = month + "/" + day + "/" + year;
                        if (year > 1950) {
                            dateStartedText.setText(date);
                            editor.putString("dateStarted", date);
                            editor.commit();
                        } else {
                            Toast.makeText(getContext(), "Please enter a more recent year", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


            }
        });
    }

    public void idSetup() {
        idEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Please Enter Your ID");

                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Pattern.matches("\\b([1-8][0-9]{5}|9[0-8][0-9]{4}|99[0-8][0-9]{3}|999[0-8][0-9]{2}|9999[0-8][0-9]|99999[0-9])\\b", input.getText().toString())) {
                            idText.setText(input.getText().toString());
                            editor.putString("id", input.getText().toString());
                            editor.commit();
                        } else {
                            Toast.makeText(getContext(), "Invalid input. ID is a 6-digit number", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    public void buttonSetup() {
        clearAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameText.setText("");
                surnameText.setText("");
                heightText.setText("");
                weightText.setText("");
                dateStartedText.setText("");
                idText.setText("");
                editor.remove("name");
                editor.remove("surname");
                editor.remove("weight");
                editor.remove("height");
                editor.remove("dateStarted");
                editor.remove("id");
                editor.commit();
            }
        });

        viewUserClassesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUsers.contains(sharedPref.getString("id", null))) {
                    Intent intent = new Intent(getContext(), UserClasses.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "User Does Not Exist. Please Review Your ID", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
