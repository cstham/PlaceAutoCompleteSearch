package code.cameo.placeautocomplete;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

public class MoreInfoFragment extends Fragment implements View.OnClickListener, View.OnTouchListener, TextWatcher {

    private ImageView mIvBack, mIvClear;
    private EditText mEtSearchText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.more_info, container, false);

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
        //Utils.hideKeyboardFromDialog(getActivity(),mEtSearchText);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP){
            //startActivity( new Intent(getActivity(), CommonTabActivity.class));
            Intent i=new Intent(getActivity(), CommonTabActivity.class);
            startActivityForResult(i,1);  //<-- 1 is request code, you can give anything.
        }
        return false;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence query, int start, int before, int count) {
        String searchText = query.toString();
        //System.out.println("searchText lol: "+searchText);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == 1 && resultCode == MapsActivity.RESULT_OK)
        {
            String selectedPosition= data.getExtras().getString("position");
            //deserialization to Latlng
            Gson gson = new Gson();
            LatLng position = gson.fromJson(selectedPosition, LatLng.class);

            ((MapsActivity)getActivity()).onPlaceSelected(position);

        }else
        {
            System.out.println("no response lol");
        }
    }



}
