package news24.conghuy.com.news24h.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import news24.conghuy.com.news24h.R;
import news24.conghuy.com.news24h.object.News24h;

public class AdapterLeftMenu extends RecyclerView.Adapter<AdapterLeftMenu.MyViewHolder> {

    public ArrayList<News24h> listNews24h;
    public Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView content, date;

        public MyViewHolder(View view) {
            super(view);
            content = (TextView) view.findViewById(R.id.tvContent);
        }
    }

    public AdapterLeftMenu(Context context, ArrayList<News24h> listNews24h) {
        this.context = context;
        this.listNews24h = listNews24h;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_menuleft, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.content.setText(listNews24h.get(position).getTitle().trim());
    }

    @Override
    public int getItemCount() {
        return listNews24h.size();
    }
}