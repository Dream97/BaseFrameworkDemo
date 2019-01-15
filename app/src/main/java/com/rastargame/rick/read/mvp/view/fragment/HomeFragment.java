package com.rastargame.rick.read.mvp.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rastargame.rick.read.R;
import com.rastargame.rick.read.base.MvpBaseFragment;
import com.rastargame.rick.read.model.entity.BookEntity;
import com.rastargame.rick.read.model.entity.LoopEntity;
import com.rastargame.rick.read.mvp.contract.HomeContract;
import com.rastargame.rick.read.mvp.presenter.HomePresenter;
import com.rastargame.rick.read.mvp.view.adapter.ClassifyGVAdapter;
import com.rastargame.rick.read.mvp.view.adapter.CoverFlowAdapter;
import com.rastargame.rick.read.mvp.view.adapter.RecoRecyclerAdapter;
import com.rastargame.rick.read.mvp.view.widget.ClassifyGridView;

import java.util.ArrayList;

import butterknife.BindView;
import recycler.coverflow.CoverFlowLayoutManger;
import recycler.coverflow.RecyclerCoverFlow;

/**
 * Author: Rick
 * Email: zhiyuanfeng@rastar.com
 * Date: 2019/1/7
 */
public class HomeFragment extends MvpBaseFragment<HomePresenter> implements HomeContract.View {

    @BindView(R.id.rc_fragment)
    RecyclerCoverFlow recyclerCoverFlow;
    @BindView(R.id.rv_home_reco)
    RecyclerView mRvHomeReco;
    @BindView(R.id.mgv_home_classify)
    ClassifyGridView mClassifyGridView;

    private RecoRecyclerAdapter mRecoRecyclerAdapter;
    private ClassifyGVAdapter mClassifyGVAdapter;
    private int[] res = new int[] {
            R.drawable.image_book_1,
            R.drawable.image_book_2,
            R.drawable.image_book_3,
            R.drawable.image_book_4,
            R.drawable.image_book_5,
            R.drawable.image_book_6,
    };

    private String[] names = new String[] {
            "西游记",
            "红楼梦",
            "三国演义",
            "水浒传",
            "儒林外史",
            "春秋战国"
    };

    private String[] authors = new String[] {
            "吴承恩",
            "曹雪芹",
            "罗贯中",
            "施耐庵",
            "吴敬梓",
            "高兴宇"
    };

    @Override
    protected void setInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public void init() {
        setViewPager();
        setRecoView();
        mClassifyGridView.setNumColumns(2);
        mClassifyGVAdapter = new ClassifyGVAdapter(getContext());
        mClassifyGridView.setAdapter(mClassifyGVAdapter);
    }

    /**
     * 主编推荐
     */
    private void setRecoView() {
        mRecoRecyclerAdapter = new RecoRecyclerAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvHomeReco.setLayoutManager(linearLayoutManager);
        mRvHomeReco.setAdapter(mRecoRecyclerAdapter);
        ArrayList<BookEntity> bookList = new ArrayList<>();
        for(int i = 0; i < res.length; i++) {
            BookEntity bookEntity = new BookEntity();
            bookEntity.setRes(res[i]);
            bookEntity.setBookName(names[i]);
            bookEntity.setAuthorName(authors[i]);
            bookList.add(bookEntity);
        }
        mRecoRecyclerAdapter.setData(bookList);
        mRecoRecyclerAdapter.notifyDataSetChanged();
    }

    /**
     * 设置轮播
     */
    private void setViewPager() {
        recyclerCoverFlow = getActivity().findViewById(R.id.rc_fragment);
        recyclerCoverFlow.setAdapter(new CoverFlowAdapter(getContext(),new LoopEntity()));
//        CoverFlowLayoutManger coverFlowLayoutManger = new CoverFlowLayoutManger()
        recyclerCoverFlow.setOnItemSelectedListener(new CoverFlowLayoutManger.OnSelected() {
            @Override
            public void onItemSelected(int position) {

            }
        });
        recyclerCoverFlow.smoothScrollToPosition(1);
    }
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void showErrorMsg(String msg) {

    }
}
