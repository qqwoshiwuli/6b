package com.fivefivelike.mybaselibrary.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class SearchEdittext extends AppCompatEditText {
	private Drawable mRightDrawable;
	private boolean isHasFocus;
	private TextChangeListener listener;
	private Activity activity;

	public SearchEdittext(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public SearchEdittext(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SearchEdittext(Context context) {
		super(context);
		init();
	}

	private void init() {
		// getCompoundDrawables:
		// Returns drawables for the left, top, right, and bottom borders.
		Drawable[] drawables = this.getCompoundDrawables();
		// 取得right位置的Drawable
		// 即我们在布局文件中设置的android:drawableRight
		mRightDrawable = drawables[2];
		// 设置焦点变化的监听
		this.setOnFocusChangeListener(new FocusChangeListenerImpl());
		// 设置EditText文字变化的监听
		this.addTextChangedListener(new TextWatcherImpl());
		// 初始化时让右边clean图标不可见
		setClearDrawableVisible(false);
		setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
					//这里调用搜索方法
					((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(
									activity.getCurrentFocus()
											.getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);
					if (listener != null) {
						listener.actionSearch();
					}
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				boolean isClean = (event.getX() > (getWidth() - getTotalPaddingRight()))
						&& (event.getX() < (getWidth() - getPaddingRight()));
				if (isClean) {
					setText("");
					if (listener != null) {
						listener.doClear();
					}
				}
				break;
			default:
				break;
		}
		return super.onTouchEvent(event);
	}

	private class FocusChangeListenerImpl implements OnFocusChangeListener {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			isHasFocus = hasFocus;
			if (isHasFocus) {
				boolean isVisible = !TextUtils.isEmpty(getText().toString());
				setClearDrawableVisible(isVisible);
			} else {
				setClearDrawableVisible(false);
			}
		}
	}

	// 当输入结束后判断是否显示右边clean的图标
	private class TextWatcherImpl implements TextWatcher {
		@Override
		public void afterTextChanged(Editable s) {
			boolean isVisible = !TextUtils.isEmpty(getText().toString());
			setClearDrawableVisible(isVisible);
			if (listener != null) {
				listener.getInput(s.toString());
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
									  int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
								  int count) {
		}
	}

	// 隐藏或者显示右边clean的图标
	protected void setClearDrawableVisible(boolean isVisible) {
		Drawable rightDrawable;
		if (isVisible) {
			rightDrawable = mRightDrawable;
		} else {
			rightDrawable = null;
		}
		// 使用代码设置该控件left, top, right, and bottom处的图标
		setCompoundDrawables(getCompoundDrawables()[0],
				getCompoundDrawables()[1], rightDrawable,
				getCompoundDrawables()[3]);
	}

	// 显示一个动画,以提示用户输入
	public void setShakeAnimation() {
		this.setAnimation(shakeAnimation(5));
	}

	// CycleTimes动画重复的次数
	public Animation shakeAnimation(int CycleTimes) {
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 10);
		translateAnimation.setInterpolator(new CycleInterpolator(CycleTimes));
		translateAnimation.setDuration(1000);
		return translateAnimation;
	}

	public interface TextChangeListener {
		void getInput(String text);//获取输入

		void doClear();//清除事件

		void actionSearch();//搜索事件
	}


	public void initListener(Activity activity, TextChangeListener listener) {
		this.listener = listener;
		this.activity = activity;
	}


}