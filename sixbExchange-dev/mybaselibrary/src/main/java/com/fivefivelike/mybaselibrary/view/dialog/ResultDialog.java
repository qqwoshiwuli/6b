package com.fivefivelike.mybaselibrary.view.dialog;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.circledialog.CircleDialog;
import com.circledialog.callback.ConfigButton;
import com.circledialog.callback.ConfigText;
import com.circledialog.callback.ConfigTitle;
import com.circledialog.params.ButtonParams;
import com.circledialog.params.TextParams;
import com.circledialog.params.TitleParams;
import com.fivefivelike.mybaselibrary.R;
import com.fivefivelike.mybaselibrary.entity.ResultDialogEntity;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GlobleContext;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;

/**
 * Created by 郭青枫 on 2017/11/24.
 */

public class ResultDialog {

    public static final int CANNEL_POSITION = 0;
    public static final int CONFIRM_POSITION = 1;
    public static final String DIALOG_KEY = "dialog";

    Context context;

    private ResultDialog(Context context) {
        this.context = context;
    }

    private static class helper {
        private static ResultDialog resultDialog = new ResultDialog(GlobleContext.getInstance().getApplicationContext());
    }

    public static ResultDialog getInstence() {
        return helper.resultDialog;
    }

    public ResultDialogEntity ShowResultDialog(FragmentActivity activity, String data, DefaultClickLinsener defaultClickLinsener) {
        if (!TextUtils.isEmpty(data)) {
            ResultDialogEntity resultDialogEntity = GsonUtil.getInstance().toObj(data, ResultDialogEntity.class);
            if ("1".equals(resultDialogEntity.getType())) {
                initAlertDialog(activity, resultDialogEntity, defaultClickLinsener).show();
            } else if ("0".equals(resultDialogEntity.getType())) {
                initConfirmDialog(activity, resultDialogEntity, defaultClickLinsener).show();
            } else if ("2".equals(resultDialogEntity.getType())) {
                if (resultDialogEntity.getTitle().contains("轮播"))
                {
                }else if (resultDialogEntity.getTitle().contains("版本"))
                {
                }else
                {
                    ToastUtil.show(TextUtils.isEmpty(resultDialogEntity.getTitle()) ?
                            resultDialogEntity.getContent() : resultDialogEntity.getTitle());
                }
            }
            return resultDialogEntity;
        }
        return null;
    }

