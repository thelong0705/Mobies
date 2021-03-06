package com.example.hoang.mobies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.models.MovieModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.hoang.mobies.activities.MainActivity.RATED_MOVIE_LIST;

/**
 * Created by Hoang on 6/9/2017.
 */

public class MoviesByCategoriesAdapter extends RecyclerView.Adapter<MoviesByCategoriesAdapter.MoviesByCategoriesViewHolder> {
    private List<MovieModel> movieModelList;
    private Context context;
    private View.OnClickListener onClickListener;

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onClickListener = onItemClickListener;
    }

    public MoviesByCategoriesAdapter(List<MovieModel> movieModelList, Context context) {
        this.movieModelList = movieModelList;
        this.context = context;
    }

    @Override
    public MoviesByCategoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_movie_by_category, parent, false);
        view.setOnClickListener(onClickListener);
        return new MoviesByCategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesByCategoriesViewHolder holder, int position) {
        holder.setData(movieModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return movieModelList.size();
    }

    public class MoviesByCategoriesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_mbc_image)
        ImageView ivMbcImage;
        @BindView(R.id.tv_mbc_name)
        TextView tvMbcName;
        @BindView(R.id.tv_mbc_rating)
        TextView tvMbcRating;
        @BindView(R.id.tv_mbc_vote)
        TextView tvMbcVote;
        View view;

        public MoviesByCategoriesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }

        public void setData(MovieModel movieModel) {
            Picasso.with(context).load("http://image.tmdb.org/t/p/w342/" + movieModel.getPoster_path()).fit().centerCrop().placeholder(R.drawable.no_image_movie_tv_portrait_final).into(ivMbcImage);
            tvMbcName.setText(movieModel.getTitle());
            tvMbcVote.setText(movieModel.getVote_average() + "");
            tvMbcRating.setText(String.format("%,d Ratings",movieModel.getVote_count()));
            for (MovieModel model : RATED_MOVIE_LIST) {
                if (model.getId() == movieModel.getId()) {
                    tvMbcRating.setText(String.format("%,d Ratings",movieModel.getVote_count()+1));
                    break;
                }
            }
            view.setTag(movieModel);
        }
    }
}
