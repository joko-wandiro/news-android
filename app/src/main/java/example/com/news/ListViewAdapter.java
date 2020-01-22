package example.com.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Joko Wandiro on 20/01/2020.
 */

public class ListViewAdapter extends ArrayAdapter<Post> {

    private List<Post> playerItemList;

    private Context context;

    public ListViewAdapter(List<Post> playerItemList, Context context) {
        super(context, R.layout.list_item, playerItemList);
        this.playerItemList = playerItemList;
        this.context = context;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View listViewItem = inflater.inflate(R.layout.list_item, null, true);

        TextView label_title = listViewItem.findViewById(R.id.label_title);
        TextView label_date = listViewItem.findViewById(R.id.label_date);
        ImageView image_post = listViewItem.findViewById(R.id.image_post);


        Post post = playerItemList.get(position);

        label_title.setText(post.getTitle());
        label_date.setText(post.getDate());
        Glide.with(context).load(post.getImage()).into(image_post);

        return listViewItem;
    }
}

