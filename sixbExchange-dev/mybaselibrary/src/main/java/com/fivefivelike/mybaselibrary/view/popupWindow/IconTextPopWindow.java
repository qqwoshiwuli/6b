package com.fivefivelike.mybaselibrary.view.popupWindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.R;
import com.fivefivelike.mybaselibrary.adapter.IconTextPopAdapter;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;


/**
 * Created by Andy on 2018/1/24.
 */

public class IconTextPopWindow extends PopupWindow {

    private TextView btn_cancle;
    private View mPopView;
    private OnItemClickListener mListener;
    private RecyclerView recycler_view;
    private TextView btn_cancel;
    private LinearLayout id_pop_layout;
    IconTextPopAdapter adapter;
    Context mContent;

    DefaultClickLinsener defaultClickLinsener;

    public void setDefaultClickLinsener(DefaultClickLinsener defaultClickLinsener) {
        this.defaultClickLinsener = defaultClickLinsener;
    }

    public IconTextPopWindow(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        mContent = context;
        init(context);
        setPopupWindow();
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = LayoutInflater.from(context);
        //绑定布局
        mPopView = inflater.inflate(R.layout.icon_text_pop_layout, null);
        btn_cancle = (TextView) mPopView.findViewById(R.id.btn_cancel);
        recycler_view = (RecyclerView) mPopView.findViewById(R.id.recycler_view);
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setDatas(List<Entity> datas) {
        adapter = new IconTextPopAdapter(mContent, datas);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (defaultClickLinsener != null) {
                    defaultClickLinsener.onClick(view, position, null);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recycler_view.setLayoutManager(new GridLayoutManager(mContent, datas.size()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recycler_view.setAdapter(adapter);
    }

    public static class Entity {
        int id;
        String titile;

        public Entity(int id,
                      String titile) {
            this.id = id;
            this.titile = titile;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitile() {
            return titile;
        }

        public void setTitile(String titile) {
            this.titile = titile;
        }
    }

    /**
     * 设置窗口的相关属性
     */
    @SuppressLint("InlinedApi")
    private void setPopupWindow() {
        this.setContentView(mPopView);// 设置View
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);// 设置弹出窗口的高
        this.setFocusable(true);// 设置弹出窗口可
        //        this.setAnimationStyle(R.style.mypopwindow_anim_style);// 设置动画
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置背景透明
        mPopView.setOnTouchListener(new View.OnTouchListener() {// 如果触摸位置在窗口外面则销毁

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int height = mPopView.findViewById(R.id.id_pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {

    }

    /**
     * 定义一个接口，公布出去 在Activity中操作按钮的单击事件
     */
    public interface OnItemClickListener {
        void setOnItemClick(View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
}
