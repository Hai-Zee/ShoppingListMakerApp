package com.example.shoppinglist_zeeshan.WelcomeScreen;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shoppinglist_zeeshan.R;


public class FirstFragment extends Fragment {

    TextView nextButtonFrag_1;
    FirstFragInterface firstFragInterface;
    public FirstFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nextButtonFrag_1 = view.findViewById(R.id.nextButtonFrag_1_ID);

        nextButtonFrag_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstFragInterface.implementFirstFragInterface();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        firstFragInterface = (FirstFragInterface) context;
    }

    interface FirstFragInterface{
        void implementFirstFragInterface();
    }
}

