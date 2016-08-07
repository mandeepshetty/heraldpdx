package droidia.com.heraldpdx.arrivalchecker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import droidia.com.heraldpdx.Utils;

class ArrivalsListAnimator extends DefaultItemAnimator{

    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    private int lastAddAnimatedItem = -2;

    @Override
    public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
        return true;
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder viewHolder) {

        if (viewHolder.getLayoutPosition() > lastAddAnimatedItem) {
            lastAddAnimatedItem++;
            runEnterAnimation((ArrivalsRecyclerViewAdapter.ArrivalViewHolder) viewHolder);
            return false;
        }
        dispatchAddFinished(viewHolder);
        return false;
    }

    private void runEnterAnimation(final ArrivalsRecyclerViewAdapter.ArrivalViewHolder holder) {
        final int screenHeight = Utils.getScreenHeight(holder.itemView.getContext());
        holder.itemView.setTranslationY(screenHeight);
        holder.itemView.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(2100)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dispatchAddFinished(holder);
                    }
                })
                .start();
    }
}
