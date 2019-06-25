package com.fivefivelike.mybaselibrary.utils;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import com.circledialog.CircleDialog;
import com.circledialog.callback.ConfigButton;
import com.circledialog.callback.ConfigDialog;
import com.circledialog.callback.ConfigInput;
import com.circledialog.callback.ConfigText;
import com.circledialog.params.ButtonParams;
import com.circledialog.params.DialogParams;
import com.circledialog.params.InputParams;
import com.circledialog.params.ProgressParams;
import com.circledialog.params.TextParams;
import com.circledialog.view.listener.OnInputClickListener;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 郭青枫 on 2017/9/15.
 */

public class DialogDeclare {
    public void show(final AppCompatActivity context) {
        //        单个按钮
        new CircleDialog.Builder(context)
                .setTitle("标题")
                .setText("提示框")
                .configText(new ConfigText() {
                    @Override
                    public void onConfig(TextParams params) {
                        params.gravity = Gravity.LEFT;
                        params.padding = new int[]{50, 50, 50, 50};
                    }
                })
                .setPositive("确定", null)
                .show();
        //        两个按钮
        new CircleDialog.Builder(context)
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .setTitle("标题")
                .setText("确定框")
                .setNegative("取消", null)
                .setPositive("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .show();
        //        底部多选
        final String[] items = {"拍照", "从相册选择", "小视频"};
        new CircleDialog.Builder(context)
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        //增加弹出动画
                        //                        params.animStyle = R.style.dialogWindowAnim;
                    }
                })
                .setTitle("标题")
                .setTitleColor(Color.BLUE)
                .setItems(items, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int
                            position, long id) {

                    }
                })
                .setNegative("取消", null)
                .configNegative(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        //取消按钮字体颜色
                        params.textColor = Color.RED;
                    }
                })
                .show();
        //        输入弹出框
        new CircleDialog.Builder(context)
                .setCanceledOnTouchOutside(false)
                .setCancelable(true)
                .setTitle("输入框")
                .setInputHint("请输入条件")
                .configInput(new ConfigInput() {
                    @Override
                    public void onConfig(InputParams params) {
                        //                                params.inputBackgroundResourceId = R.drawable.bg_input;
                    }
                })
                .setNegative("取消", null)
                .setPositiveInput("确定", new OnInputClickListener() {
                    @Override
                    public void onClick(String text, View v) {

                    }
                })
                .show();
        //        进度弹出框
        final Timer timer = new Timer();
        final CircleDialog.Builder builder = new CircleDialog.Builder(context);
        builder.setCancelable(false).setCanceledOnTouchOutside(false)
                .setTitle("下载")
                .setProgressText("已经下载")
                //                        .setProgressText("已经下载%s了")
                //                        .setProgressDrawable(R.drawable.bg_progress_h)
                .setNegative("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timer.cancel();
                    }
                })
                .show();
        TimerTask timerTask = new TimerTask() {
            final int max = 222;
            int progress = 0;

            @Override
            public void run() {
                progress++;
                if (progress >= max) {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            builder.setProgressText("下载完成").create();
                            timer.cancel();
                        }
                    });
                } else {
                    builder.setProgress(max, progress).create();
                }
            }
        };
        timer.schedule(timerTask, 0, 50);
        //        等待弹出框
        final DialogFragment dialogFragment = new CircleDialog.Builder(context)
                .setProgressText("登录中...")
                .setProgressStyle(ProgressParams.STYLE_SPINNER)
                //                        .setProgressDrawable(R.drawable.bg_progress_s)
                .show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialogFragment.dismiss();
            }
        }, 3000);
        // 动态改变内容弹出框
        builder.configDialog(new ConfigDialog() {
            @Override
            public void onConfig(DialogParams params) {
                params.gravity = Gravity.TOP;
                //                        TranslateAnimation refreshAnimation = new TranslateAnimation(15, -15,
                // 0, 0);
                //                        refreshAnimation.setInterpolator(new OvershootInterpolator());
                //                        refreshAnimation.setDuration(100);
                //                        refreshAnimation.setRepeatCount(3);
                //                        refreshAnimation.setRepeatMode(Animation.RESTART);
                //                params.refreshAnimation = R.anim.refresh_animation;
            }
        })
                .setTitle("动态改变内容")
                .setText("3秒后更新其它内容")
                .show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                builder.setText("已经更新内容").create();
            }
        }, 3000);
    }
}
