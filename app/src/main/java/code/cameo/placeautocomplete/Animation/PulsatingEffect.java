package code.cameo.placeautocomplete.Animation;


import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class PulsatingEffect {

    private static PulsatingEffect pulsatingEffect;

    public static PulsatingEffect getInstance(){
        if(pulsatingEffect == null) pulsatingEffect = new PulsatingEffect();
        return pulsatingEffect;
    }

    //private Circle lastUserCircle;
    //private ValueAnimator lastPulseAnimator;

    //public ListMultimap<String, Circle> circle_ListMap = LinkedListMultimap.create();
    //public ListMultimap<String, ValueAnimator> pulseAnimation_ListMap = LinkedListMultimap.create();

    //public List<Circle> circle_list = new ArrayList<Circle>();
    //public List<ValueAnimator> pulseAnimation_list = new ArrayList<ValueAnimator>();

    public LinkedHashMap<String,Circle> circleHashMap = new LinkedHashMap<>();
    public LinkedHashMap<String,ValueAnimator> pulseHashMap = new LinkedHashMap<>();

    public void addPulsatingEffect(final GoogleMap googleMap, final String transitName, final LatLng position){
        long pulseDuration = 2000;

        if(pulseHashMap.get(transitName) != null){
            pulseHashMap.get(transitName).cancel();
        }
        if(circleHashMap.get(transitName) != null)
            circleHashMap.get(transitName).setCenter(position);
        ValueAnimator lastPulseAnimator = valueAnimate(300, pulseDuration, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(pulseHashMap.get(transitName) != null)
                    circleHashMap.get(transitName).setRadius((Float) animation.getAnimatedValue());
                else {
                    Circle lastUserCircle = googleMap.addCircle(new CircleOptions()
                            .center(position)
                            .radius((Float) animation.getAnimatedValue())
                            .strokeColor(Color.parseColor("#239b56"))
                            .fillColor(Color.parseColor("#82e0aa")));

                    circleHashMap.put(transitName, lastUserCircle);

                }
            }
        });
        
        //pulseAnimation_ListMap.put(transitName, lastPulseAnimator);
        pulseHashMap.put(transitName, lastPulseAnimator);

    }

    protected ValueAnimator valueAnimate(float accuracy, long duration, ValueAnimator.AnimatorUpdateListener updateListener){
        ValueAnimator va = ValueAnimator.ofFloat(0,accuracy);
        va.setDuration(duration);
        va.addUpdateListener(updateListener);
        va.setRepeatCount(ValueAnimator.INFINITE);
        va.setRepeatMode(ValueAnimator.RESTART);

        va.start();
        return va;
    }

    public void removePulsatingEffect(String transitName)
    {
        if(pulseHashMap.get(transitName) != null){
            pulseHashMap.get(transitName).cancel();
        }

        if(circleHashMap.get(transitName) != null){
            circleHashMap.get(transitName).remove();
        }

        //pulseHashMap.clear();
        //circleHashMap.clear();

    }



}
