package com.mantra.ionnews.ui.fragments;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mantra.ionnews.R;
import com.mantra.ionnews.adapters.SearchAdapter;
import com.mantra.ionnews.interfaces.BaseResponseInterface;
import com.mantra.ionnews.interfaces.OnFragmentEventTagListener;
import com.mantra.ionnews.interfaces.OnProfileGridItemClickListener;
import com.mantra.ionnews.models.DashboardRequest;
import com.mantra.ionnews.models.FragmentState;
import com.mantra.ionnews.models.Story;
import com.mantra.ionnews.models.responses.Error;
import com.mantra.ionnews.models.responses.GetAllLikesResponse;
import com.mantra.ionnews.models.responses.StoriesResponse;
import com.mantra.ionnews.ui.activities.DashboardActivity;
import com.mantra.ionnews.ui.activities.NewsDetailActivity;
import com.mantra.ionnews.ui.activities.SearchResultActivity;
import com.mantra.ionnews.ui.activities.TAGActivity;
import com.mantra.ionnews.ui.customui.GridDividerDecoration;
import com.mantra.ionnews.utils.LocalStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.event.EventBus;

import static com.mantra.ionnews.restclient.ApiEndpoints.CATAGORY_AND_TAG_LIST;
import static com.mantra.ionnews.utils.AppConstants.BASE_URL;
import static com.mantra.ionnews.utils.AppConstants.KEY_CATEGORY_ID;
import static com.mantra.ionnews.utils.AppConstants.KEY_CATEGORY_NAME;
import static com.mantra.ionnews.utils.AppConstants.KEY_CATEGORY_STORIES;
import static com.mantra.ionnews.utils.AppConstants.KEY_LIKED_STORIES;
import static com.mantra.ionnews.utils.AppConstants.KEY_LIKED_STORY_INDEX;
import static com.mantra.ionnews.utils.AppConstants.KEY_TAG_STORIES_PREF;
import static com.mantra.ionnews.utils.AppConstants.KEY_USER_ID;
import static com.mantra.ionnews.utils.ConstantClass.SEARCH;
import static com.mantra.ionnews.utils.ConstantClass.STORIES_RESPONSE;


public class SearchFragment extends BaseFragment implements OnFragmentEventTagListener, OnProfileGridItemClickListener, BaseResponseInterface, SearchView.OnQueryTextListener {

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private static String TAG = SEARCH + " ==>";

    private SearchView searchView;
    private RecyclerView recyclerView;

    private List<StoriesResponse> storiesResponseList = new ArrayList<>();
    SearchAdapter searchAdapter;
    private GetAllLikesResponse likesResponse;
    private List<Story> likesItemList = new ArrayList<>();

    RequestQueue requestQueue;
    SearchView searchBox;
    List<String> searchBoxStoryList = new ArrayList<>();
    ArrayAdapter<String> listAdapter = null;
    ListView listview;
    private RecyclerView.LayoutManager profileGridLayoutManager;
    private boolean isLikeView = false, isDecorationAdded = false;


