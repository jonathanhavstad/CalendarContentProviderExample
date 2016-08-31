package com.mac.training.calendarcontentproviderexample;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventInfoListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventInfoListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventInfoListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private List<EventInfo> eventInfoList;

    private SwipeRefreshLayout refreshView;

    private RecyclerView recyclerView;

    private AsyncTask<Integer, Integer, Integer> task;

    public EventInfoListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EventInfoListFragment.
     */
    public static EventInfoListFragment newInstance() {
        EventInfoListFragment fragment = new EventInfoListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        eventInfoList = new ArrayList<EventInfo>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.fragment_layout, container, false);
        View view = parentView.findViewById(R.id.list);
        if (view instanceof RecyclerView) {
            recyclerView = (RecyclerView) view;
            recyclerView.setAdapter(new EventsViewAdapter(eventInfoList, mListener));

            if (parentView instanceof SwipeRefreshLayout) {
                refreshView = (SwipeRefreshLayout) parentView;
                refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        setIsRefreshing(true);
                    }
                });
            }
        }
        return parentView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        setIsRefreshing(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopThread();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(EventInfo eventInfo);
    }

    private void getUserContacts() {
        new ContactListAsyncTask().execute();
    }

    public class ContactListAsyncTask extends AsyncTask<Integer, Integer, Integer> {
        private Cursor cursor;

        @Override
        protected Integer doInBackground(Integer... integers) {
            Log.i(getClass().getName(), "Retrieving calendar events");
            Uri calendarUri = CalendarContract.Events.CONTENT_URI;
            String[] projection = {
                    CalendarContract.Events.CALENDAR_ID,
                    CalendarContract.Events.TITLE,
                    CalendarContract.Events.DTSTART,
                    CalendarContract.Events.DTEND,
                    CalendarContract.Events.DESCRIPTION,
                    CalendarContract.Events.EVENT_LOCATION
            };
            try {
                Log.d(getClass().getName(), "Making call to Content Provider for Calendar Events");
                cursor =
                        getContext().
                                getContentResolver().
                                    query(calendarUri, projection, null, null, null);
                Log.d(
                        getClass().getName(),
                        "Number of calendar events retrieved: " + cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        EventInfo eventInfo = new EventInfo();
                        eventInfo.setId(cursor.getInt(
                                cursor.getColumnIndex(CalendarContract.Events.CALENDAR_ID)));
                        eventInfo.setTitle(cursor.getString(
                                cursor.getColumnIndex(CalendarContract.Events.TITLE)));
                        eventInfo.setDtStart(cursor.getString(
                                cursor.getColumnIndex(CalendarContract.Events.DTSTART)));
                        eventInfo.setDtEnd(cursor.getString(
                                cursor.getColumnIndex(CalendarContract.Events.DTEND)));
                        eventInfo.setDescription(cursor.getString(
                                cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION)));
                        eventInfo.setLocation(cursor.getString(
                                cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION)));
                        Log.d(getClass().getName(), "Event Info: " + eventInfo.toString());
                        eventInfoList.add(eventInfo);
                    } while (cursor.moveToNext());
                    cursor.close();
                }

            } catch (SecurityException e) {
                Log.e(getClass().getName(), e.getMessage());
            }

            return eventInfoList.size();
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            recyclerView.getAdapter().notifyDataSetChanged();
            setIsRefreshing(false);
        }
    }

    private void stopThread() {
        Log.d(getClass().getName(), "Stopping thread");
        if (task != null) {
            try {
                task.cancel(true);
                task = null;
                Log.d(getClass().getName(), "Thread cancelled");
            } catch (IllegalStateException e) {
                Log.e(getClass().getName(), "Error stopping background thread!");
            }
        }
    }

    private void startThread() {
        Log.d(getClass().getName(), "Starting thread");
        if (task == null) {
            task = new ContactListAsyncTask();
        }

        try {
            task.execute();
        } catch (IllegalStateException e) {
            Log.e(getClass().getName(),
                    "Error starting background thread to retrieve event data!  " + e.getMessage());
            stopThread();
            startThread();
        }
    }

    private void setIsRefreshing(boolean isRefreshing) {
        if (isRefreshing) {
            Log.d(getClass().getName(), "Refreshing data");
            eventInfoList.clear();
            recyclerView.setAlpha(0.5f);
            refreshView.setRefreshing(true);
            startThread();
        } else {
            Log.d(getClass().getName(), "Stopping data refresh");
            refreshView.setRefreshing(false);
            recyclerView.setAlpha(1.0f);
        }
    }
}
