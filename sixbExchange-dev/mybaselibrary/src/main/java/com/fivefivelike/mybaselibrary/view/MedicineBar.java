package com.fivefivelike.mybaselibrary.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class MedicineBar extends View{
	private String city="#abcdefghijklmnopqrstuvwxyz";
	private OnClickGetWord click;
	private int height;
	private int choose=-1;
	public MedicineBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public MedicineBar(Context context, AttributeSet attrs) {
		super(context, attrs,0);
	}
	public MedicineBar(Context context) {
		super(context,null);
	}
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint=new Paint();
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);
		paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 
				15, getResources().getDisplayMetrics()));
		int width=getWidth();
		height=getHeight();
		int eachHeight=height/city.length();
		for(int i=0;i<city.length();i++){
			String text=String.valueOf(city.charAt(i));
			float textWidth = paint.measureText(text);
			canvas.drawText(text, 
					(width-textWidth)/2,
					(1+i)*eachHeight, paint);
		}
		paint.reset();
	}
	public interface  OnClickGetWord{
		public void getWord(MotionEvent event, String word);
	}
	public void setOnClickGetWord(OnClickGetWord  click){
		this.click=click;
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(click!=null){
				float y=event.getY();
				int index=(int) (y/height*city.length());
				if(choose!=index&&index>0&&index<city.length()){
					choose=index;
					click.getWord(event,String.valueOf(city.charAt(index)));
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if(click!=null){
				click.getWord(event,String.valueOf(city.charAt(choose)));
			}
			break;
		default:
			break;
		}
		return true;
	}
	
}
