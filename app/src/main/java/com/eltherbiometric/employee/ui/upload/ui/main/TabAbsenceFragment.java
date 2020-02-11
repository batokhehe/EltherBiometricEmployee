package com.eltherbiometric.employee.ui.upload.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eltherbiometric.employee.R;
import com.eltherbiometric.employee.data.model.Presence;
import com.eltherbiometric.employee.data.sqllite.Services;

import java.util.ArrayList;
import java.util.List;

public class TabAbsenceFragment extends Fragment {

    private TabAbsenceAdapter adapter;
    private List<Presence> dataList = new ArrayList<>();
    private Services services;
    private RecyclerView rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_absence, container, false);
        rv = root.findViewById(R.id.rv);
        services = new Services(getContext());
        dataList.clear();
        dataList.addAll(services.AllAbsence());
        adapter = new TabAbsenceAdapter(dataList, getContext());

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
        return root;
    }
}
