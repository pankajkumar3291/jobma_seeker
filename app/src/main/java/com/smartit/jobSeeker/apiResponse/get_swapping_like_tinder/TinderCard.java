package com.smartit.jobSeeker.apiResponse.get_swapping_like_tinder;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.MultiTransformation;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.Utils;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeHead;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;
import com.mindorks.placeholderview.annotations.swipe.SwipeView;
import com.smartit.jobSeeker.R;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

///**
// * Created by janisharali on 19/08/16.
// */
//@Layout(R.layout.row_swapping_like_tinder)
//public class TinderCard {
//
//    @View(R.id.circleImageView)
//    private ImageView profileImageView;
//
//    @View(R.id.textView14)
//    private TextView nameAgeTxt;
//
//    @View(R.id.textView16)
//    private TextView locationNameTxt;
//
//    @SwipeView
//    private android.view.View cardView;
//
//    private Profile mProfile;
//    private Context mContext;
//    private SwipePlaceHolderView mSwipeView;
//
//    public TinderCard(Context context, Profile profile, SwipePlaceHolderView swipeView) {
//        mContext = context;
//        mProfile = profile;
//        mSwipeView = swipeView;
//    }
//    @Resolve
//    private void onResolved(){
//        MultiTransformation multi = new MultiTransformation(
//                new BlurTransformation(mContext, 30),
//                new RoundedCornersTransformation(
//                        mContext, Utils.dpToPx(7), 0,
//                        RoundedCornersTransformation.CornerType.TOP));
//
////        Glide.with(mContext).load(mProfile.getImageUrl())
////                .bitmapTransform(multi)
////                .into(profileImageView);
//        nameAgeTxt.setText(mProfile.getName());
////        locationNameTxt.setText(mProfile.getJobmaJobAddress());
//    }
//
//    @SwipeHead
//    private void onSwipeHeadCard(){
////        Glide.with(mContext).load(mProfile.getImageUrl())
////                .bitmapTransform(new RoundedCornersTransformation(
////                        mContext, Utils.dpToPx(7), 0,
////                        RoundedCornersTransformation.CornerType.TOP))
////                .into(profileImageView);
////        cardView.invalidate();
//    }
//
//
//    @SwipeOut
//    private void onSwipedOut(){
//        Log.d("EVENT", "onSwipedOut");
////        mSwipeView.addView(this);
//    }
//
//    @SwipeCancelState
//    private void onSwipeCancelState(){
//        Log.d("EVENT", "onSwipeCancelState");
//    }
//
//    @SwipeIn
//    private void onSwipeIn(){
//        Log.d("EVENT", "onSwipedIn");
//    }
//
//    @SwipeInState
//    private void onSwipeInState(){
//        Log.d("EVENT", "onSwipeInState");
//    }
//
//    @SwipeOutState
//    private void onSwipeOutState(){
//        Log.d("EVENT", "onSwipeOutState");
//    }
//}
