package MyAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import Model.Data;

import com.example.shoppinglist_zeeshan.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    List<Data> list;
    public MyAdapter(List<Data> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_view, parent, false);
        MyViewHolder v = new MyViewHolder(view);
        return v;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Data data = list.get(position);
        holder.item.setText(data.getItem());
        holder.desc.setText(data.getDesc());
        String stringPrice = String.valueOf(data.getPrice());
        holder.price.setText(stringPrice);
        holder.time.setText(data.getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView item, desc, price, time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.itemID);
            desc = itemView.findViewById(R.id.descriptionID);
            price = itemView.findViewById(R.id.priceID);
            time = itemView.findViewById(R.id.timeID);

        }
    }
}
