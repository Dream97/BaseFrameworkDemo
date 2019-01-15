package com.rastargame.rick.read.mvp.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rastargame.rick.read.R;
import com.rastargame.rick.read.model.entity.LoopEntity;
import com.rastargame.rick.read.utils.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: Rick
 * Email: zhiyuanfeng@rastar.com
 * Date: 2019/1/10
 */

public class CoverFlowAdapter extends RecyclerView.Adapter<CoverFlowAdapter.MyHolder> {

    private LoopEntity bean;
    private Context context;
    private int type = 0;
    String[] TEXT = {"华农","华工","广州新鲜事"};
    String[] RESURL ={"https://ss3.baidu.com/9fo3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=e7c761355dda81cb51e685cd6264d0a4/4bed2e738bd4b31ccda81d7a8bd6277f9f2ff85f.jpg"
            ,"https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=6b9a910cf0039245beb5e70fb795a4a8/b8014a90f603738d01236be3bf1bb051f919ec82.jpg"
            ,"https://ss0.baidu.com/-Po3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=fc55c37eacec08fa390015a769ef3d4d/b17eca8065380cd79a75c52cad44ad3458828183.jpg"
    };

    public CoverFlowAdapter(Context context,LoopEntity bean) {
        this.context = context;
        this.bean = bean;
    }


    @Override
    public CoverFlowAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.loop_layout,parent,false);
        CoverFlowAdapter.MyHolder myHolder = new CoverFlowAdapter.MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(CoverFlowAdapter.MyHolder holder, final int position) {
        ImageLoader.displayImage(context,RESURL[position],holder.imageView);
        holder.textView.setText(TEXT[position]);
    }



    @Override
    public int getItemCount() {
        return 3;
    }

    class MyHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.arc_icon)
        ImageView imageView;
        @BindView(R.id.arc_text)
        TextView textView;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
//            itemView.setOnClickListener(this);
        }
    }
}

