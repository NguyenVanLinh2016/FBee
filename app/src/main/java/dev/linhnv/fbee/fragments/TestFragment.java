package dev.linhnv.fbee.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

import dev.linhnv.fbee.QuizOccupationActivity;
import dev.linhnv.fbee.QuizPersonalityActivity;
import dev.linhnv.fbee.R;
import dev.linhnv.fbee.TestAndGiftActivity;

/**
 * Created by DevLinhnv on 12/31/2016.
 */

public class TestFragment extends Fragment implements View.OnClickListener{
    TestAndGiftActivity testAndGiftActivity;
    Button btn_start;
    int count = 0;
    public TestFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        testAndGiftActivity = (TestAndGiftActivity) getActivity();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_test, container, false);
        MultiStateToggleButton button = (MultiStateToggleButton) root.findViewById(R.id.mstb_multi_id);
        button.setValue(0);

        button.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int position) {
                Log.d("TestFragment", "Position: " + position);
                count = position;
            }
        });
        btn_start = (Button) root.findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_start:
                if(count == 0){
                    Intent intent = new Intent(view.getContext(), QuizOccupationActivity.class);
                    view.getContext().startActivity(intent);
                    getActivity().finish();
                }else{
                    Intent intent = new Intent(view.getContext(), QuizPersonalityActivity.class);
                    view.getContext().startActivity(intent);
                    getActivity().finish();
                }
                break;
        }
    }
}
