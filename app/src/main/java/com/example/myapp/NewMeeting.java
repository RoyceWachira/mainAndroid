package com.example.myapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class NewMeeting extends Fragment {

    private EditText meetVenue,meetPurpose;
    private TextView pickDate,pickTime;
    private Button btnSchedule;
    String chamaId,chamaFlow,chamaName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_new_meeting, container, false);

        meetPurpose= view.findViewById(R.id.meetPurpose);
        meetVenue= view.findViewById(R.id.meetVenue);
        btnSchedule=view.findViewById(R.id.btnSchedule);
        pickDate= view.findViewById(R.id.pickDate);
        pickTime= view.findViewById(R.id.pickTime);

        Bundle arguments= getArguments();
        if(arguments != null) {
            chamaId = arguments.getString("chamaId");
            chamaFlow= arguments.getString("chamaFlow");
            chamaName= arguments.getString("chamaName");
        }

        final int[] thour = {12}; // Declare as an array
        final int[] tminute = {0}; // Declare as an array

        pickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        thour[0] = hourOfDay; // Modify the value in the array
                        tminute[0] = minute; // Modify the value in the array

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0, 0, 0, thour[0], tminute[0]);

                        pickTime.setText(DateFormat.getTimeInstance().format(calendar.getTime()));

                    }
                }, thour[0], tminute[0], false);
                timePickerDialog.updateTime(thour[0], tminute[0]);
                timePickerDialog.show();
            }
        });



        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current date
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
                        int selectedMonth = monthOfYear;
                        int selectedDay = dayOfMonth;

                        // Create a Calendar instance and set the selected date
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(selectedYear, selectedMonth, selectedDay);

                        // Format the date using a SimpleDateFormat
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = dateFormat.format(selectedCalendar.getTime());

                        // Set the selected date to your TextView or do something with it
                        pickDate.setText(formattedDate);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });



        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMeeting();
            }
        });
        return view;
    }





    private void createMeeting() {
        final String meetingVenue = meetVenue.getText().toString().trim();
        final String meetingPurpose = meetPurpose.getText().toString().trim();
        final String meetingTime = pickTime.getText().toString().trim();
        final String meetingDate = pickDate.getText().toString().trim();

        // Check if all fields are filled
        if (TextUtils.isEmpty(meetingPurpose) || TextUtils.isEmpty(meetingVenue) ) {
            showToast("Please fill in all the fields", true);
            return;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Schedule");
        builder.setMessage("Are you sure you want to Schedule meeting at" + meetingTime + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userId = null;
                userId = SharedPrefManager.getInstance(getContext()).getUserId();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_CREATE_MEETING + "?user_id=" + userId + "&chama_id=" + chamaId, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");
                            String message = jsonObject.getString("message");

                            if (error) {
                                showToast(message, true);
                            } else {

                                showToast(message,false);
                                sendNotification();
                                // Pass the contribution ID to the next fragment
                                MeetingsFragment meetingsFragment = new MeetingsFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("chamaId",chamaId);
                                bundle.putString("chamaName",chamaName);
                                bundle.putString("chamaFlow",chamaFlow);
                                meetingsFragment.setArguments(bundle);

                                // Replace the current fragment with the next fragment
                                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.chamaFrameLayout, meetingsFragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast("Error Occurred: " + e.getMessage(), true);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = error != null && error.getMessage() != null ? error.getMessage() : "Unknown error";
                        Log.e("Error", "Error occurred", error);
                        showToast("Error occurred: " + errorMessage, true);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("meetingPurpose", meetingPurpose);
                        params.put("meetingVenue", meetingVenue);
                        params.put("meetingDate",meetingDate);
                        params.put("meetingTime",meetingTime);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);
            }
        });
        builder.setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void sendNotification() {
        int userId = Integer.parseInt(SharedPrefManager.getInstance(getContext()).getUserId());
        getName(String.valueOf(userId), new NameCallback() {
            @Override
            public void onNameReceived(String name) {
                String notificationContent = "Member " + name + " has created a meeting";
                String url = Constants.URL_CREATE_MEETING + "?user_id=" + userId + "&chama_id=" + chamaId;

                // Create an instance of NotificationSender
                NotificationSender notificationSender = new NotificationSender();

                // Call the sendNotification method
                notificationSender.sendNotification(getContext(), String.valueOf(userId), notificationContent, url);
            }

            @Override
            public void onError(String errorMessage) {
                // Handle the error
                // Display or log the error message
            }
        });
    }

    interface NameCallback {
        void onNameReceived(String name);
        void onError(String errorMessage);
    }


    private void getName(String userId, final NameCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GET_NAME + "?user_id=" + userId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String name = jsonObject.getString("name");
                    callback.onNameReceived(name);
                } catch (JSONException e) {
                    callback.onError("Error occurred: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error.getMessage());
            }
        });

        Context context = getContext();
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
    }


    private void showToast(String message, boolean isError) {
        LayoutInflater inflater = getLayoutInflater();
        View layout;

        if (isError) {
            layout = inflater.inflate(R.layout.custom_toast_error, getView().findViewById(R.id.toast_message));
        } else {
            layout = inflater.inflate(R.layout.custom_toast_success, getView().findViewById(R.id.toast_message));
        }

        TextView toastMessage = layout.findViewById(R.id.toast_message);
        toastMessage.setText(message);

        Toast toast = new Toast(getContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

}