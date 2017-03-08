package com.atguigu.shoppingmall_1020.app;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.atguigu.shoppingmall_1020.R;
import com.atguigu.shoppingmall_1020.home.bean.GoodsBean;
import com.atguigu.shoppingmall_1020.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class QRcode extends AppCompatActivity {

    @InjectView(R.id.iv_qr)
    ImageView ivQr;
    @InjectView(R.id.logo)
    ImageView logo;

    private GoodsBean goodsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        ButterKnife.inject(this);

        goodsBean = (GoodsBean) getIntent().getSerializableExtra("goodsBean");

        Glide.with(this)
                .load(Constants.BASE_URL_IMAGE+goodsBean.getFigure())
                .asBitmap().into(new SimpleTarget<Bitmap>() {

                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            Bitmap bitmap= EncodingUtils.createQRCode(Constants.BASE_URL_IMAGE+goodsBean.getFigure(),
                                    500,500,resource);
                            ivQr.setImageBitmap(bitmap);
            }
        });



    }


}
