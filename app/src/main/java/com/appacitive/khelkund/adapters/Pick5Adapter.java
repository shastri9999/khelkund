package com.appacitive.khelkund.adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.activities.pick5.Pick5HomeActivity;
import com.appacitive.khelkund.infra.BusProvider;
import com.appacitive.khelkund.infra.KhelkundApplication;
import com.appacitive.khelkund.infra.widgets.CircleView;
import com.appacitive.khelkund.model.Match;
import com.appacitive.khelkund.infra.TeamHelper;
import com.appacitive.khelkund.model.Team;
import com.appacitive.khelkund.model.events.MatchSelectedEvent;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sathley on 3/31/2015.
 */
public class Pick5Adapter extends RecyclerView.Adapter<Pick5Adapter.Pick5ViewHolder> {

    private List<Match> mMatches;

    public Pick5Adapter(List<Match> matches)
    {
        this.mMatches = matches;
    }

    private static final DateFormat df = new SimpleDateFormat("dd MMM");
    private static final DateFormat tf  = new SimpleDateFormat("hh:mm a");

    @Override
    public Pick5ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pick5_card, parent, false);
        return new Pick5ViewHolder(itemView);
    }

    private static final Integer[] colors = new Integer[]{
            R.color.DD, R.color.CSK, R.color.RR, R.color.KXIP, R.color.KKR, R.color.RCB, R.color.MI, R.color.SRH
    };

    private Date toUtc(Date date)
    {
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(date); // sets calendar time/date
        cal.add(Calendar.HOUR_OF_DAY, 5);
        cal.add(Calendar.MINUTE, 30);
        return cal.getTime();
    }

    @Override
    public void onBindViewHolder(Pick5ViewHolder holder, int position) {
        final Match match = mMatches.get(position);

        holder.date.setText(df.format((match.getStartDate())));
        holder.time.setText(tf.format(toUtc(match.getStartDate())));

        holder.homeLogo.setTitleText(match.getHomeTeamShortName());
        holder.homeLogo.setFillColor(KhelkundApplication.getAppContext().getResources().getColor(TeamHelper.getTeamColor(match.getHomeTeamShortName())));
        holder.awayLogo.setTitleText(match.getAwayTeamShortName());
        holder.awayLogo.setFillColor(KhelkundApplication.getAppContext().getResources().getColor(TeamHelper.getTeamColor(match.getAwayTeamShortName())));


        holder.card.setCardBackgroundColor(KhelkundApplication.getAppContext().getResources().getColor(colors[position % 8]));
        holder.relativeLayout.setOnClickListener(null);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchSelectedEvent event = new MatchSelectedEvent();
                event.MatchId = match.getId();
                BusProvider.getInstance().post(event);
            }
        });

    }



    @Override
    public int getItemCount() {
        return mMatches.size();
    }

    public static class Pick5ViewHolder extends RecyclerView.ViewHolder
    {

        @InjectView(R.id.iv_pick5_away_logo)
        public CircleView awayLogo;
        @InjectView(R.id.iv_pick5_home_logo)
        public CircleView homeLogo;

        @InjectView(R.id.tv_pick5_play_time)
        public TextView time;
        @InjectView(R.id.tv_pick5_play_date)
        public TextView date;
        @InjectView(R.id.card_view_pick5)
        public CardView card;
        @InjectView(R.id.rl_pick5_match)
        public RelativeLayout relativeLayout;

        public Pick5ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
