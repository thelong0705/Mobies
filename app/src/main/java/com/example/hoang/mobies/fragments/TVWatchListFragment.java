package com.example.hoang.mobies.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.adapters.WatchListAdapter;
import com.example.hoang.mobies.databases.RealmHandle;
import com.example.hoang.mobies.managers.ScreenManager;
import com.example.hoang.mobies.models.TVModel;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TVWatchListFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.rv_watch_list)
    RecyclerView rvWatchList;
    private WatchListAdapter watchListAdapter;

    public TVWatchListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tvwatch_list, container, false);
        ButterKnife.bind(this, view);
        setupUI();
        return view;
    }

    private void setupUI() {
        watchListAdapter = new WatchListAdapter(getContext(), null, RealmHandle.getInstance().getListTVWatchList());
        rvWatchList.setAdapter(watchListAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvWatchList.setLayoutManager(manager);
        watchListAdapter.setOnClickListener(this);
//        GravitySnapHelper snapHelper = new GravitySnapHelper(Gravity.TOP);
//        snapHelper.attachToRecyclerView(rvWatchList);
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() instanceof TVModel) {
            TVModel tvModel = (TVModel) v.getTag();
            TVShowDetailFragment tvShowDetailFragment = new TVShowDetailFragment();
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("TVDetail", tvModel);
//            tvShowDetailFragment.setArguments(bundle);
            EventBus.getDefault().postSticky(tvModel);
            ScreenManager.openFragment(getActivity().getSupportFragmentManager(), tvShowDetailFragment, R.id.drawer_layout, true, false);
        }
    }
}
