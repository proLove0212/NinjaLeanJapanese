package gdg.ninja.navigate;

import gdg.nat.R;
import gdg.ninja.util.NLog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NavigationBar extends RelativeLayout {
	private final String TAG = "NavigationBar";

	private ImageButton btnLeft, btnRight;
	private TextView txtTitle;
	private NavigationManager mNaviManager;

	public NavigationBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NavigationBar(Context context) {
		super(context);
	}

	/* When this view finish inflate. */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		btnLeft = (ImageButton) findViewById(R.id.btn_left_navi);
		txtTitle = (TextView) findViewById(R.id.txt_title_navi);
		btnRight = (ImageButton) findViewById(R.id.btn_right_navi);
	}

	public void initNaviBar(NavigationManager naviManager) {
		this.mNaviManager = naviManager;
		setupLeftButton();
		setupRightButton();
		mNaviManager
				.addBackStackChangeListener(new OnBackStackChangedListener() {
					@Override
					public void onBackStackChanged() {
						synsNavigationBar();
					}
				});
	}

	private void setupLeftButton() {
		btnLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment fragment = mNaviManager.getActiveFragment();
				if (fragment == null)
					return;
				NLog.i(TAG, "Fragment(btnLeft): " + fragment.toString());
				if (fragment instanceof INavigationBarListener) {
					((INavigationBarListener) fragment).onLeftClicked();
				}
			}
		});
	}

	private void setupRightButton() {
		btnRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment fragment = mNaviManager.getActiveFragment();
				if (fragment == null)
					return;
				NLog.i(TAG, "Fragment(btnRight): " + fragment.toString());
				if (fragment instanceof INavigationBarListener) {
					((INavigationBarListener) fragment).onRightClicked();
				}
			}
		});
	}

	private void synsNavigationBar() {
		Fragment fragment = mNaviManager.getActiveFragment();
		if (fragment == null)
			return;
		resetNavigationBar();
		NLog.i(TAG, "Fragment(synsNavigationBar): " + fragment.toString());
		if (fragment instanceof INavigationBarListener) {
			INavigationBarListener naviBarListener = (INavigationBarListener) fragment;
			setTitle(naviBarListener.getTitle());
			BTN_LEFT_MODE leftMode = naviBarListener.getButtonLeftMode();
			switch (leftMode) {
				case BACK:
					setBtnBack();
					break;
				case NONE:
				default:
					hideBtnLeft();
			}
			BTN_RIGHT_MODE rightMode = naviBarListener.getButtonRightMode();
			switch (rightMode) {
				case FACEBOOK:
					setBtnFacebook();
					break;
				case NONE:
				default:
					hideBtnRight();
			}
		}
	}

	public void setTitle(String title) {
		txtTitle.setText(title);
	}

	private void resetNavigationBar() {
		btnLeft.setVisibility(View.GONE);
		txtTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		btnRight.setVisibility(View.GONE);
	}

	public void setBtnBack() {
		btnLeft.setImageResource(R.drawable.ic_btn_back);
		btnLeft.setVisibility(View.VISIBLE);
		if (btnRight.getVisibility() == View.GONE) {
			btnRight.setVisibility(View.INVISIBLE);
			txtTitle.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.ic_btn_back_arrow, 0, 0, 0);
		} else {
			txtTitle.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.ic_btn_back_arrow, 0,
					R.drawable.ic_btn_facebook_arrow, 0);
		}
	}

	public void setBtnFacebook() {
		btnRight.setImageResource(R.drawable.ic_facebook);
		btnRight.setVisibility(View.VISIBLE);
		if (btnRight.getVisibility() == View.GONE) {
			btnRight.setVisibility(View.INVISIBLE);
			txtTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.ic_btn_facebook_arrow, 0);
		} else {
			txtTitle.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.ic_btn_back_arrow, 0,
					R.drawable.ic_btn_facebook_arrow, 0);
		}
	}

	public void hideBtnLeft() {
		btnLeft.setOnClickListener(null);
		if (btnRight.getVisibility() == View.VISIBLE) {
			btnLeft.setVisibility(View.INVISIBLE);
			Drawable[] drawable = txtTitle.getCompoundDrawables();
			txtTitle.setCompoundDrawablesWithIntrinsicBounds(null, null,
					drawable[2], null);
		} else {
			btnLeft.setVisibility(View.GONE);
			btnRight.setVisibility(View.GONE);
			txtTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		}
	}

	public void hideBtnRight() {
		btnRight.setOnClickListener(null);
		if (btnLeft.getVisibility() == View.VISIBLE) {
			btnRight.setVisibility(View.INVISIBLE);
			Drawable[] drawable = txtTitle.getCompoundDrawables();
			txtTitle.setCompoundDrawablesWithIntrinsicBounds(drawable[0], null,
					null, null);
		} else {
			btnLeft.setVisibility(View.GONE);
			btnRight.setVisibility(View.GONE);
			txtTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		}
	}

	public interface INavigationBarListener {
		public String getTitle();

		public BTN_LEFT_MODE getButtonLeftMode();

		public void onLeftClicked();

		public BTN_RIGHT_MODE getButtonRightMode();

		public void onRightClicked();
	}

	public enum BTN_LEFT_MODE {
		BACK, NONE;
	}

	public enum BTN_RIGHT_MODE {
		FACEBOOK, NONE;
	}
}