    private CircleDialog.Builder initAlertDialog(FragmentActivity activity,
                                                 final ResultDialogEntity resultDialogEntity,
                                                 final DefaultClickLinsener defaultClickLinsener) {
        CircleDialog.Builder builder = new CircleDialog.Builder(activity);
        if (!TextUtils.isEmpty(resultDialogEntity.getContent())) {
            builder.setTitle(resultDialogEntity.getTitle())
                    .configTitle(new ConfigTitle() {
                        @Override
                        public void onConfig(TitleParams params) {
                            if (TextUtils.isEmpty(resultDialogEntity.getTitleColor())) {
                                params.textColor = context.getResources().getColor(R.color.black);
                            } else {
                                params.textColor = Color.parseColor(resultDialogEntity.getTitleColor());
                            }
                        }
                    });
        }
        builder.setText(TextUtils.isEmpty(resultDialogEntity.getContent()) ? resultDialogEntity.getTitle() : resultDialogEntity.getContent())
                .configText(new ConfigText() {
                    @Override
                    public void onConfig(TextParams params) {
                        if (!TextUtils.isEmpty(resultDialogEntity.getContent())) {
                            if (TextUtils.isEmpty(resultDialogEntity.getContentColor())) {
                                params.textColor = context.getResources().getColor(TextUtils.isEmpty(resultDialogEntity.getContent()) ? R.color.black : R.color.font_grey);
                            } else {
                                params.textColor = Color.parseColor(resultDialogEntity.getContentColor());
                            }
                            params.gravity = Gravity.CENTER;
                            params.padding = new int[]{30, TextUtils.isEmpty(resultDialogEntity.getContent()) ? 30 : 0, 30, 30};
                        } else {
                            params.padding = new int[]{30, 30, 30, 0};
                        }
                    }
                });
        builder.setWidth(0.7f)
                .setPositive(TextUtils.isEmpty(resultDialogEntity.getConfirmBtn()) ? CommonUtils.getString(R.string.str_determine) : resultDialogEntity.getConfirmBtn(), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        defaultClickLinsener.onClick(view, CONFIRM_POSITION, resultDialogEntity);
                    }
                })
                .configPositive(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        if (TextUtils.isEmpty(resultDialogEntity.getConfirmColor())) {
                            params.textColor = context.getResources().getColor(R.color.color_blue);
                        } else {
                            params.textColor = Color.parseColor(resultDialogEntity.getConfirmColor());
                        }
                    }
                });
        return builder;
    }

    private CircleDialog.Builder initConfirmDialog(FragmentActivity activity,
                                                   final ResultDialogEntity resultDialogEntity,
                                                   final DefaultClickLinsener defaultClickLinsener) {
        CircleDialog.Builder builder = new CircleDialog.Builder(activity);
        if (!TextUtils.isEmpty(resultDialogEntity.getContent())) {
            builder.setTitle(resultDialogEntity.getTitle())
                    .configTitle(new ConfigTitle() {
                        @Override
                        public void onConfig(TitleParams params) {
                            if (TextUtils.isEmpty(resultDialogEntity.getTitleColor())) {
                                params.textColor = context.getResources().getColor(R.color.black);
                            } else {
                                params.textColor = Color.parseColor(resultDialogEntity.getTitleColor());
                            }
                        }
                    });
        }
        builder.setText(TextUtils.isEmpty(resultDialogEntity.getContent()) ?
                resultDialogEntity.getTitle() : resultDialogEntity.getContent())
                .configText(new ConfigText() {
                    @Override
                    public void onConfig(TextParams params) {
                        if (!TextUtils.isEmpty(resultDialogEntity.getContent())) {
                            if (TextUtils.isEmpty(resultDialogEntity.getContentColor())) {
                                params.textColor = context.getResources().getColor(TextUtils.isEmpty(resultDialogEntity.getContent()) ? R.color.black : R.color.font_grey);
                            } else {
                                params.textColor = Color.parseColor(resultDialogEntity.getContentColor());
                            }
                            params.gravity = Gravity.CENTER;
                            params.padding = new int[]{30, TextUtils.isEmpty(resultDialogEntity.getContent()) ? 30 : 0, 30, 30};
                        } else {
                            params.padding = new int[]{30, 30, 30, 0};
                        }
                    }
                });
        builder.setWidth(0.7f)
                .setPositive(TextUtils.isEmpty(resultDialogEntity.getConfirmBtn()) ? CommonUtils.getString(R.string.str_determine) : resultDialogEntity.getConfirmBtn(), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        defaultClickLinsener.onClick(view, CONFIRM_POSITION, resultDialogEntity);
                    }
                })
                .configPositive(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        if (TextUtils.isEmpty(resultDialogEntity.getConfirmColor())) {
                            params.textColor = context.getResources().getColor(R.color.color_blue);
                        } else {
                            params.textColor = Color.parseColor(resultDialogEntity.getConfirmColor());
                        }
                    }
                })
                .setNegative(TextUtils.isEmpty(resultDialogEntity.getCancelBtn()) ? CommonUtils.getString(R.string.str_cancel) : resultDialogEntity.getCancelBtn(), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        defaultClickLinsener.onClick(view, CANNEL_POSITION, resultDialogEntity);
                    }
                })
                .configNegative(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        if (TextUtils.isEmpty(resultDialogEntity.getCancelColor())) {
                            params.textColor = context.getResources().getColor(R.color.font_grey);
                        } else {
                            params.textColor = Color.parseColor(resultDialogEntity.getCancelColor());
                        }
                    }
                });
        return builder;
    }


}
