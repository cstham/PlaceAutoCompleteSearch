package code.cameo.placeautocomplete.Animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.List;

import code.cameo.placeautocomplete.MapsActivity;
import code.cameo.placeautocomplete.Routing;

import static code.cameo.placeautocomplete.Adv_SearchBarFragment.via_exist;
import static code.cameo.placeautocomplete.Colors.manipulateColor;

public class MapAnimator {


    private static MapAnimator mapAnimator;

    public static MapAnimator getInstance(){
        if(mapAnimator == null) mapAnimator = new MapAnimator();
        return mapAnimator;
    }

    public ListMultimap<String, Polyline> polyline_list = LinkedListMultimap.create();   //consists of background and foreground polyline
    public ListMultimap<String, AnimatorSet> animation_list = LinkedListMultimap.create();  //consists of first and second animation

    //static final int GREY = Color.parseColor("#FFA7A6A6");


    final int animationColor = Color.argb(255, 35, 155, 86);
    final int lighterColor = manipulateColor(animationColor, 1.6f);

    public int animationCount = 0;
    public float zIndex = 1f;

    //Routing routing = new Routing(mContext);

    //==============================================================================================
    public void stopAnimateRoute (String polylineName){

        //remove polyline animations
        for (AnimatorSet animatorSet : animation_list.get(polylineName)) {
            if (animatorSet!=null){
                animatorSet.removeAllListeners();
                animatorSet.removeAllListeners();
                animatorSet.end();
                animatorSet.cancel();
            }
        }
        animation_list.removeAll(polylineName);

    }

    public void removeDrawRoute (String polylineName){
        //remove polyline
        for (Polyline polyline : polyline_list.get(polylineName)) {
            if (polyline!=null){
                polyline.remove();
            }
        }

        polyline_list.removeAll(polylineName);

    }

    //==============================================================================================
    public void drawRoute(final GoogleMap googleMap, String polylineName, List<LatLng> bangaloreRoute) {
        PolylineOptions optionsBackground = new PolylineOptions().width(20).color(lighterColor).geodesic(true);
        for (int z = 0; z < bangaloreRoute.size(); z++) {
            LatLng point = bangaloreRoute.get(z);
            optionsBackground.add(point);
        }
        //System.out.println("options BACKground color lol: "+optionsBackground.getColor());
        Polyline backgroundPolyline = googleMap.addPolyline(optionsBackground);
        //
        PolylineOptions optionsForeground = new PolylineOptions().width(20).color(lighterColor).geodesic(true);
        for (int z = 0; z < bangaloreRoute.size(); z++) {
            LatLng point = bangaloreRoute.get(z);
            optionsForeground.add(point);
        }
        //System.out.println("options FOREground color lol: "+optionsForeground.getColor());
        Polyline foregroundPolyline = googleMap.addPolyline(optionsForeground);
        //=========================================================================================
        polyline_list.put(polylineName, backgroundPolyline);
        polyline_list.put(polylineName, foregroundPolyline);
    }
    //==============================================================================================
    public void animateRoute(final int routeNo) {

        animationCount++;

        final Polyline backgroundPolyline = polyline_list.get("polyline" +routeNo).get(0);
        final Polyline foregroundPolyline = polyline_list.get("polyline" +routeNo).get(1);

        if (animationCount >1){
            backgroundPolyline.setZIndex(zIndex);
            foregroundPolyline.setZIndex(zIndex + 0.01f); //to ensure that foreground always has higher zIndex than background
        }else{
            backgroundPolyline.setZIndex(zIndex);
            foregroundPolyline.setZIndex(zIndex + 0.01f);
        }

        zIndex += 0.01f;

        final AnimatorSet firstRunAnimSet = new AnimatorSet();

        final AnimatorSet secondLoopRunAnimSet = new AnimatorSet();

        final ValueAnimator percentageCompletion = ValueAnimator.ofInt(0, 100);
        percentageCompletion.setDuration(1200); //2000
        percentageCompletion.setInterpolator(new LinearInterpolator());
        percentageCompletion.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                List<LatLng> foregroundPoints = backgroundPolyline.getPoints();

                int percentageValue = (int) animation.getAnimatedValue();
                int pointcount = foregroundPoints.size();
                int countTobeRemoved = (int) (pointcount * (percentageValue / 100.0f));
                List<LatLng> subListTobeRemoved = foregroundPoints.subList(0, countTobeRemoved);
                subListTobeRemoved.clear();

                foregroundPolyline.setPoints(foregroundPoints);
            }
        });
        percentageCompletion.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                foregroundPolyline.setColor(lighterColor);
                foregroundPolyline.setPoints(backgroundPolyline.getPoints());
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        //GREY, BLACK
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), lighterColor, animationColor);
        colorAnimation.setInterpolator(new AccelerateInterpolator());
        colorAnimation.setDuration(1200); // milliseconds  1200

        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                foregroundPolyline.setColor((int) animator.getAnimatedValue());
            }

        });
        //==========================================================================================
        firstRunAnimSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                secondLoopRunAnimSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        secondLoopRunAnimSet.playSequentially(colorAnimation,
                percentageCompletion);
        secondLoopRunAnimSet.setStartDelay(200);

        secondLoopRunAnimSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //System.out.println("end of animation lol");
                if (via_exist){

                    stopAnimateRoute("polyline" + (routeNo));

                    if (animationCount>1){
                        animationCount = 0;
                        animateRoute(routeNo - 1);
                    }else{
                        animateRoute(routeNo + 1);
                    }

                }
                else{
                    secondLoopRunAnimSet.start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        firstRunAnimSet.start();


        //==========================================================================================
        animation_list.put ("polyline" +routeNo, firstRunAnimSet);
        animation_list.put ("polyline" +routeNo, secondLoopRunAnimSet);


    }

    /**
     * This will be invoked by the ObjectAnimator multiple times. Mostly every 16ms.
     **/

    /*
    public void setRouteIncreaseForward(LatLng endLatLng) {
        List<LatLng> foregroundPoints = foregroundPolyline.getPoints();
        foregroundPoints.add(endLatLng);
        foregroundPolyline.setPoints(foregroundPoints);
    }
    */

}