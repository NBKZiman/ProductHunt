package fr.ec.producthunt.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ViewAnimator;
import fr.ec.producthunt.R;
import fr.ec.producthunt.data.DataProvider;
import fr.ec.producthunt.data.SyncService;

import android.support.v4.widget.SwipeRefreshLayout


public class CollectFragments extends Fragment {

    private static final int PROGRESS_CHILD = 1;
    private static final int LIST_CHILD = 0;

    private DataProvider dataProvider;
    private CollectAdapter collectAdapter;
    private ViewAnimator viewAnimator;
    private SwipeRefreshLayout swipeRefreshLayout;

    private SyncCollectionReceiver syncCollectionReceiver;

    private Callback callback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (Callback) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.home_collection_list_fragment, container, false);

        syncCollectionReceiver = new SyncCollectionReceiver();

        collectAdapter = new CollectAdapter();

        ListView listView = rootView.findViewById(R.id.list_item);
        listView.setEmptyView(rootView.findViewById(R.id.empty_element));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Collection collection = (Collection) parent.getAdapter().getItem(position);
                callback.onClickCollection(collection);

            }
        });
        viewAnimator = rootView.findViewById(R.id.main_view_animator);
        listView.setAdapter(collectAdapter);

        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshCollections();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataProvider = DataProvider.getInstance(getActivity().getApplication());
        loadCollections();
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SyncCollectionReceiver.ACTION_LOAD_COLLECTIONS);
        LocalBroadcastManager.getInstance(this.getContext())
                .registerReceiver(syncCollectionReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this.getContext()).unregisterReceiver(syncCollectionReceiver);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.refresh:
                swipeRefreshLayout.setRefreshing(true);
                refreshCollections();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class SyncCollectionReceiver extends BroadcastReceiver {
        public static final String ACTION_LOAD_COLLECTIONS = "fr.ec.producthunt.data.action.LOAD_COLLECTIONS";

        public SyncCollectionReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction().equals(ACTION_LOAD_COLLECTIONS)) {
                loadCollections();
            }
        }
    }

    private void refreshCollections() {
        SyncService.startSyncCollections(getContext());
    }

    private void loadCollections() {
        FetchCollectionsAsyncTask fetchCollectionsAsyncTask = new FetchCollectionsAsyncTask();
        fetchCollectionsAsyncTask.execute();
    }

    private class FetchCollectionsAsyncTask extends AsyncTask<Void, Void, List<Collection>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            viewAnimator.setDisplayedChild(PROGRESS_CHILD);
        }

        @Override
        protected List<Collection> doInBackground(Void... params) {
            return dataProvider.getCollectionsFromDatabase();
        }

        @Override
        protected void onPostExecute(List<Collection> collections) {
            if (collections != null && !collections.isEmpty()) {
                collectAdapter.showCollections(collections);
            }
            swipeRefreshLayout.setRefreshing(false);
            viewAnimator.setDisplayedChild(LIST_CHILD);
        }
    }

    public interface Callback {
        void onClickCollection(Collection collection);
    }
}
