package com.example.sanguage;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sanguage.pojo.DictionaryPojo;
import com.example.sanguage.utils.FlashcardAdapter;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;

public class LearnFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FlashcardAdapter flashcardAdapter;
    private SwipeFlingAdapterView flingContainer;
    private ArrayList<DictionaryPojo> dictionaryListSimple;
    private ArrayList<DictionaryPojo> dictionaryListBuffer;
    private Context context;

    public enum DictionaryListEnum {SIMPLE, BUFFER}

    private String mParam1;
    private String mParam2;

    public LearnFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public static LearnFragment newInstance(String param1, String param2) {
        LearnFragment fragment = new LearnFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_learn);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void setContentView(int fragment_learn) {
    }

    public void setFlingContainer() {
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                dictionaryListSimple.remove(0);
                flashcardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {
                System.out.println("rejected");
            }

            @Override
            public void onRightCardExit(Object o) {
                System.out.println("added");
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {

            }

            @Override
            public void onScroll(float v) {
                ;
            }
        });

        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                System.out.println("onitemclicked");

            }
        });
    }
/*public void dictionaryRequest(DictionaryListEnum i) {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, currentURL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    DictionaryPojo[] dictionary = mapper.readValue(response.toString(), DictionaryPojo[].class);
                    if (i.equals(DictionaryListEnum.SIMPLE)) {
                        dictionaryListSimple.addAll(Arrays.asList(dictionary));
                    } else {
                        dictionaryListBuffer.addAll(Arrays.asList(dictionary));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (i.equals(DictionaryListEnum.SIMPLE)) {
                    dictionaryListSimple.addAll(dictionaryListBuffer);
                }
            }
        });
        queue.add(jsonArrayRequest);
    }*/

    /*public void updateFlashcardAdapter(ArrayList<DictionaryPojo> dictList) {
        if (flashcardAdapter == null) {
            createFlashcardAdapter(dictionaryListSimple);
        } else {
            setFlashcardAdapter(dictionaryListSimple);
        }
    }

    public void createFlashcardAdapter(ArrayList<DictionaryPojo> dictList) {
        FlashcardAdapter flashcardAdapter = new FlashcardAdapter(this, dictList);
        flingContainer.setAdapter(flashcardAdapter);
    }*/

    public void setFlashcardAdapter(ArrayList<DictionaryPojo> dictList) {
        flashcardAdapter.clear();
        flashcardAdapter.addAll(dictList);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_learn, container, false);

        flingContainer = (SwipeFlingAdapterView) view.findViewById(R.id.frame_flashcard);

        setFlingContainer();

        dictionaryListSimple = new ArrayList<>();
        dictionaryListBuffer = new ArrayList<>();

        dictionaryListSimple.add(new DictionaryPojo("das312312daas"));
        dictionaryListSimple.add(new DictionaryPojo("dasd1w12s12aas"));
        dictionaryListSimple.add(new DictionaryPojo("dasd12s12aas"));


        flashcardAdapter = new FlashcardAdapter(context, R.layout.flashcard, dictionaryListSimple);
        flingContainer.setAdapter(flashcardAdapter);

        return view;
    }

}