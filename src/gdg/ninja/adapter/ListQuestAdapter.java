package gdg.ninja.adapter;

import gdg.nat.R;
import gdg.ninja.gameinfo.QuestInfo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ListQuestAdapter extends BaseAdapter {
	private Context mContext;

	private List<QuestInfo> mList;
	private int itemSize;

	public void setItemSize(int itemSize) {
		this.itemSize = itemSize;
	}

	public void setList(ArrayList<QuestInfo> list) {
		this.mList = list;
	}

	public void addList(ArrayList<QuestInfo> list) {
		this.mList.addAll(mList);
	}

	public void addItem(QuestInfo item) {
		this.mList.add(item);
	}

	public ListQuestAdapter(List<QuestInfo> list, Context context) {
		this.mList = list;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public QuestInfo getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.item_ltv_list_game,
					null);
			holder = new ViewHolder();
			holder.layout = (RelativeLayout) convertView
					.findViewById(R.id.layout);
			holder.imgAvatar = (ImageView) convertView
					.findViewById(R.id.img_avatar);
			holder.imgRateOne = (ImageView) convertView
					.findViewById(R.id.rate_one);
			holder.imgRateTwo = (ImageView) convertView
					.findViewById(R.id.rate_two);
			holder.imgRateThree = (ImageView) convertView
					.findViewById(R.id.rate_three);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (holder.layout.getHeight() < itemSize) {
			LayoutParams param = new LayoutParams(itemSize, itemSize);
			holder.layout.setLayoutParams(param);
		}
		QuestInfo item = getItem(position);
		holder.imgAvatar.setImageResource(item.getImgId());
		int rate = item.getQuestStt();
		switch (rate) {
			case 6:
				holder.imgRateThree.setImageResource(R.drawable.ic_rate_on);
				holder.imgRateTwo.setImageResource(R.drawable.ic_rate_on);
				holder.imgRateOne.setImageResource(R.drawable.ic_rate_on);
				break;
			case 5:
				holder.imgRateThree.setImageResource(R.drawable.ic_rate_half);
				holder.imgRateTwo.setImageResource(R.drawable.ic_rate_on);
				holder.imgRateOne.setImageResource(R.drawable.ic_rate_on);
				break;
			case 4:
				holder.imgRateThree.setImageResource(R.drawable.ic_rate_off);
				holder.imgRateTwo.setImageResource(R.drawable.ic_rate_on);
				holder.imgRateOne.setImageResource(R.drawable.ic_rate_on);
				break;
			case 3:
				holder.imgRateThree.setImageResource(R.drawable.ic_rate_off);
				holder.imgRateTwo.setImageResource(R.drawable.ic_rate_half);
				holder.imgRateOne.setImageResource(R.drawable.ic_rate_on);
				break;
			case 2:
				holder.imgRateThree.setImageResource(R.drawable.ic_rate_off);
				holder.imgRateTwo.setImageResource(R.drawable.ic_rate_off);
				holder.imgRateOne.setImageResource(R.drawable.ic_rate_on);
				break;
			case 1:
				holder.imgRateThree.setImageResource(R.drawable.ic_rate_off);
				holder.imgRateTwo.setImageResource(R.drawable.ic_rate_off);
				holder.imgRateOne.setImageResource(R.drawable.ic_rate_half);
				break;
			default:
				holder.imgRateThree.setImageResource(R.drawable.ic_rate_off);
				holder.imgRateTwo.setImageResource(R.drawable.ic_rate_off);
				holder.imgRateOne.setImageResource(R.drawable.ic_rate_off);
				break;
		}
		return convertView;
	}

	public class ViewHolder {
		public RelativeLayout layout;
		public ImageView imgAvatar;
		public ImageView imgRateOne;
		public ImageView imgRateTwo;
		public ImageView imgRateThree;
	}
}