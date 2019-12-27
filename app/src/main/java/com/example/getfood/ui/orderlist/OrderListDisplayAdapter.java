package com.example.getfood.ui.orderlist;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.getfood.R;
import com.example.getfood.ui.orderdetail.OrderDetailActivity;
import com.fazemeright.canteen_app_models.models.FullOrder;

import java.util.List;

public class OrderListDisplayAdapter extends ListAdapter<FullOrder, OrderListDisplayAdapter.ViewHolder> {

    private Context context;

    OrderListDisplayAdapter(Context context) {
        super(new OrderListDiffCallBack());
        this.context = context;
    }

    void swapData(List<FullOrder> newList) {
        submitList(newList);
    }

    @NonNull
    @Override
    public OrderListDisplayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list_display_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderListDisplayAdapter.ViewHolder holder, int position) {
        FullOrder item = getItem(position);

        holder.bind(item);
    }

    static class OrderListDiffCallBack extends DiffUtil.ItemCallback<FullOrder> {

        @Override
        public boolean areItemsTheSame(@NonNull FullOrder order, @NonNull FullOrder t1) {
            return order.getOrderId().equals(t1.getOrderId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull FullOrder order, @NonNull FullOrder t1) {
            return order.equals(t1);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView orderIDTextView, orderAmountTextView, orderTimeTextView;

        ViewHolder(View itemView) {
            super(itemView);
            orderIDTextView = itemView.findViewById(R.id.orderIDTextView);
            orderAmountTextView = itemView.findViewById(R.id.orderAmountTextView);
            orderTimeTextView = itemView.findViewById(R.id.orderTimeTextView);
        }

        void bind(final FullOrder item) {

            String orderId;
            if (item.getDisplayID() != null &&
                    !item.getDisplayID().isEmpty()) {
                orderId = item.getDisplayID();
            } else {
                orderId = item.getOrderId();
            }
            orderIDTextView.setText(String.format("Order ID: %s", orderId));
            orderAmountTextView.setText(String.format("Amount: ₹ %s",
                    item.getOrderAmount()));
            orderTimeTextView.setText(item.getTimeToDeliver());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, OrderDetailActivity.class);
                    i.putExtra("TestOrderData", item);
                    context.startActivity(i);
                }
            });
        }
    }
}