    public SearchFragment() {

    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
//        args.putParcelableArrayList(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        DashboardActivity.onFragmentEventTagListener = this;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_search, container, false);

        updateFragment();
        initView(layout);
        storiesResponseList = LocalStorage.getInstance(getContext()).getStories();
        searchAdapter = new SearchAdapter(storiesResponseList, null, this);
        setUpProfileGrid();



       /* RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(searchAdapter);*/

        storiesResponseList = LocalStorage.getInstance(getContext()).getStories();
        likesResponse = LocalStorage.getInstance(getContext()).getAllLikes();
        if (likesResponse != null) {
            likesItemList = likesResponse.getData();
        }


        searchBox.setOnQueryTextListener(this);



        searchBox.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    recyclerView.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                } else {
                    listview.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                }
            }
        });




        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {



                String selection = (String) parent.getItemAtPosition(position);
                int pos = -1;

                for (int i = 0; i < searchBoxStoryList.size(); i++) {
                    if (searchBoxStoryList.get(i).equals(selection)) {
                        pos = i;
                        break;
                    }
                }

                String item = ((TextView)view).getText().toString();
                if(!item.isEmpty())
                {
                    String replaceString = item.replace("#","");
                    fetchCatagoryAndTAG(LocalStorage.getInstance(getActivity()).getID(),replaceString);
                }
                else {
                    Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(KEY_USER_ID, Integer.parseInt(LocalStorage.getInstance(getContext()).getUserID()));
                bundle.putString(KEY_CATEGORY_ID, String.valueOf(pos));
                bundle.putString(KEY_CATEGORY_NAME, item);
                intent.putExtras(bundle);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_up, 0);
                }

            }
        });
        final String token = LocalStorage.getInstance(getActivity()).getToken();

        listAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.activity_listview,searchBoxStoryList);
        //attach the adapter
        requestQueue =  Volley.newRequestQueue(getActivity());

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("Authorization",token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET,BASE_URL + CATAGORY_AND_TAG_LIST,jsonObject,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                JSONArray catagoryArray=response.optJSONArray("category");
                searchBoxStoryList.add("CATAGORY");
                for (int i = 0; i < catagoryArray.length(); i++) {
                    String id = catagoryArray.optJSONObject(i).optString("id");
                    String name = catagoryArray.optJSONObject(i).optString("name");
                    String slugName = catagoryArray.optJSONObject(i).optString("slug_name");
                    Log.d("TAG", "id "+ id + " ask "+ name+"slugName"+slugName);
                    searchBoxStoryList.add(name);
                    listview.setAdapter(listAdapter);

                }

                searchBoxStoryList.add("TAG");

                JSONArray itemArray=response.optJSONArray("tag");
                for (int i = 0; i < itemArray.length(); i++) {
                    String value= null;
                    try {
                        value = itemArray.getString(i);
                        String replaceString = value.replace("#","");
                        searchBoxStoryList.add(replaceString);
                        listview.setAdapter(listAdapter);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                };


            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(jr);


        return layout;
    }

    private void initView(View rootView) {
        searchView = (SearchView) rootView.findViewById(R.id.search);
        recyclerView=(RecyclerView) rootView.findViewById(R.id.listView);
        listview = (ListView) rootView.findViewById(R.id.mobile_list);
        searchBox = (SearchView) rootView.findViewById(R.id.search);

    }





    private void updateFragment() {
        EventBus.getDefault().postSticky(new FragmentState(SEARCH));
    }

    protected void registerEventBus() {
        EventBus.getDefault().registerSticky(this);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void onEvent(DashboardRequest dashboardRequest) {
        Log.d(TAG, dashboardRequest.getStoriesResponse());
        if (dashboardRequest.getStoriesResponse().equals(STORIES_RESPONSE)) {
            storiesResponseList = LocalStorage.getInstance(getContext()).getStories();
        }
    }

    @Override
    public void onProfileGridTypeChange(boolean isStories) {

    }

    @Override
    public void onProfileGridItemClick(View view, boolean isLikeItem) {
        int position = recyclerView.getChildAdapterPosition(view);
        if (isLikeItem) {
            Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(KEY_LIKED_STORIES, (Serializable) likesItemList);
            bundle.putInt(KEY_LIKED_STORY_INDEX, position);
            intent.putExtra(KEY_LIKED_STORIES, bundle);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_up, 0);
        } else {
            Bundle storiesResponseBundle = new Bundle();
            storiesResponseBundle.putSerializable(KEY_CATEGORY_STORIES, storiesResponseList.get(position));

            Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
            intent.putExtra(KEY_CATEGORY_NAME, storiesResponseList.get(position).getCategoryTitle() + "");
            intent.putExtra(KEY_CATEGORY_ID, storiesResponseList.get(position).getCategoryStories().get(0).getCategoryId() + "");
            intent.putExtra(KEY_CATEGORY_STORIES, storiesResponseBundle);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_up, 0);
        }
    }


    //fetchStoryTitles
    public void fetchCatagoryAndTAG(String keyUserId, String keyCategoryName) {
        postTAG(getActivity(),keyUserId,keyCategoryName);
    }


    @Override
    public void response(Object response, Error error) {


    }


    // Story Search List
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        listAdapter.getFilter().filter(newText);
        return true;


    }

    public void postTAG(Context context, final  String ID ,final String tagName){
        RequestQueue queue = Volley.newRequestQueue(context);
        final String URL = BASE_URL+"search/storyByTag?user_id="+ID+"&tags="+tagName;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", LocalStorage.getInstance(getActivity()).getToken());

        JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: "+response);

                        JSONObject jsonObject= response.optJSONObject("data").optJSONObject("all_data");
                        if(jsonObject!= null)
                        {
                            Gson gson = new Gson();
                            List<StoriesResponse> storiesResponseList = new ArrayList<>();
                            Iterator<?> keys = jsonObject.keys();

                            while (keys.hasNext()) {
                                String key = (String) keys.next();
                                StoriesResponse storiesResponse = new StoriesResponse();
                                storiesResponse.setCategoryTitle(key);
                                if (jsonObject.optJSONArray(key) != null && jsonObject.optJSONArray(key).length() > 0) {
                                    Type storyListType = new TypeToken<List<Story>>() {
                                    }.getType();
                                    List<Story> stories = gson.fromJson(jsonObject.optJSONArray(key).toString(), storyListType);
                                    storiesResponse.setCategoryStories(stories);
                                    storiesResponseList.add(storiesResponse);


                                        Bundle storiesResponseBundle = new Bundle();
                                        storiesResponseBundle.putSerializable(KEY_TAG_STORIES_PREF, storiesResponse);

                                        Intent intent = new Intent(getActivity(), TAGActivity.class);
                                        intent.putExtra(KEY_CATEGORY_NAME, storiesResponse.getCategoryTitle() + "");
                                        intent.putExtra(KEY_CATEGORY_ID, storiesResponse.getCategoryStories().get(0).getCategoryId() + "");
                                        intent.putExtra(KEY_CATEGORY_STORIES, storiesResponseBundle);
                                        startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.slide_in_up, 0);

                                }
                            }

                            LocalStorage.getInstance(getContext()).setTagStories(gson.toJson(storiesResponseList));
                        }
                        else {
                            Toast.makeText(getContext(),"No Result Found", Toast.LENGTH_SHORT).show();
                        }

                        }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        queue.add(request_json);


    }



    //Grid View 2
    private void setUpProfileGrid() {

        profileGridLayoutManager = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);
        if (isLikeView)
            searchAdapter = new SearchAdapter(null, likesItemList, this);
        else
            searchAdapter = new SearchAdapter(storiesResponseList, null, this);
        recyclerView.setLayoutManager(profileGridLayoutManager);
        if (!isDecorationAdded) {
            isDecorationAdded = true;
            recyclerView.addItemDecoration(new GridDividerDecoration(getResources().getDimensionPixelSize(R.dimen.likes_item_margin), 2));
        }
        recyclerView.setAdapter(searchAdapter);
    }
    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    fragmentTransaction();
                    return true;
                }

                return false;
            }
        });
    }


    public void fragmentTransaction()
    {
        Fragment fragment = new BaseFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.ad_fragment_container, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }


}
