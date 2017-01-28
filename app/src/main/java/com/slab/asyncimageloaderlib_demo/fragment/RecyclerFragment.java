package com.slab.asyncimageloaderlib_demo.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.slab.asyncimageloaderlib_demo.R;
import com.slab.asyncimageloaderlib_demo.adapter.ContentRecyclerAdapter;
import com.slab.asyncimageloaderlib_demo.listener.RecyclerItemClickListener;
import com.slab.imageloaderlib.LoadStringFromURL;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecyclerFragment extends Fragment {

    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private List<JSONObject> contentList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout pullToRefresh;
    private ContentRecyclerAdapter mAdapter;
    private boolean loading = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recycler, viewGroup, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);
        pullToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh);
        refreshDataSet();
        return view;
    }
    private void refreshDataSet(){
        contentList.clear();
        LoadStringFromURL loadStringFromURL = new LoadStringFromURL() {
            @Override
            public void onResponseReceived(Object result) {
                if (result != null) {
                    try {
                        JSONArray jsonArray = new JSONArray((String) result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            contentList.add(jsonArray.getJSONObject(i));
                        }
                        /**
                         * data is loaded to contentList, next to load it in RecyclerView :see initRecyclerView()
                         */
                        initRecyclerView();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                pullToRefresh.setRefreshing(false);

            }
        };
        pullToRefresh.setRefreshing(true);

        loadStringFromURL.execute(getString(R.string.data));
    }
    private void initRecyclerView() {

        mAdapter = new ContentRecyclerAdapter(getActivity(), contentList, mRecyclerView);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(1000);
        animator.setRemoveDuration(1000);
        mRecyclerView.setItemAnimator(animator);
        mRecyclerView.setAdapter(mAdapter);

        //load more after it reach to last position
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            Log.v("Postion", "it reached last position, next set of data will be added to the list !");

                            /**
                             * For your case Access next page i.e. fetch new data
                             * in this example I wrote the  below code  because of the following
                             * contentList is added to contentList to increase the List Size so , we can load next set of 10 or 20 item to list
                             * don't forget to "loading=true" because it will enable the next set of data to be inserted.
                             */


                            for(int i=0;i<10;i++){
                                //adding duplicate data for demo
                                contentList.add(contentList.get(i));
                            }
                            mAdapter.notifyDataSetChanged();
                            loading = true;
                        }
                    }
                }
            }
        });
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
//                        Toast.makeText(getActivity(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                        loadSecondUserFragment(view, position);
                    }
                })
        );


        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                refreshDataSet();
            }
        });

    }

    private void loadSecondUserFragment(View view, int position) {
        String ivProfileTransitionName = "";
        String tvNameTransitionName = "";

        ImageView ivProfile = (ImageView) view.findViewById(R.id.ivProfile);
        TextView tvName = (TextView) view.findViewById(R.id.tvName);

        UserDetailsFragment userDetailsFragment = new UserDetailsFragment();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementReturnTransition(TransitionInflater.from(
                    getActivity()).inflateTransition(R.transition.change_image_transition));
            setExitTransition(TransitionInflater.from(
                    getActivity()).inflateTransition(android.R.transition.fade));

            userDetailsFragment.setSharedElementEnterTransition(TransitionInflater.from(
                    getActivity()).inflateTransition(R.transition.change_image_transition));
            userDetailsFragment.setEnterTransition(TransitionInflater.from(
                    getActivity()).inflateTransition(android.R.transition.fade));

            ivProfileTransitionName = ivProfile.getTransitionName();
            tvNameTransitionName = tvName.getTransitionName();
        }

        Bundle bundle = new Bundle();
        bundle.putString("TRANS_IMG_NAME", ivProfileTransitionName);
        bundle.putString("TRANS_TEXT_NAME", tvNameTransitionName);
        bundle.putString("USER_DATA", contentList.get(position).toString());
        userDetailsFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.ll_frag_container, userDetailsFragment)
                .addToBackStack("UserDetails")
                .addSharedElement(ivProfile, ivProfileTransitionName)
                .addSharedElement(tvName, tvNameTransitionName)
                .commit();
    }
}