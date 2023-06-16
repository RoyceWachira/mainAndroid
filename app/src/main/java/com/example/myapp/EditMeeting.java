package com.example.myapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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


public class EditMeeting extends Fragment {

    private EditText meetVenue,meetPurpose;
    private TextView pickDate,pickTime;
    private Button btnEdit;
    String meetingId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_edit_meeting, container, false);

        meetPurpose= view.findViewById(R.id.meetPurpose);
        meetVenue= view.findViewById(R.id.meetVenue);
        btnEdit=view.findViewById(R.id.btnEditMeeting);
        pickDate= view.findViewById(R.id.pickDate);
        pickTime= view.findViewById(R.id.pickTime);

        Bundle arguments= getArguments();
        if(arguments != null) {
            meetingId = arguments.getString("meetingId");
        }

        fetchMeetingData();

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



        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String meetingDate= pickDate.getText().toString().trim();
                String meetingTime= pickTime.getText().toString().trim();
                String meetingVenue= meetVenue.getText().toString().trim();
                String meetingPurpose= meetPurpose.getText().toString().trim();
                editMeeting(meetingDate,meetingTime,meetingVenue,meetingPurpose);
            }
        });
        return view;
    }

    private void fetchMeetingData() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GET_MEETING+ "?meeting_id=" + meetingId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RES",response);
                try {
                    // Parse the JSON response
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");

                    if (error==false) {
                        JSONObject meetingObject = jsonObject.getJSONObject("meeting");

                        String meetingVenue = meetingObject.getString("meeting_venue");
                        meetVenue.setText(meetingVenue);
                        String meetingDate = meetingObject.getString("meeting_date");
                        pickDate.setText(meetingDate);
                        String meetingPurpose= meetingObject.getString("meeting_purpose");
                        meetPurpose.setText(meetingPurpose);
                        String meetingTime= meetingObject.getString("meeting_time");
                        pickTime.setText(meetingTime);

                    } else {
                        // User data retrieval failed
                        showToast("Error", true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("Error occurred: " + e.getMessage(), true);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToast("Error occurred: " + error.getMessage(), true);
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void editMeeting( String meetingDate, String meetingTime, String meetingVenue, String meetingPurpose) {

        if (TextUtils.isEmpty(meetingPurpose) || TextUtils.isEmpty(meetingDate) || TextUtils.isEmpty(meetingVenue) || TextUtils.isEmpty(meetingTime)) {
            showToast("Please fill in all the fields",true);
            return;
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_EDIT_MEETING+ "?meeting_id=" + meetingId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RES",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    String message = jsonObject.getString("message");
                    if (error==false) {
                        showToast(message, false);
                    } else {
                        showToast(message, true); // Show error message with red background color
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("Error occurred: " + e.getMessage(), true); // Show error message with red background color
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToast("Error occurred: " + error.getMessage(), true); // Show error message with red background color
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Add the request parameters
                Map<String, String> params = new HashMap<>();
                params.put("meetingDate", meetingDate);
                params.put("meetingTime", meetingTime);
                params.put("meetingVenue", meetingVenue);
                params.put("meetingPurpose", meetingPurpose);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
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