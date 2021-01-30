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

public class SecFragment extends Fragment {

    TextView nextButtonFrag_2, backButtonFrag2;
    SecondFragInterface secondFragInterface;

    public SecFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sec, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backButtonFrag2 = view.findViewById(R.id.backButtonFrag_2_ID);
        nextButtonFrag_2 = view.findViewById(R.id.nextButtonFrag_2_ID);

        nextButtonFrag_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secondFragInterface.implementSecondFragInterfaceNext();
            }
        });

        backButtonFrag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secondFragInterface.implementSecondFragInterfaceBack();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        secondFragInterface = (SecondFragInterface) context;
    }

    interface SecondFragInterface{
        void implementSecondFragInterfaceNext();
        void implementSecondFragInterfaceBack();
    }
}