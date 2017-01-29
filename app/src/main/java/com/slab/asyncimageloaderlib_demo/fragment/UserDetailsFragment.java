package com.slab.asyncimageloaderlib_demo.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.slab.asyncimageloaderlib_demo.R;
import com.slab.imageloaderlib.Loader;

import org.json.JSONObject;

/**
 * This fragment contents the user details
 */
public class UserDetailsFragment extends Fragment {
    ImageView ivProfile, ivWall;
    TextView tvName, tvDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_details, viewGroup, false);
        ivProfile = (ImageView) view.findViewById(R.id.ivProfile);
        ivWall = (ImageView) view.findViewById(R.id.ivWall);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvDetails = (TextView) view.findViewById(R.id.tvDetails);
        Bundle bundle = getArguments();
        String ivProfileTransitionName = "";
        String tvNameTransitionName = "";
        String jsonData = "";
        if (bundle != null) {
            ivProfileTransitionName = bundle.getString("TRANS_IMG_NAME");
            tvNameTransitionName = bundle.getString("TRANS_TEXT_NAME");
            jsonData = bundle.getString("USER_DATA");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ivProfile.setTransitionName(ivProfileTransitionName);
            tvName.setTransitionName(tvNameTransitionName);
        }
        try {
            /**
             * In this example, just loading the profile image and Name
             *
             */
            JSONObject rootObject = new JSONObject(jsonData);
            JSONObject userObj = rootObject.getJSONObject("user");
            JSONObject profileImgObj = userObj.getJSONObject("profile_image");
            JSONObject linksObj = userObj.getJSONObject("links");

            tvName.setText(userObj.getString("name"));
            new Loader(getActivity()).loadImage(ivProfile, profileImgObj.getString("large"));
            String userDetails = linksObj.getString("self") + "\n" + linksObj.getString("photos") +
                    "\n" + linksObj.getString("html") + "\n" + linksObj.getString("likes");
            tvDetails.setText(userDetails + "\nCreated at " + rootObject.getString("created_at"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }


}