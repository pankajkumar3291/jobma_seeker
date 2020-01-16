package com.smartit.jobSeeker.multiSelectSpinnerWithLimit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatSpinner;

import com.smartit.jobSeeker.App;
import com.smartit.jobSeeker.R;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by V1k on 08-Sep-17.
 */

public class MultipleSelectionSpinnerIndustry extends AppCompatSpinner implements
        DialogInterface.OnMultiChoiceClickListener {

    String[] _items = null;
    boolean[] mSelection = null;
    int selectedPosition;

    ArrayAdapter<String> simple_adapter;
    private int sbLength;

    public MultipleSelectionSpinnerIndustry(Context context) {
        super(context);

        simple_adapter = new ArrayAdapter<String>(context,
                R.layout.spinner_item_others_details);
        super.setAdapter(simple_adapter);
    }

    public MultipleSelectionSpinnerIndustry(Context context, AttributeSet attrs) {
        super(context, attrs);

        simple_adapter = new ArrayAdapter<String>(context,
                R.layout.spinner_item_others_details);
        super.setAdapter(simple_adapter);
    }

    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (mSelection != null && which < mSelection.length) {
            mSelection[which] = isChecked;
            simple_adapter.clear();
//            simple_adapter.add(buildSelectedItemString());

            if (buildSelectedItemString().length() > 0) {
                simple_adapter.add(buildSelectedItemString());
            } else {
                simple_adapter.add("Industry");
            }

            if (isChecked) {
                selectedPosition += simple_adapter.getCount();
                if (selectedPosition > 3) {
                    dialog.dismiss();
                    mSelection[which] = false;
                    selectedPosition -= simple_adapter.getCount();
                    Toast.makeText(App.context, "Maximum 3 can be select", Toast.LENGTH_SHORT).show();

                } else {
                    mSelection[which] = isChecked;
                }
            } else {

                selectedPosition -= simple_adapter.getCount();
            }


        } else {
            throw new IllegalArgumentException(
                    "Argument 'which' is out of bounds");
        }
    }

    @Override
    public boolean performClick() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(_items, mSelection, this);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }

        });
        /*if (mSelection.length > 3){
            Toast.makeText(getContext(), "Cannot select more than 3", Toast.LENGTH_SHORT).show();
            return false;
        }*/
        builder.show();
        return true;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        throw new RuntimeException(
                "setAdapter is not supported by MultiSelectSpinner.");
    }

    public void setItems(String[] items) {
        _items = items;
        mSelection = new boolean[_items.length];
        simple_adapter.clear();
        simple_adapter.add(_items[0]);
        Arrays.fill(mSelection, false);
    }

    public void setItems(List<String> items) {
        _items = items.toArray(new String[items.size()]);
        mSelection = new boolean[_items.length];
        simple_adapter.clear();
        simple_adapter.add("Industry");
        ///simple_adapter.add(_items[0]);
        Arrays.fill(mSelection, false);
    }

//    public void setSelection(String[] selection) {
//        for (String cell : selection) {
//            for (int j = 0; j < _items.length; ++j) {
//                if (_items[j].equals(cell)) {
//                    mSelection[j] = true;
//                }
//            }
//        }
//    }


    public void setSelection(String[] selection) {
        for (String cell : selection) {
            for (int j = 0; j < _items.length; ++j) {
                if (_items[j].equals(cell)) {
                    mSelection[j] = true;
                    selectedPosition += 1;
                }
            }
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }


    public void setSelection(List<String> selection) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
        }
        for (String sel : selection) {
            for (int j = 0; j < _items.length; ++j) {
                if (_items[j].equals(sel)) {
                    mSelection[j] = true;
                }
            }
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    public void setSelection(int index) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
        }
        if (index >= 0 && index < mSelection.length) {
            mSelection[index] = true;
        } else {
            throw new IllegalArgumentException("Index " + index
                    + " is out of bounds.");
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
        /*if (sbLength>0){
            Toast.makeText(getContext(), "Length greater than zero", Toast.LENGTH_SHORT).show();
            simple_adapter.add(buildSelectedItemString());
        }else{
            Toast.makeText(getContext(), "Length shorter", Toast.LENGTH_SHORT).show();
            simple_adapter.add("Tap to select");
        }*/
    }

    public void setSelection(int[] selectedIndicies) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
        }
        for (int index : selectedIndicies) {
            if (index >= 0 && index < mSelection.length) {
                mSelection[index] = true;
            } else {
                throw new IllegalArgumentException("Index " + index
                        + " is out of bounds.");
            }
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    public List<String> getSelectedStrings() {
        List<String> selection = new LinkedList<String>();
        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                selection.add(_items[i]);
            }
        }
        return selection;
    }

    public List<Integer> getSelectedIndicies() {
        List<Integer> selection = new LinkedList<Integer>();
        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                selection.add(i);
            }
        }
        return selection;
    }

    private String buildSelectedItemString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {

                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;

                sb.append(_items[i]);
            }
        }

        //Log.e("sb length",""+sb.length());
        sbLength = sb.length();
        return sb.toString();
    }

    public String getSelectedItemsAsString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;
                sb.append(_items[i]);
            }
        }
        /*String sbCheck;
        if (sb.length()>0){
           sbCheck=sb.toString();
        }else{
            sbCheck="Tap to select";
        }*/
        return sb.toString();
    }
}