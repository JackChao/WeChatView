package com.weixin.wechatview;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jauker.widget.BadgeView;

public class MainActivity extends FragmentActivity {

	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mData;

	private TextView mChatTextView, mFriendTextView, mContactTextView;

	private BadgeView mBadgeView;

	private LinearLayout mChatLinearLayout;

	private int mScreen1_3;
	private int mCurrentPagerIndex;
	private ImageView mTabLine;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		initTabLine();

		initView();
	}

	private void initTabLine() {
		mTabLine = (ImageView) findViewById(R.id.tabline);
		// 获得屏幕的宽度
		Display display = getWindow().getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		// outMetrics包含屏幕宽高
		display.getMetrics(outMetrics);
		mScreen1_3 = outMetrics.widthPixels / 3;

		android.view.ViewGroup.LayoutParams lp = mTabLine.getLayoutParams();
		lp.width = mScreen1_3;

		mTabLine.setLayoutParams(lp);
	}

	private void initView() {

		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		mChatTextView = (TextView) findViewById(R.id.chat);
		mFriendTextView = (TextView) findViewById(R.id.friend);
		mContactTextView = (TextView) findViewById(R.id.contact);

		mChatLinearLayout = (LinearLayout) findViewById(R.id.ll_chat);

		mData = new ArrayList<Fragment>();

		// 填充数据集
		ChatTabFragment tab01 = new ChatTabFragment();
		FriendTabFragment tab02 = new FriendTabFragment();
		ContactTabFragment tab03 = new ContactTabFragment();

		mData.add(tab01);
		mData.add(tab02);
		mData.add(tab03);

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {

				return mData.size();
			}

			@Override
			public Fragment getItem(int arg0) {

				return mData.get(arg0);
			}
		};

		mViewPager.setAdapter(mAdapter);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			// 改变颜色 获取消息提示数量
			@Override
			public void onPageSelected(int position) {
				resetTextView();// 首先重置颜色为黑色
				switch (position) {
				case 0:
					if (mBadgeView != null) {
						mChatLinearLayout.removeView(mBadgeView);
					}

					mBadgeView = new BadgeView(MainActivity.this);
					mBadgeView.setBadgeCount(5);
					mChatLinearLayout.addView(mBadgeView);

					mChatTextView.setTextColor(Color.parseColor("#008000"));
					break;

				case 1:
					mFriendTextView.setTextColor(Color.parseColor("#008000"));
					break;

				case 2:
					mContactTextView.setTextColor(Color.parseColor("#008000"));
					break;
				}

				mCurrentPagerIndex = position;
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPx) {

				LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTabLine
						.getLayoutParams();

				// 方法一
				// mTabline.getLayoutParams();
				// lp.leftMargin = (int) ((position+posOffset) * mScreen1_3);
				// mTabline.setLayoutParams(lp);

				/**
				 * 方法二
				 * 从首页到第一页，不建议使用
				 */
				if (mCurrentPagerIndex == 0 && position == 0) {
					lp.leftMargin = (int) (positionOffset * mScreen1_3 + mCurrentPagerIndex
							* mScreen1_3);

				} // 从第一页到首页
				else if (mCurrentPagerIndex == 1 && position == 0) {
					lp.leftMargin = (int) (mCurrentPagerIndex * mScreen1_3 + (positionOffset - 1)
							* mScreen1_3);

				}// 从第一页到第二页
				else if (mCurrentPagerIndex == 1 && position == 1) {
					lp.leftMargin = (int) (mCurrentPagerIndex * mScreen1_3 + positionOffset
							* mScreen1_3);

				}// 从第二页到第一页
				else if (mCurrentPagerIndex == 2 && position == 1) {
					lp.leftMargin = (int) (mCurrentPagerIndex * mScreen1_3 + (positionOffset - 1)
							* mScreen1_3);
				}
				mTabLine.setLayoutParams(lp);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}

	// 默认颜色为黑色
	protected void resetTextView() {
		mChatTextView.setTextColor(Color.BLACK);
		mFriendTextView.setTextColor(Color.BLACK);
		mContactTextView.setTextColor(Color.BLACK);
	}

}
