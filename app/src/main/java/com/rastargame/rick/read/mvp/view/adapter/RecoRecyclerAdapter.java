package com.rastargame.rick.read.mvp.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rastargame.rick.read.R;
import com.rastargame.rick.read.model.entity.BookEntity;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: Rick
 * Email: zhiyuanfeng@rastar.com
 * Date: 2019/1/14
 */
public class RecoRecyclerAdapter extends RecyclerView.Adapter<RecoRecyclerAdapter.MyHolder> {
    private ArrayList<BookEntity> mBookList = new ArrayList<>();
    private Context mContext;
    public RecoRecyclerAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(ArrayList<BookEntity> bookList) {
        this.mBookList = bookList;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_reco_book, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.mIvHeader.setImageResource(mBookList.get(position).getRes());
        holder.mTvBookName.setText(mBookList.get(position).getBookName());
        holder.mTvBookAuthor.setText(mBookList.get(position).getAuthorName());
    }



    @Override
    public int getItemCount() {
        return mBookList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_reco_book_header)
        ImageView mIvHeader;
        @BindView(R.id.tv_reco_book_name)
        TextView mTvBookName;
        @BindView(R.id.tv_reco_book_autor)
        TextView mTvBookAuthor;
        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
