package com.flyjingfish.openimagefulllib;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.flyjingfish.openimagefulllib.databinding.FragmentVideoBinding;
import com.flyjingfish.openimagelib.BaseImageFragment;
import com.flyjingfish.openimagelib.photoview.PhotoView;
import com.flyjingfish.openimagelib.widget.LoadingView;

public class VideoPlayerFragment extends BaseImageFragment<LoadingView> {

    protected FragmentVideoBinding binding;
    protected String playerKey;
    protected boolean isPlayed;
    protected boolean isLoadImageFinish;

    @Override
    protected PhotoView getSmallCoverImageView() {
        return binding.videoPlayer.getSmallCoverImageView();
    }

    @Override
    protected PhotoView getPhotoView() {
        return binding.videoPlayer.getCoverImageView();
    }

    @Override
    protected LoadingView getLoadingView() {
        return (LoadingView) binding.videoPlayer.getLoadingView();
    }

    @Override
    protected void hideLoading(LoadingView pbLoading) {
        super.hideLoading(pbLoading);
        binding.videoPlayer.getStartButton().setVisibility(View.VISIBLE);
    }

    @Override
    protected void showLoading(LoadingView pbLoading) {
        super.showLoading(pbLoading);
        binding.videoPlayer.getStartButton().setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentVideoBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.videoPlayer.findViewById(R.id.back).setOnClickListener(v -> close());
        playerKey = binding.videoPlayer.getVideoKey();
        binding.videoPlayer.goneAllWidget();
        isPlayed = false;
    }

    @Override
    protected void onTouchClose(float scale) {
        super.onTouchClose(scale);
        binding.videoPlayer.findViewById(R.id.surface_container).setVisibility(View.GONE);
        binding.videoPlayer.goneAllWidget();
    }

    @Override
    protected void onTouchScale(float scale) {
        super.onTouchScale(scale);
        binding.videoPlayer.goneAllWidget();
        if (scale == 0){
            binding.videoPlayer.showAllWidget();
        }
    }

    @Override
    protected void loadImageFinish(boolean isLoadImageSuccess) {
        isLoadImageFinish = true;
        play();
    }

    private void play(){
        if (isTransitionEnd && isLoadImageFinish && !isPlayed){
            if (getLifecycle().getCurrentState() == Lifecycle.State.RESUMED){
                toPlay4Resume();
            }else {
                getLifecycle().addObserver(new LifecycleEventObserver() {
                    @Override
                    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                        if (event == Lifecycle.Event.ON_RESUME){
                            toPlay4Resume();
                            source.getLifecycle().removeObserver(this);
                        }
                    }
                });
            }

            isPlayed = true;
        }
    }

    protected void toPlay4Resume(){
        binding.videoPlayer.playUrl(openImageUrl.getVideoUrl());
        binding.videoPlayer.startPlayLogic();
    }

    @Override
    protected void onTransitionEnd() {
        super.onTransitionEnd();
        play();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (playerKey != null) {
            GSYVideoController.resumeByKey(playerKey);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (playerKey != null) {
            GSYVideoController.pauseByKey(playerKey);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (playerKey != null) {
            GSYVideoController.cancelByKeyAndDeleteKey(playerKey);
        }
    }

    @Override
    public View getExitImageView() {
        binding.videoPlayer.getThumbImageViewLayout().setVisibility(View.VISIBLE);
        return super.getExitImageView();
    }
}