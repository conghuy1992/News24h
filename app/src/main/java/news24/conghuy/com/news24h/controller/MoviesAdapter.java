package news24.conghuy.com.news24h.controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import news24.conghuy.com.news24h.ContentDetails;
import news24.conghuy.com.news24h.R;
import news24.conghuy.com.news24h.common.Apis;
import news24.conghuy.com.news24h.common.Const;
import news24.conghuy.com.news24h.model.XmlDto;

/**
 * Created by maidinh on 13/7/2016.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {
    public String TAG = "MoviesAdapter";
    public Context context;
    public List<XmlDto> xmlDtoList;
    private ArrayList<String> listAdv;

    public void update(List<XmlDto> xmlDtoList) {
        this.xmlDtoList = xmlDtoList;
        notifyDataSetChanged();
    }

    public MoviesAdapter(Context context, List<XmlDto> xmlDtoList, ArrayList<String> listAdv) {
        this.context = context;
        this.xmlDtoList = xmlDtoList;
        this.listAdv = listAdv;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView content, date;
        public ImageView img;
        public LinearLayout root;

        public MyViewHolder(View view) {
            super(view);
            content = (TextView) view.findViewById(R.id.content);
            date = (TextView) view.findViewById(R.id.date);
            img = (ImageView) view.findViewById(R.id.img);
            root = (LinearLayout) view.findViewById(R.id.root);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final XmlDto xmlDto = xmlDtoList.get(position);
        holder.content.setText(Html.fromHtml(xmlDto.title));
        holder.date.setText(xmlDto.pubDate);

        holder.content.setBackgroundColor(context.getResources().getColor(R.color.colorTrans));
        holder.date.setBackgroundColor(context.getResources().getColor(R.color.colorTrans));

        Const.setPictureFromURL(context, xmlDto.description, holder.img);
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d(TAG, "" + xmlDto.link);
                Intent intent = new Intent(context, ContentDetails.class);
                intent.putExtra(Apis.link, xmlDto.link);
                intent.putStringArrayListExtra(Const.LIST_ADV, listAdv);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return xmlDtoList.size();
    }
}