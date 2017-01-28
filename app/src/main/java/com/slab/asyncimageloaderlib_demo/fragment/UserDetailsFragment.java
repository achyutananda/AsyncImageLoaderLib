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
//            JSONObject urlsObj = rootObject.getJSONObject("urls");
            JSONObject profileImgObj = userObj.getJSONObject("profile_image");
            JSONObject linksObj = userObj.getJSONObject("links");

            tvName.setText(userObj.getString("name"));
            new Loader(getActivity()).loadImage(ivProfile, profileImgObj.getString("large"));
            String userDetails = linksObj.getString("self") + "\n" + linksObj.getString("photos") +
                    "\n" + linksObj.getString("html") + "\n" + linksObj.getString("likes");
            tvDetails.setText(userDetails + "\nCreated at " + rootObject.getString("created_at"));
            /**
             * loading details
             *
             * "id":"4kQA1aQK8-Y",
             "created_at":"2016-05-29T15:42:02-04:00",
             "width":2448,
             "height":1836,
             "color":"#060607",
             "likes":12,
             "liked_by_user":false,
             */
//            ivWall.setBackgroundColor(Color.parseColor(rootObject.getString("color")));
            //set likes etc


//            //Generating the Color from Palette
//            Palette palette = Palette.from(((BitmapDrawable) ivProfile.getDrawable()).getBitmap()).generate();
//            // Pick one of the swatches
//            Palette.Swatch vibrant = palette.getVibrantSwatch();
//            Palette.Swatch vibrant2 = palette.getLightVibrantSwatch();
//            if (vibrant != null) {
//                // Set the background color of a layout based on the vibrant color
//                ivWall.setBackgroundColor(vibrant.getRgb());
//                // Update the title TextView with the proper text color
//                tvName.setTextColor(vibrant.getTitleTextColor());
//            } else if (vibrant2 != null) {
//                ivWall.setBackgroundColor(vibrant2.getRgb());
//                tvName.setTextColor(vibrant2.getTitleTextColor());
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }


}