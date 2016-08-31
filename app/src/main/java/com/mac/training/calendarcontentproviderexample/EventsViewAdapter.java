package com.mac.training.calendarcontentproviderexample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;
import java.text.DateFormat;

/**
 * Created by User on 8/30/2016.
 */
public class EventsViewAdapter extends RecyclerView.Adapter<EventsViewAdapter.ViewHolder> {

    private final List<EventInfo> items;
    private final EventInfoListFragment.OnFragmentInteractionListener listener;
    private final DateFormat df;

    public EventsViewAdapter(
            List<EventInfo> items,
            EventInfoListFragment.OnFragmentInteractionListener listener) {
        this.items = items;
        this.listener = listener;
        this.df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final EventInfo eventInfo= items.get(position);
        holder.setEventInfo(eventInfo);
        holder.setEventTitleView(eventInfo.getTitle());
        holder.setEventDtStartView(eventInfo.getDtStart());
        holder.setEventDtEndView(eventInfo.getDtEnd());
        holder.setEventDescriptionView(eventInfo.getDescription());
        holder.setEventLocationView(eventInfo.getLocation());

        holder.getView().setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onFragmentInteraction(eventInfo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View view;
        private EventInfo eventInfo;
        private final TextView eventTitleView;
        private final TextView eventDtStartView;
        private final TextView eventDtEndView;
        private final TextView eventDescriptionView;
        private final TextView eventLocationView;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.eventTitleView =
                    ((TextView) view.findViewById(R.id.event_title_view));
            this.eventDtStartView =
                    ((TextView) view.findViewById(R.id.event_start_date_view));
            this.eventDtEndView =
                    ((TextView) view.findViewById(R.id.event_end_date_view));
            this.eventDescriptionView =
                    ((TextView) view.findViewById(R.id.event_description_view));
            this.eventLocationView =
                    ((TextView) view.findViewById(R.id.event_location_view));
        }

        public EventInfo getEventInfo() {
            return eventInfo;
        }

        public void setEventInfo(EventInfo eventInfo) {
            this.eventInfo = eventInfo;
        }

        public void setEventTitleView(String eventTitle) {
            if (!eventTitle.isEmpty()) {
            }
            eventTitleView.setText(eventTitle);
        }

        public void setEventDtStartView(String eventDtStart) {
           if (!eventDtStart.isEmpty()) {
               Date date = new Date(Long.valueOf(eventDtStart));
               eventDtStartView.setText(df.format(date).toString());
           }
        }

        public void setEventDtEndView(String eventDtEnd) {
            if (!eventDtEnd.isEmpty()) {
                Date date = new Date(Long.valueOf(eventDtEnd));
                eventDtStartView.setText(df.format(date).toString());
            }
        }

        public void setEventDescriptionView(String eventDescription) {
            if (!eventDescription.isEmpty()) {
                eventDescriptionView.setText(eventDescription);
            }
        }

        public void setEventLocationView(String eventLocation) {
            if (!eventLocation.isEmpty()) {
                eventLocationView.setText(eventLocation);
            }
        }

        public View getView() {
            return view;
        }
    }
}
