package com.example.animatepepper;

import android.os.Bundle;

import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.object.actuation.Animate;
import com.aldebaran.qi.sdk.object.actuation.Animation;

public class MainActivity extends RobotActivity implements RobotLifecycleCallbacks {

    private Animate hello;
    private Animate saxophone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QiSDK.register(this, this);
    }

    @Override
    protected void onDestroy() {
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        // The robot focus is gained.

        Animation helloAnimation = AnimationBuilder.with(qiContext)
                .withResources(R.raw.hello_a001)
                .build();
        Animation saxophoneAnimation = AnimationBuilder.with(qiContext)
                .withResources(R.raw.saxophone_a001)
                .build();

        hello = AnimateBuilder.with(qiContext)
                .withAnimation(helloAnimation)
                .build();

        Future<Void> animateHello = hello.async().run();

        Future<Void> animate = animateHello.thenCompose(first -> {
            saxophone = AnimateBuilder.with(qiContext)
                    .withAnimation(saxophoneAnimation)
                    .build();

            Future<Void> animateSaxophone = saxophone.async().run();
            return animateSaxophone;
        });
    }

    @Override
    public void onRobotFocusLost() {
        // The robot focus is lost.

        if (hello != null) {
            hello.removeAllOnStartedListeners();
        }

        if (saxophone != null) {
            saxophone.removeAllOnStartedListeners();
        }
    }

    @Override
    public void onRobotFocusRefused(String reason) {
        // The robot focus is refused.
    }
}

