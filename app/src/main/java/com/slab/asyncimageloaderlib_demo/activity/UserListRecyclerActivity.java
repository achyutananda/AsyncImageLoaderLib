package com.slab.asyncimageloaderlib_demo.activity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.slab.asyncimageloaderlib_demo.R;
import com.slab.asyncimageloaderlib_demo.fragment.RecyclerFragment;

/**
 * RecyclerView loads 10 items at a time, when it reach the end of the list will load next item. Animation added while scrolling up or down in RecyclerView.
 * - Material UI elements added in Sample application(i.e. Ripple effect, Material buttons, Animations, fragment shared element transition etc ).
 * - Pull to refresh functionality is added with recycler view, which will refresh the data set.
 */
public class UserListRecyclerActivity extends AppCompatActivity {
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_recycler);

        RecyclerFragment startFragment = new RecyclerFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.ll_frag_container, startFragment)
                .commit();
    }

    @SuppressLint("NewApi")
    public void launchMenu(View view) {
        final LinearLayout linearView = (LinearLayout) findViewById(R.id.linearView);
        final LinearLayout layoutButtons = (LinearLayout) findViewById(R.id.layoutButtons);
        final RelativeLayout rlDetails = (RelativeLayout) findViewById(R.id.rlDetails);
        final ImageButton btnMenuOpen = (ImageButton) findViewById(R.id.btnMenuOpen);
        int x = rlDetails.getRight()/2;
        int y = rlDetails.getTop();
        int hypotenuse = (int) Math.hypot(rlDetails.getWidth(), rlDetails.getHeight());
        final Animation alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_anim);
        if (flag) {

            btnMenuOpen.setBackgroundResource(R.drawable.rounded_cancel_button);
            btnMenuOpen.setImageResource(R.drawable.ic_expand_less);

            FrameLayout.LayoutParams parameters = (FrameLayout.LayoutParams)
                    linearView.getLayoutParams();
            parameters.height = rlDetails.getHeight();
            linearView.setLayoutParams(parameters);

            Animator anim = ViewAnimationUtils.createCircularReveal(linearView, x, y, 0, hypotenuse);
            anim.setDuration(500);

            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    layoutButtons.setVisibility(View.VISIBLE);
                    layoutButtons.startAnimation(alphaAnimation);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            linearView.setVisibility(View.VISIBLE);
            anim.start();

            flag = false;
        } else {

            btnMenuOpen.setBackgroundResource(R.drawable.rounded_button);
            btnMenuOpen.setImageResource(R.drawable.ic_expand_more);

            Animator anim = ViewAnimationUtils.createCircularReveal(linearView, x, y, hypotenuse, 0);
            anim.setDuration(500);

            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    linearView.setVisibility(View.GONE);
                    layoutButtons.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            anim.start();
            flag = true;
        }
    }

}
