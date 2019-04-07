package com.example.getfood.ui.orderlist;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.getfood.models.OrderListItem;
import com.example.getfood.R;
import com.example.getfood.ui.base.BaseActivity;
import com.example.getfood.ui.orderdetail.OrderDetailActivity;

import java.util.ArrayList;

public class OrderListActivity extends BaseActivity implements OrderListMvpView {

    private ListView ordersListView;
    private String rollNo;
    private OrderListDisplayAdapter orderListDisplayAdapter;
    private OrderListPresenter<OrderListActivity> presenter;

    @Override
    public void initViews() {
        showLoading();
        ordersListView = findViewById(R.id.ordersListView);
//        ordersHeadingTextView = findViewById(R.id.ordersHeadingTextView);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.your_orders));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        presenter = new OrderListPresenter<>();
        presenter.onAttach(this);

        Intent data = getIntent();
        rollNo = data.getStringExtra(getString(R.string.i_roll_no));

        //        fetch all the order IDs of the user first
        presenter.fetchOrderList(rollNo);
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
    public void bindListAdapter(final ArrayList<OrderListItem> orderListItems) {
        orderListDisplayAdapter = new OrderListDisplayAdapter(orderListItems, getApplicationContext());
        ordersListView.setAdapter(orderListDisplayAdapter);
        hideLoading();

//        attach OnItemClickListener
        ordersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(OrderListActivity.this, OrderDetailActivity.class);
                i.putExtra(getString(R.string.i_order_id), orderListItems.get(position).getOrderID());
                i.putExtra(getString(R.string.i_total), orderListItems.get(position).getOrderAmount());
                i.putExtra(getString(R.string.i_roll_no), rollNo);
                startActivity(i);
            }
        });
    }
}
