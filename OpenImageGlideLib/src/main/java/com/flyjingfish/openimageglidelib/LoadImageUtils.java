package com.flyjingfish.openimageglidelib;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.flyjingfish.openimagelib.listener.OnLoadBigImageListener;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;

public enum LoadImageUtils {
    INSTANCE;
    private final ExecutorService cThreadPool = Executors.newFixedThreadPool(5);
    private final Handler handler = new Handler(Looper.getMainLooper());
    private OkHttpClient okHttpClient;

    public synchronized void initOkHttpClient(){
        this.okHttpClient = ProgressManager.getInstance().with(new OkHttpClient.Builder())
                .build();
    }

    public OkHttpClient getOkHttpClient() {
        if (okHttpClient == null){
            initOkHttpClient();
        }
        return okHttpClient;
    }

    public boolean isInitOkHttpClient() {
        return okHttpClient != null;
    }

    public void loadImageForSize(Context context, String imageUrl, OnLocalRealFinishListener finishListener) {
        boolean isWeb = BitmapUtils.isWeb(imageUrl);

        if (!isWeb) {
            cThreadPool.submit(() -> {
                int[] size = BitmapUtils.getImageSize(context, imageUrl);
                int[] maxImageSize = BitmapUtils.getMaxImageSize(size[0], size[1]);

                handler.post(() -> {
                    if (context instanceof LifecycleOwner){
                        LifecycleOwner lifecycleOwner = (LifecycleOwner) context;
                        if (lifecycleOwner.getLifecycle().getCurrentState() != Lifecycle.State.DESTROYED){
                            finishListener.onGoLoad(maxImageSize, false);
                        }
                    }else if (context instanceof Activity){
                        if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()){
                            finishListener.onGoLoad(maxImageSize, false);
                        }
                    }
                });
            });
        } else {
            int[] maxImageSize = new int[]{Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL};
            finishListener.onGoLoad(maxImageSize, true);
        }

    }

    public void loadWebImage(Context context, String imageUrl, final OnLoadBigImageListener onLoadBigImageListener, OnLocalRealFinishListener finishListener){
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        Glide.with(context).asFile()
                .load(imageUrl).apply(requestOptions).addListener(new RequestListener<File>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                        onLoadBigImageListener.onLoadImageFailed();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(new CustomTarget<File>() {
                    @Override
                    public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                        loadImageForSize(context,resource.getPath(),finishListener);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    void saveFile(Context context, File resource, boolean video,OnSaveFinish onSaveFinish) {
        cThreadPool.submit(() -> {
            String sucPath = FileUtils.save(context, resource, video);
            if (onSaveFinish != null){
                handler.post(() -> onSaveFinish.onFinish(sucPath));
            }
        });

    }

    interface OnSaveFinish{
        void onFinish(String sucPath);
    }
}
