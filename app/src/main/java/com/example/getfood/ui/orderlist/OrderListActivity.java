package com.example.getfood.ui.orderlist;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.getfood.R;
import com.example.getfood.ui.base.BaseActivity;
import com.fazemeright.canteen_app_models.models.FullOrder;

import java.util.ArrayList;

public class OrderListActivity extends BaseActivity implements OrderListMvpView {

    private RecyclerView ordersListRecyclerView;
    private OrderListRecyclerViewDisplayAdapter orderListDisplayAdapter;
    private OrderListPresenter<OrderListActivity> presenter;

    @Override
    public void initViews() {
        showLoading();
        ordersListRecyclerView = findViewById(R.id.ordersListRecyclerView);
        ordersListRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        ordersListRecyclerView.addItemDecoration(new DividerItemDecoration(ordersListRecyclerView.getContext(), LinearLayoutManager.VERTICAL));

//        ordersHeadingTextView = findViewById(R.id.ordersHeadingTextView);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.your_orders));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        presenter = new OrderListPresenter<>();
        presenter.onAttach(this);

        orderListDisplayAdapter = new OrderListRecyclerViewDisplayAdapter(mContext);
        ordersListRecyclerView.setAdapter(orderListDisplayAdapter);

        //        fetch all the order IDs of the user first
        presenter.fetchOrderList();
    }

    @Override
    public void setListeners() {
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_order_list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void bindListAdapter(ArrayList<FullOrder> orderListItems) {
        orderListDisplayAdapter.submitList(orderListItems);
        hideLoading();
    }

    @Override
    public void onRollNumberNull() {
//        TODO: Either refresh state of Auth or redirect to LoginActivity
    }
}
