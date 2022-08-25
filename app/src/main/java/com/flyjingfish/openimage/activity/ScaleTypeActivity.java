package com.flyjingfish.openimage.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.request.target.Target;
import com.flyjingfish.openimage.R;
import com.flyjingfish.openimage.bean.ImageEntity;
import com.flyjingfish.openimage.databinding.ActivityScaleTypeBinding;
import com.flyjingfish.openimage.imageloader.MyImageLoader;
import com.flyjingfish.openimagelib.OpenImage;
import com.flyjingfish.openimagelib.beans.OpenImageUrl;
import com.flyjingfish.openimagelib.listener.ItemLoadHelper;
import com.flyjingfish.openimagelib.listener.OnLoadCoverImageListener;
import com.flyjingfish.openimagelib.transformers.ScaleInTransformer;


public class ScaleTypeActivity extends AppCompatActivity {

    private ActivityScaleTypeBinding binding;
    private ImageEntity itemData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScaleTypeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        itemData = new ImageEntity();
        itemData.url = "https://pics4.baidu.com/feed/50da81cb39dbb6fd95aa0c599b8d0d1e962b3708.jpeg?token=bf17224f51a6f4bb389e787f9c487940";
        setData();
    }


    private void setData() {// Target.SIZE_ORIGINAL ,Target.SIZE_ORIGINAL,
        MyImageLoader.getInstance().load(binding.ivCenter, itemData.getCoverImageUrl(), Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL, R.mipmap.img_load_placeholder, R.mipmap.img_load_placeholder);
        MyImageLoader.getInstance().load(binding.ivCenterCrop, itemData.getCoverImageUrl(), Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL, R.mipmap.img_load_placeholder, R.mipmap.img_load_placeholder);
        MyImageLoader.getInstance().load(binding.ivCenterInside, itemData.getCoverImageUrl(), Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL, R.mipmap.img_load_placeholder, R.mipmap.img_load_placeholder);
        MyImageLoader.getInstance().load(binding.ivFitStart, itemData.getCoverImageUrl(), Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL, R.mipmap.img_load_placeholder, R.mipmap.img_load_placeholder);
        MyImageLoader.getInstance().load(binding.ivFitCenter, itemData.getCoverImageUrl(), Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL, R.mipmap.img_load_placeholder, R.mipmap.img_load_placeholder);
        MyImageLoader.getInstance().load(binding.ivFitEnd, itemData.getCoverImageUrl(), Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL, R.mipmap.img_load_placeholder, R.mipmap.img_load_placeholder);
        MyImageLoader.getInstance().load(binding.ivFitXY, itemData.getCoverImageUrl(), Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL, R.mipmap.img_load_placeholder, R.mipmap.img_load_placeholder);
    }

    public void onPicClick(View view) {
        switch (view.getId()) {
            case R.id.tv_pic1:
                binding.tvPic1.setBackgroundColor(getResources().getColor(R.color.teal_200));
                binding.tvPic2.setBackgroundColor(Color.TRANSPARENT);
                itemData.url = "https://pics4.baidu.com/feed/50da81cb39dbb6fd95aa0c599b8d0d1e962b3708.jpeg?token=bf17224f51a6f4bb389e787f9c487940";
                break;
            case R.id.tv_pic2:
                binding.tvPic2.setBackgroundColor(getResources().getColor(R.color.teal_200));
                binding.tvPic1.setBackgroundColor(Color.TRANSPARENT);
                itemData.url = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fp0.itc.cn%2Fq_70%2Fimages03%2F20210227%2F6687c969b58d486fa2f23d8488b96ae4.jpeg&refer=http%3A%2F%2Fp0.itc.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1661701773&t=19043990158a1d11c2a334146020e2ce";
                break;
        }
        setData();
    }

    public void onIvClick(View view) {
        ImageView.ScaleType scaleType = ((ImageView) view).getScaleType();
        OpenImage.with(this).setClickImageView(((ImageView) view))
                .setSrcImageViewScaleType(scaleType, true)
                .setImageUrl(itemData).setImageDiskMode(MyImageLoader.imageDiskMode)
                .setItemLoadHelper(new ItemLoadHelper() {
                    @Override
                    public void loadImage(Context context, OpenImageUrl openImageUrl, String imageUrl, ImageView imageView, int overrideWidth, int overrideHeight, OnLoadCoverImageListener onLoadCoverImageListener) {
                        MyImageLoader.getInstance().load(imageView, imageUrl, overrideWidth, overrideHeight, R.mipmap.img_load_placeholder, R.mipmap.img_load_placeholder, new MyImageLoader.OnImageLoadListener() {
                            @Override
                            public void onSuccess() {
                                onLoadCoverImageListener.onLoadImageSuccess();
                            }

                            @Override
                            public void onFailed() {
                                onLoadCoverImageListener.onLoadImageFailed();
                            }
                        });
                    }
                }).addPageTransformer(new ScaleInTransformer())
                .setOpenImageStyle(R.style.DefaultPhotosTheme)
                .setClickPosition(0).show();
    }
}