package kr.co.live.thankee.test.testandroid;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btnHellowWorld = (Button)findViewById(R.id.btnHellowWorld);
		btnHellowWorld.setOnClickListener(new OnClickListener()
										  {
											  @Override
											  public void onClick(View v)
											  {
												  Toast.makeText(getApplicationContext(), "헬로 월드?", Toast.LENGTH_SHORT).show();
											  }
										  });

		Button btnOpenBrowser = (Button)findViewById(R.id.btnOpenBrowser);
		btnOpenBrowser.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent internet = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.naver.com/"));
				startActivity(internet);
			}
		});

		Button btnOpenCall = (Button)findViewById(R.id.btnOpenCall);
		btnOpenCall.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent openCall = new Intent(Intent.ACTION_VIEW, Uri.parse("010-7119-1397"));
				startActivity(openCall);
			}
		});
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
