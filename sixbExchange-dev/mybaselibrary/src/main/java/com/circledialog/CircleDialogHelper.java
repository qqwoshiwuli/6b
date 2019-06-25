package com.circledialog;

import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import com.circledialog.callback.ConfigButton;
import com.circledialog.callback.ConfigDialog;
import com.circledialog.callback.ConfigInput;
import com.circledialog.callback.ConfigItems;
import com.circledialog.callback.ConfigText;
import com.circledialog.callback.ConfigTitle;
import com.circledialog.params.ButtonParams;
import com.circledialog.params.DialogParams;
import com.circledialog.params.InputParams;
import com.circledialog.params.ItemsParams;
import com.circledialog.params.TextParams;
import com.circledialog.params.TitleParams;
import com.circledialog.view.listener.OnInputClickListener;
import com.fivefivelike.mybaselibrary.R;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;

import java.util.List;

import static com.fivefivelike.mybaselibrary.utils.CommonUtils.getResources;

/**
 * Created by 郭青枫 on 2017/12/21 0021.
 */


public class CircleDialogHelper {
    public static CircleDialog.Builder initDefaultToastDialog(FragmentActivity activity,
                                                              String title,
                                                              View.OnClickListener onClickListener) {
        return new CircleDialog.Builder(activity)
                .setText(title)
                .setWidth(0.7f)
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        params.backgroundColor = CommonUtils.getColor(R.color.color_Primary);//; CommonUtils.getColor(R.color.color_Primary);
                    }
                })
                .configText(new ConfigText() {
                    @Override
                    public void onConfig(TextParams params) {
                        params.gravity = Gravity.CENTER;
                        params.padding = new int[]{30, 30, 30, 30};
                        params.backgroundColor = CommonUtils.getColor(R.color.color_Primary);
                    }
                })
                .setPositive(CommonUtils.getString(R.string.str_determine), onClickListener)
                .configPositive(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.textColor = getResources().getColor(R.color.color_blue);
                        params.backgroundColor = CommonUtils.getColor(R.color.color_Primary);
                    }
                });
    }
    public static CircleDialog.Builder initDefaultToastDialog(FragmentActivity activity,
                                                              String title,
                                                              String content,
                                                              View.OnClickListener onClickListener) {
        return new CircleDialog.Builder(activity)
                .setTitle(title)
                .setTitleColor(CommonUtils.getColor(R.color.color_font1))
                .setText(content)
                .setWidth(0.7f)
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        params.backgroundColor = CommonUtils.getColor(R.color.color_Primary);//; CommonUtils.getColor(R.color.color_Primary);
                    }
                })
                .configText(new ConfigText() {
                    @Override
                    public void onConfig(TextParams params) {
                        params.gravity = Gravity.CENTER;
                        params.padding = new int[]{30, 30, 30, 30};
                        params.backgroundColor = CommonUtils.getColor(R.color.color_Primary);
                    }
                })
                .setPositive(CommonUtils.getString(R.string.str_determine), onClickListener)
                .configPositive(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.textColor = getResources().getColor(R.color.color_blue);
                        params.backgroundColor = CommonUtils.getColor(R.color.color_Primary);
                    }
                });
    }
    public static CircleDialog.Builder initDefaultDialog(FragmentActivity activity,
                                                         String title,
                                                         View.OnClickListener onClickListener) {
        return new CircleDialog.Builder(activity)
                .setCanceledOnTouchOutside(false)
                .setWidth(0.7f)
                .setCancelable(false)
                .setText(title)
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        params.backgroundColor = CommonUtils.getColor(R.color.color_Primary);
                    }
                })
                .configText(new ConfigText() {
                    @Override
                    public void onConfig(TextParams params) {
                        params.padding = new int[]{30, 30, 30, 30};
                        params.textColor = CommonUtils.getColor(R.color.color_font1);
                        params.backgroundColor = CommonUtils.getColor(R.color.color_Primary);
                    }
                })
                .setNegative(CommonUtils.getString(R.string.str_cancel), null)
                .setPositive(CommonUtils.getString(R.string.str_determine), onClickListener)
                .configNegative(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.textColor = CommonUtils.getColor(R.color.color_font2);
                        params.backgroundColor = CommonUtils.getColor(R.color.color_Primary);
                    }
                })
                .configPositive(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.textColor = getResources().getColor(R.color.color_blue);
                        params.backgroundColor = CommonUtils.getColor(R.color.color_Primary);
                    }
                });
    }

    public static CircleDialog.Builder initDefaultDialog(FragmentActivity activity,
                                                         String title,
                                                         String content,
                                                         View.OnClickListener onClickListener) {
        return new CircleDialog.Builder(activity)
                .setCanceledOnTouchOutside(false)
                .setWidth(0.7f)
                .setCancelable(false)
                .setTitle(title)
                .setTitleColor(CommonUtils.getColor(R.color.color_font1))
                .setText(content)
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        params.backgroundColor = CommonUtils.getColor(R.color.color_Primary);
                    }
                })
                .configText(new ConfigText() {
                    @Override
                    public void onConfig(TextParams params) {
                        params.padding = new int[]{30, 10, 30, 40};
                        params.textColor = CommonUtils.getColor(R.color.color_font2);
                        params.backgroundColor = CommonUtils.getColor(R.color.color_Primary);
                    }
                })
                .setNegative(CommonUtils.getString(R.string.str_cancel), null)
                .setPositive(CommonUtils.getString(R.string.str_determine), onClickListener)
                .configNegative(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.textColor = CommonUtils.getColor(R.color.color_font2);
                        params.backgroundColor = CommonUtils.getColor(R.color.color_Primary);

                    }
                })
                .configPositive(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.textColor = getResources().getColor(R.color.color_blue);
                        params.backgroundColor = CommonUtils.getColor(R.color.color_Primary);
                    }
                });
    }

    public static CircleDialog.Builder initDefaultItemDialog(FragmentActivity activity,
                                                             String[] title,
                                                             AdapterView.OnItemClickListener onItemClickListener) {
        return new CircleDialog.Builder(activity)
                .setWidth(0.9f)
                .setItems(title, onItemClickListener)
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        params.backgroundColor = CommonUtils.getColor(R.color.transparent);
                    }
                })
                .configItems(new ConfigItems() {
                    @Override
                    public void onConfig(ItemsParams params) {
                        params.textColor = getResources().getColor(R.color.color_blue);
                        params.textSize = getResources().getDimensionPixelSize(R.dimen.text_trans_32px);
                        params.backgroundColor = CommonUtils.getColor(R.color.color_Primary);
                    }
                })
                .setNegative(CommonUtils.getString(R.string.str_cancel), null)
                .configNegative(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        //取消按钮字体颜色
                        params.textColor = CommonUtils.getColor(R.color.color_font2);
                        params.textSize = getResources().getDimensionPixelSize(R.dimen.text_trans_32px);
                        params.backgroundColor = CommonUtils.getColor(R.color.color_Primary);
                    }
                });
    }

    public static CircleDialog.Builder initDefaultItemDialogWithTitle(FragmentActivity activity,
                                                                      String title,
                                                                      List<String> list, AdapterView.OnItemClickListener onItemClickListener) {
        return new CircleDialog.Builder(activity)
                .setWidth(0.9f)
                .setTitle(title)
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        params.backgroundColor = CommonUtils.getColor(R.color.transparent);
                    }
                })
                .configTitle(new ConfigTitle() {
                    @Override
                    public void onConfig(TitleParams params) {
                        params.backgroundColor = CommonUtils.getColor(R.color.color_Primary);
                    }
                })
                .setItems(list.toArray(new String[list.size()]), onItemClickListener)
                .configItems(new ConfigItems() {
                    @Override
                    public void onConfig(ItemsParams params) {
                        params.textColor = CommonUtils.getColor(R.color.color_font1);
                        params.textSize = getResources().getDimensionPixelSize(R.dimen.text_trans_26px);
                        params.backgroundColor = CommonUtils.getColor(R.color.color_Primary);
                    }
                })
                .setNegative(CommonUtils.getString(R.string.str_cancel), null)
                .configNegative(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        //取消按钮字体颜色
                        params.textColor = CommonUtils.getColor(R.color.color_font2);
                        params.textSize = getResources().getDimensionPixelSize(R.dimen.text_trans_30px);
                        params.backgroundColor = CommonUtils.getColor(R.color.color_Primary);
                    }
                });
    }

    public static CircleDialog.Builder initDefaultInputDialog(FragmentActivity activity,
                                                              String title,
                                                              String hint,
                                                              String okBtnStr,
                                                              OnInputClickListener onInputClickListener) {
        return new CircleDialog.Builder(activity)
                .setCanceledOnTouchOutside(false)
                .setCancelable(true)
                .setTitle(title)
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        params.backgroundColor = CommonUtils.getColor(R.color.color_Primary);
                    }
                })
                .setInputHint(hint)
                .configInput(new ConfigInput() {
                    @Override
                    public void onConfig(InputParams params) {
                        //                                params.inputBackgroundResourceId = R.drawable.bg_input;
                        params.hintTextColor = CommonUtils.getColor(R.color.color_font2);// getResources().getColor(R.color.color_font3);
                        params.textColor = CommonUtils.getColor(R.color.color_font1);
                        params.backgroundColor = CommonUtils.getColor(R.color.color_Primary);
                    }
                })
                .setNegative(CommonUtils.getString(R.string.str_cancel), null)
                .setPositiveInput(okBtnStr, onInputClickListener);
    }


}
