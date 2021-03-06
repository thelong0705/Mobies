package com.example.hoang.mobies.fragments;


import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.activities.MainActivity;
import com.example.hoang.mobies.adapters.PopularCelebAdapter;
import com.example.hoang.mobies.managers.ScreenManager;
import com.example.hoang.mobies.models.PeopleModel;
import com.example.hoang.mobies.network.RetrofitFactory;
import com.example.hoang.mobies.network.get_people.GetPopularPeopleService;
import com.example.hoang.mobies.network.get_people.MainPeopleObject;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.hoang.mobies.network.RetrofitFactory.API_KEY;
import static com.example.hoang.mobies.network.RetrofitFactory.DEFAULT_PAGE;
import static com.example.hoang.mobies.network.RetrofitFactory.LANGUAGE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CelebFragment extends Fragment implements View.OnClickListener{
    @BindView(R.id.rv_popular_celeb_content)
    RecyclerView rvPopularCeleb;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.tv_no_connection)
    TextView tvNoConnection;
    private List<PeopleModel> popularList;
    private PopularCelebAdapter popularCelebAdapter;
    private boolean loading = true;
    private int pastVisiblesItems;
    private int visibleItemCount;
    private int totalItemCount;
    private int loadTimes = 0;
    private Snackbar snackbar;
    public CelebFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_celeb, container, false);
        loadData();
        setupUI(view);
        return view;
    }

    private void setupUI(View view) {
        ButterKnife.bind(this, view);
        popularCelebAdapter = new PopularCelebAdapter(getContext(), popularList);
        popularCelebAdapter.setOnItemClickListener(this);
        rvPopularCeleb.setAdapter(popularCelebAdapter);
        final GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        rvPopularCeleb.setLayoutManager(manager);
        SnapHelper snapHelper = new GravitySnapHelper(Gravity.TOP);
        snapHelper.attachToRecyclerView(rvPopularCeleb);

        rvPopularCeleb.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = manager.getChildCount();
                    totalItemCount = manager.getItemCount();
                    pastVisiblesItems = manager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            loadTimes ++;
                            loadPopularPeople();
                        }
                    }
                }
            }
        });
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.popular_celeb);
        MainActivity.navigationView.setCheckedItem(R.id.nav_celeb);
    }

    private void loadData() {
        popularList = new ArrayList<>();
        loadPopularPeople();

    }

    private void loadPopularPeople() {
        GetPopularPeopleService getPopularPeopleService = RetrofitFactory.createService(GetPopularPeopleService.class);
        getPopularPeopleService.getPopularPeople(API_KEY, LANGUAGE, DEFAULT_PAGE + loadTimes).enqueue(new Callback<MainPeopleObject>() {
            @Override
            public void onResponse(Call<MainPeopleObject> call, Response<MainPeopleObject> response) {
                if (snackbar != null) snackbar.dismiss();
                for (PeopleModel peopleModel : response.body().getResults()) {
                    popularList.add(peopleModel);
                }
                loading = true;
                popularCelebAdapter.notifyDataSetChanged();
                pbLoading.setVisibility(View.GONE);
                rvPopularCeleb.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<MainPeopleObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
                pbLoading.setVisibility(View.GONE);
                tvNoConnection.setVisibility(View.VISIBLE);
                if (snackbar != null) snackbar.dismiss();
                FrameLayout flContainer = (FrameLayout) getActivity().findViewById(R.id.fl_container);
                snackbar = Snackbar.make(flContainer, "No connection", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(CelebFragment.this).attach(CelebFragment.this).commit();
                    }
                });
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorStatusBar));
                snackbar.show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() instanceof PeopleModel) {
            PeopleModel peopleModel = (PeopleModel) v.getTag();
            CelebDetailFragment celebDetailFragment = new CelebDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("CelebDetail", peopleModel);
            celebDetailFragment.setArguments(bundle);
            ScreenManager.openFragment(getFragmentManager(), celebDetailFragment, R.id.drawer_layout, true, false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (snackbar != null) snackbar.dismiss();
    }
}
