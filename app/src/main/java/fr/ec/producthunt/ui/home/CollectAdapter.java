package fr.ec.producthunt.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import fr.ec.producthunt.R;
import fr.ec.producthunt.data.model.Collect;

public class CollectAdapter extends BaseAdapter {

    private List<Collect> dataSource = Collections.emptyList();

    public CollectAdapter() {
    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.name = convertView.findViewById(R.id.sub_title);
            viewHolder.title = convertView.findViewById(R.id.title);
            viewHolder.collectionImage = convertView.findViewById(R.id.img_product);

            convertView.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        Collect collect = dataSource.get(position);
        viewHolder.name.setText(collect.getName());
        viewHolder.title.setText(collect.getTitle());

        Picasso.with(parent.getContext())
                .load(collect.getImageUrl())
                .centerCrop()
                .fit()
                .into(viewHolder.collectionImage);

        return convertView;
    }

    public void showCollections(List<Collect> collects) {
        dataSource = collects;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView name;
        TextView title;
        ImageView collectionImage;
    }
}
