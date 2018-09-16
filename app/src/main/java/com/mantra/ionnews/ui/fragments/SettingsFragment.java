package com.mantra.ionnews.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mantra.ionnews.R;
import com.mantra.ionnews.models.FragmentState;
import com.mantra.ionnews.ui.activities.OnboardingActivity;
import com.mantra.ionnews.utils.LocalStorage;

import de.greenrobot.event.EventBus;

import static com.mantra.ionnews.utils.ConstantClass.SETTINGS;

/**
 * Created by TaNMay on 03/05/17.
 */

public class SettingsFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private static String TAG = SETTINGS + " ==>";

    private LinearLayout aboutSec, helpFeedbackSec, logoutSec;
    private TextView aboutDesc;
    private ImageView back;

    public SettingsFragment() {

    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_settings, container, false);

        updateFragment();
        initView(layout);

        setUpHeader();
        setUpSettingsOptions();

        return layout;
    }

    private void initView(View rootView) {
        aboutSec = (LinearLayout) rootView.findViewById(R.id.bss_about_sec);
        helpFeedbackSec = (LinearLayout) rootView.findViewById(R.id.bss_help_feedback_sec);
        logoutSec = (LinearLayout) rootView.findViewById(R.id.bss_logout_sec);
        aboutDesc = (TextView) rootView.findViewById(R.id.bss_about_desc);
        back = (ImageView) rootView.findViewById(R.id.bss_back);
    }

    private void updateFragment() {
        EventBus.getDefault().postSticky(new FragmentState(SETTINGS));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setUpHeader() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void setUpSettingsOptions() {
        PackageInfo pInfo = null;
        String appVersion = "";
        try {
            pInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            appVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        aboutDesc.setText(getString(R.string.app_name) + "  " + appVersion);

        logoutSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogoutClick();
            }
        });
    }

    private void onLogoutClick() {
        LocalStorage.getInstance(getContext()).clearLocalStorage();
        Intent i = new Intent(getContext(), OnboardingActivity.class);
        startActivity(i);
        getActivity().finish();
    }
}