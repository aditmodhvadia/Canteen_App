package com.example.getfood.ui.orderlist;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.getfood.R;
import com.example.getfood.models.FullOrder;
import com.example.getfood.ui.base.BaseActivity;

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

        //        fetch all the order IDs of the user first
        presenter.fetchOrderList(mRollNo);
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
    public void bindListAdapter(final ArrayList<FullOrder> orderListItems) {
        orderListDisplayAdapter = new OrderListRecyclerViewDisplayAdapter(orderListItems, mContext);
        ordersListRecyclerView.setAdapter(orderListDisplayAdapter);
        hideLoading();

//        attach OnItemClickListener
        /*ordersListRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(OrderListActivity.this, OrderDetailActivity.class);
                i.putExtra("TestOrderData", orderListItems.get(position));
                startActivity(i);
            }
        });*/
    }
}
