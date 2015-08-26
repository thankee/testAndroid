package kr.co.live.thankee.test.testandroid;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.io.InputStream;
 
/**
 * Created by Dae-kwan on 2015-08-09.
 * Android개발에 필요한 각종 헬퍼 클래스
 */
public class ImageHelper
{
	/**
	 * Created by Dae-kwan on 2015-08-09.
	 * Android App은 제한된 메모리 내에서 동작하며, 수시로 GC에 의해 메모리가 확보된다.
	 * 특히 이미지나, 포함된 Widget, Data가 많은 경우, 아래와 같은 오류가 수시로 발생할 수 있디.
	 * java.lang.OutOfMemoryError: Bitmap Size Exceeds VM Budget
	 * 이는 GC가 제때 호출되지 못해서 인데, 예를 들어 휴대폰이 Portrait에서 Landscape로 전환 될 때
	 * 안드로이드는 Landscape Activity를 새로 구축하고, 그 후 Portrait Activity를 해제하는 과정을 거친다.
	 * 이 과정에서 메모리 오버플로우가 발생할 수 있다. 이 경우 해결책은, Portrait Activity를 미리 해제하도록
	 * Reference를 해제하고 GC를 미리호출하는 것이다. 아래 메서드는 Root View를 매개변수로 지정할 경우
	 * 하위 모든 Widget과 View를 해제하고 메모리를 확보해준다.
	 *
	 * @param view 해제하고자 하는 View
	 */
	public static void unbindDrawables(View view)
	{
		if (view.getBackground() != null)
		{
			view.getBackground().setCallback(null);
		}
		if (view instanceof ViewGroup)
		{
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
			{
				unbindDrawables(((ViewGroup) view).getChildAt(i));
			}
			((ViewGroup) view).removeAllViews();
		}
	}

	/**
	 * Created by Dae-kwan on 2015-08-09.
	 * 각 장치마다 차이가 있지만 안드로이드 장치마다 최대 사용가능한 메모리 제한이 있으며,
	 * 따라서 대용량 이미지를 로드할 경우 OutOfMemoryError가 발생한다.
	 * 이 경우 대안은, 이미지를 잘게 쪼개고 현재 화면에 나타나는 이미지를 순차적으로 로드하는 것이다.
	 * 이러한 작업은 Android API Level 10(Android Gingerbread 2.3.3 - 2011.2.9)부터 제공되는
	 * BitmapRegionDecoder class가 이 작업을 자동화 해준다. 이 메서드는 해당 작업을 자동화해준다.
	 *
	 * 각 장치마다 메모리 제한이 다른데, 아무리 못해도 1024크기 이상은 지원하기 때문에, 이를 기준으로 작업한다.
	 *
	 * @param resourceId 해당 이미지의 ResourceId.
	 * @param context 현재 Activity의 Context.
	 *
	 * @return Background로 할당가능한 Drawable 인스턴스
	 *
	 * @throws IOException 이미지 타입이 지원되지 않거나 읽을 수 없는 경우
	 *
	 * @see BitmapRegionDecoder
	 */
	public static Drawable createLargeDrawable(int resourceId, Context context) throws IOException
	{
		return createLargeDrawable(resourceId, context, 1024);
	}
	/**
	 * Created by Dae-kwan on 2015-08-09.
	 * 각 장치마다 차이가 있지만 안드로이드 장치마다 최대 사용가능한 메모리 제한이 있으며,
	 * 따라서 대용량 이미지를 로드할 경우 OutOfMemoryError가 발생한다.
	 * 이 경우 대안은, 이미지를 잘게 쪼개고 현재 화면에 나타나는 이미지를 순차적으로 로드하는 것이다.
	 * 이러한 작업은 Android API Level 10(Android Gingerbread 2.3.3 - 2011.2.9)부터 제공되는
	 * BitmapRegionDecoder class가 이 작업을 자동화 해준다. 이 메서드는 해당 작업을 자동화해준다.
	 *
	 * 각 장치마다 메모리 제한이 다른데, 아무리 못해도 1024크기 이상은 지원하기 때문에, 이를 기준으로 작업한다.
	 *
	 * @param resourceId 해당 이미지의 ResourceId.
	 * @param context 현재 Activity의 Context.
	 * @param maxImageSize 이 크기를 넘어설 때, 이미지는 분할되어 로드되게 된다. 최소한 1024크기는 지원한다.
	 *
	 * @return Background로 할당가능한 Drawable 인스턴스
	 *
	 * @throws IOException 이미지 타입이 지원되지 않거나 읽을 수 없는 경우
	 *
	 * @see BitmapRegionDecoder
	 */
	public static Drawable createLargeDrawable(int resourceId, Context context, int maxImageSize) throws IOException
	{
		InputStream is = context.getResources().openRawResource(resourceId);
		BitmapRegionDecoder brd = BitmapRegionDecoder.newInstance(is, true);

		try
		{
			if (brd.getWidth() <= maxImageSize && brd.getHeight() <= maxImageSize)
			{
				return new BitmapDrawable(context.getResources(), is);
			}

			int rowCount = (int) Math.ceil((float) brd.getHeight() / (float) maxImageSize);
			int colCount = (int) Math.ceil((float) brd.getWidth() / (float) maxImageSize);

			BitmapDrawable[] drawables = new BitmapDrawable[rowCount * colCount];
			for (int i = 0; i < rowCount; i++)
			{

				int top = maxImageSize * i;
				int bottom = i == rowCount - 1 ? brd.getHeight() : top + maxImageSize;

				for (int j = 0; j < colCount; j++)
				{

					int left = maxImageSize * j;
					int right = j == colCount - 1 ? brd.getWidth() : left + maxImageSize;

					Bitmap b = brd.decodeRegion(new Rect(left, top, right, bottom), null);
					BitmapDrawable bd = new BitmapDrawable(context.getResources(), b);
					bd.setGravity(Gravity.TOP | Gravity.LEFT);
					drawables[i * colCount + j] = bd;
				}
			}

			LayerDrawable ld = new LayerDrawable(drawables);
			for (int i = 0; i < rowCount; i++)
			{
				for (int j = 0; j < colCount; j++)
				{
					ld.setLayerInset(i * colCount + j, maxImageSize * j, maxImageSize * i, 0, 0);
				}
			}

			return ld;
		}
		finally
		{
			brd.recycle();
		}
	}
}
