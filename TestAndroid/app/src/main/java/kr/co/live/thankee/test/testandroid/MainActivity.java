package kr.co.live.thankee.test.testandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends Activity
{
	private ImageView _imgvTop;
	private ImageView _imgvBottom;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		try
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			getWindow().getAttributes().format = android.graphics.PixelFormat.RGBA_8888;

			//1. 이미지 찾기
			_imgvTop = (ImageView) findViewById(R.id.imgvTop);
			_imgvBottom = (ImageView) findViewById(R.id.imgvBottom);
			boolean isBackgroundLoaded = _imgvTop.getBackground() != null || _imgvBottom.getBackground() != null;
			if (!isBackgroundLoaded)
			{
				//*. 아직 이미지가 지정되지 않았으면 이미지 지정
				LayerDrawable image = (LayerDrawable)ImageHelper.createLargeDrawable(R.drawable.ezbiz_logo, this);

				_imgvTop.setImageDrawable(image);
				_imgvTop.getLayoutParams().width = image.getIntrinsicWidth();
				_imgvTop.getLayoutParams().height = image.getIntrinsicHeight();
			}


			//2. 이벤트 설정
			Button btnUpAndDownImage = (Button)findViewById(R.id.btnUpDownImage);
			btnUpAndDownImage.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					//Drawable background = _imgvTop.getBackground();
				}
			});

		}
		catch (Exception e)
		{
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("error message");
			dialog.setMessage(Log.getStackTraceString(e));
			dialog.create().show();
		}getWindow().getAttributes().format = android.graphics.PixelFormat.RGBA_8888;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings)
		{
			return true;
		}

		return super.onOptionsItemSelected(item);
	}



}
