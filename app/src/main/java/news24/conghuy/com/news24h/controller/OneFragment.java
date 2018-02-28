package news24.conghuy.com.news24h.controller;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import news24.conghuy.com.news24h.common.HttpRequest;
import news24.conghuy.com.news24h.R;
import news24.conghuy.com.news24h.common.interfaces.XmlDataCallBack;
import news24.conghuy.com.news24h.model.XmlDto;

/**
 * Created by huy on 19/07/2016.
 */
public class OneFragment extends Fragment {
    private String TAG = "OneFragment";
//    private ShimmerFrameLayout mShimmerViewContainer;
    private MoviesAdapter mAdapter;
    private RecyclerView recyclerView;
    private String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private String _URL = "";
    private TextView tvNoData;
    private List<XmlDto> xmlDtoList;
    private ArrayList<String> listAdv;
    @SuppressLint("ValidFragment")
    public OneFragment(String str,ArrayList<String> listAdv) {
        super();
        this._URL = str;
        this.listAdv=listAdv;
    }

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        _URL = getArguments().getString(Apis.EXTRA_MESSAGE);
    }

    private void showLoading(){
//        mShimmerViewContainer.setVisibility(View.VISIBLE);
//        mShimmerViewContainer.startShimmerAnimation();

    }
    private void hideLoading(){
//        mShimmerViewContainer.setVisibility(View.GONE);
//        mShimmerViewContainer.stopShimmerAnimation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_one, container, false);

        Log.d(TAG, "_URL:" + _URL);
        xmlDtoList = new ArrayList<>();
        tvNoData = (TextView) v.findViewById(R.id.tvNoData);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mAdapter = new MoviesAdapter(getActivity(), xmlDtoList,listAdv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

//        mShimmerViewContainer = (ShimmerFrameLayout) v.findViewById(R.id.shimmer_view_container);

        showLoading();

        new HttpRequest(getActivity()).getData(_URL, new XmlDataCallBack() {
            @Override
            public void onSuccess(List<XmlDto> list) {
                hideLoading();
                Log.d(TAG, "onSuccess:" + list.size());
                mAdapter.update(list);
            }

            @Override
            public void onFail() {
                Log.d(TAG, "onFail");
                hideLoading();
                tvNoData.setVisibility(View.VISIBLE);
            }
        });
        return v;
    }
}
