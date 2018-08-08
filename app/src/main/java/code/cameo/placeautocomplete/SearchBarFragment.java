package code.cameo.placeautocomplete;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class SearchBarFragment extends Fragment implements View.OnClickListener, View.OnTouchListener, TextWatcher {

    private ImageView mIvBack, mIvClear;
    private EditText mEtSearchText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_box, container, false);

        initializeView(view);

        //initListeners();
        mIvBack.setOnClickListener(this);
        mIvClear.setOnClickListener(this);
        mEtSearchText.setOnTouchListener(this);
        mEtSearchText.addTextChangedListener(this);

        return view;
    }


    private void initializeView(View view) {
        mIvBack = (ImageView) view.findViewById(R.id.iv_back);
        mIvClear = (ImageView) view.findViewById(R.id.iv_clear_search_box);
        mEtSearchText = (EditText) view.findViewById(R.id.et_search_text);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();

        if (id == R.id.iv_back) {
            resetSearchBox();
            performBackAction();
        }
        else if(id == R.id.iv_clear_search_box){
            resetSearchBox();
        }
    }

    private void enableSearchField() {
        mIvBack.setImageResource(R.drawable.ic_arrow_back);
    }

    private void resetSearchBox() {
        mEtSearchText.setText("");
    }

    protected void performBackAction() {
        mIvBack.setImageResource(R.drawable.ic_search);
        mEtSearchText.clearFocus();
        Utils.hideKeyboardFromDialog(getActivity(),mEtSearchText);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP){
            //startActivity( new Intent(getActivity(), CommonTabActivity.class));
            enableSearchField();
        }
        return false;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence query, int start, int before, int count) {
        String searchText = query.toString();
        System.out.println("searchText lol: "+searchText);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }



}
